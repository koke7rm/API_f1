package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Carrera;
import com.mycompany.myapp.repository.CarreraRepository;
import com.mycompany.myapp.service.CarreraService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarreraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarreraResourceIT {

    private static final String DEFAULT_NOMBRE_CIRCUITO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CIRCUITO = "BBBBBBBBBB";

    private static final String DEFAULT_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_PAIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/carreras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarreraRepository carreraRepository;

    @Mock
    private CarreraRepository carreraRepositoryMock;

    @Mock
    private CarreraService carreraServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarreraMockMvc;

    private Carrera carrera;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrera createEntity(EntityManager em) {
        Carrera carrera = new Carrera().nombreCircuito(DEFAULT_NOMBRE_CIRCUITO).pais(DEFAULT_PAIS);
        return carrera;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrera createUpdatedEntity(EntityManager em) {
        Carrera carrera = new Carrera().nombreCircuito(UPDATED_NOMBRE_CIRCUITO).pais(UPDATED_PAIS);
        return carrera;
    }

    @BeforeEach
    public void initTest() {
        carrera = createEntity(em);
    }

    @Test
    @Transactional
    void createCarrera() throws Exception {
        int databaseSizeBeforeCreate = carreraRepository.findAll().size();
        // Create the Carrera
        restCarreraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carrera)))
            .andExpect(status().isCreated());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeCreate + 1);
        Carrera testCarrera = carreraList.get(carreraList.size() - 1);
        assertThat(testCarrera.getNombreCircuito()).isEqualTo(DEFAULT_NOMBRE_CIRCUITO);
        assertThat(testCarrera.getPais()).isEqualTo(DEFAULT_PAIS);
    }

    @Test
    @Transactional
    void createCarreraWithExistingId() throws Exception {
        // Create the Carrera with an existing ID
        carrera.setId(1L);

        int databaseSizeBeforeCreate = carreraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarreraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carrera)))
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarreras() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        // Get all the carreraList
        restCarreraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrera.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCircuito").value(hasItem(DEFAULT_NOMBRE_CIRCUITO)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarrerasWithEagerRelationshipsIsEnabled() throws Exception {
        when(carreraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarreraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carreraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarrerasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carreraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarreraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carreraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCarrera() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        // Get the carrera
        restCarreraMockMvc
            .perform(get(ENTITY_API_URL_ID, carrera.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carrera.getId().intValue()))
            .andExpect(jsonPath("$.nombreCircuito").value(DEFAULT_NOMBRE_CIRCUITO))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS));
    }

    @Test
    @Transactional
    void getNonExistingCarrera() throws Exception {
        // Get the carrera
        restCarreraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCarrera() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();

        // Update the carrera
        Carrera updatedCarrera = carreraRepository.findById(carrera.getId()).get();
        // Disconnect from session so that the updates on updatedCarrera are not directly saved in db
        em.detach(updatedCarrera);
        updatedCarrera.nombreCircuito(UPDATED_NOMBRE_CIRCUITO).pais(UPDATED_PAIS);

        restCarreraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarrera.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCarrera))
            )
            .andExpect(status().isOk());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
        Carrera testCarrera = carreraList.get(carreraList.size() - 1);
        assertThat(testCarrera.getNombreCircuito()).isEqualTo(UPDATED_NOMBRE_CIRCUITO);
        assertThat(testCarrera.getPais()).isEqualTo(UPDATED_PAIS);
    }

    @Test
    @Transactional
    void putNonExistingCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carrera.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carrera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carrera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carrera)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarreraWithPatch() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();

        // Update the carrera using partial update
        Carrera partialUpdatedCarrera = new Carrera();
        partialUpdatedCarrera.setId(carrera.getId());

        partialUpdatedCarrera.nombreCircuito(UPDATED_NOMBRE_CIRCUITO);

        restCarreraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarrera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarrera))
            )
            .andExpect(status().isOk());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
        Carrera testCarrera = carreraList.get(carreraList.size() - 1);
        assertThat(testCarrera.getNombreCircuito()).isEqualTo(UPDATED_NOMBRE_CIRCUITO);
        assertThat(testCarrera.getPais()).isEqualTo(DEFAULT_PAIS);
    }

    @Test
    @Transactional
    void fullUpdateCarreraWithPatch() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();

        // Update the carrera using partial update
        Carrera partialUpdatedCarrera = new Carrera();
        partialUpdatedCarrera.setId(carrera.getId());

        partialUpdatedCarrera.nombreCircuito(UPDATED_NOMBRE_CIRCUITO).pais(UPDATED_PAIS);

        restCarreraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarrera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarrera))
            )
            .andExpect(status().isOk());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
        Carrera testCarrera = carreraList.get(carreraList.size() - 1);
        assertThat(testCarrera.getNombreCircuito()).isEqualTo(UPDATED_NOMBRE_CIRCUITO);
        assertThat(testCarrera.getPais()).isEqualTo(UPDATED_PAIS);
    }

    @Test
    @Transactional
    void patchNonExistingCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carrera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carrera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carrera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carrera)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarrera() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        int databaseSizeBeforeDelete = carreraRepository.findAll().size();

        // Delete the carrera
        restCarreraMockMvc
            .perform(delete(ENTITY_API_URL_ID, carrera.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
