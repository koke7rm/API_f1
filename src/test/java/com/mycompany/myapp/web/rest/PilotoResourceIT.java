package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Piloto;
import com.mycompany.myapp.repository.PilotoRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PilotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PilotoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDAD = 1;
    private static final Integer UPDATED_EDAD = 2;

    private static final String DEFAULT_COCHE = "AAAAAAAAAA";
    private static final String UPDATED_COCHE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pilotos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PilotoRepository pilotoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPilotoMockMvc;

    private Piloto piloto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piloto createEntity(EntityManager em) {
        Piloto piloto = new Piloto().nombre(DEFAULT_NOMBRE).apellido(DEFAULT_APELLIDO).edad(DEFAULT_EDAD).coche(DEFAULT_COCHE);
        return piloto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piloto createUpdatedEntity(EntityManager em) {
        Piloto piloto = new Piloto().nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).edad(UPDATED_EDAD).coche(UPDATED_COCHE);
        return piloto;
    }

    @BeforeEach
    public void initTest() {
        piloto = createEntity(em);
    }

    @Test
    @Transactional
    void createPiloto() throws Exception {
        int databaseSizeBeforeCreate = pilotoRepository.findAll().size();
        // Create the Piloto
        restPilotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(piloto)))
            .andExpect(status().isCreated());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeCreate + 1);
        Piloto testPiloto = pilotoList.get(pilotoList.size() - 1);
        assertThat(testPiloto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPiloto.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testPiloto.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testPiloto.getCoche()).isEqualTo(DEFAULT_COCHE);
    }

    @Test
    @Transactional
    void createPilotoWithExistingId() throws Exception {
        // Create the Piloto with an existing ID
        piloto.setId(1L);

        int databaseSizeBeforeCreate = pilotoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPilotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(piloto)))
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPilotos() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get all the pilotoList
        restPilotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(piloto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)))
            .andExpect(jsonPath("$.[*].coche").value(hasItem(DEFAULT_COCHE)));
    }

    @Test
    @Transactional
    void getPiloto() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        // Get the piloto
        restPilotoMockMvc
            .perform(get(ENTITY_API_URL_ID, piloto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(piloto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD))
            .andExpect(jsonPath("$.coche").value(DEFAULT_COCHE));
    }

    @Test
    @Transactional
    void getNonExistingPiloto() throws Exception {
        // Get the piloto
        restPilotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPiloto() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();

        // Update the piloto
        Piloto updatedPiloto = pilotoRepository.findById(piloto.getId()).get();
        // Disconnect from session so that the updates on updatedPiloto are not directly saved in db
        em.detach(updatedPiloto);
        updatedPiloto.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).edad(UPDATED_EDAD).coche(UPDATED_COCHE);

        restPilotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPiloto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPiloto))
            )
            .andExpect(status().isOk());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
        Piloto testPiloto = pilotoList.get(pilotoList.size() - 1);
        assertThat(testPiloto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPiloto.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testPiloto.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testPiloto.getCoche()).isEqualTo(UPDATED_COCHE);
    }

    @Test
    @Transactional
    void putNonExistingPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, piloto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(piloto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(piloto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(piloto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePilotoWithPatch() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();

        // Update the piloto using partial update
        Piloto partialUpdatedPiloto = new Piloto();
        partialUpdatedPiloto.setId(piloto.getId());

        partialUpdatedPiloto.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO);

        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPiloto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPiloto))
            )
            .andExpect(status().isOk());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
        Piloto testPiloto = pilotoList.get(pilotoList.size() - 1);
        assertThat(testPiloto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPiloto.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testPiloto.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testPiloto.getCoche()).isEqualTo(DEFAULT_COCHE);
    }

    @Test
    @Transactional
    void fullUpdatePilotoWithPatch() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();

        // Update the piloto using partial update
        Piloto partialUpdatedPiloto = new Piloto();
        partialUpdatedPiloto.setId(piloto.getId());

        partialUpdatedPiloto.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).edad(UPDATED_EDAD).coche(UPDATED_COCHE);

        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPiloto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPiloto))
            )
            .andExpect(status().isOk());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
        Piloto testPiloto = pilotoList.get(pilotoList.size() - 1);
        assertThat(testPiloto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPiloto.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testPiloto.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testPiloto.getCoche()).isEqualTo(UPDATED_COCHE);
    }

    @Test
    @Transactional
    void patchNonExistingPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, piloto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(piloto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(piloto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPiloto() throws Exception {
        int databaseSizeBeforeUpdate = pilotoRepository.findAll().size();
        piloto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPilotoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(piloto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Piloto in the database
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePiloto() throws Exception {
        // Initialize the database
        pilotoRepository.saveAndFlush(piloto);

        int databaseSizeBeforeDelete = pilotoRepository.findAll().size();

        // Delete the piloto
        restPilotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, piloto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Piloto> pilotoList = pilotoRepository.findAll();
        assertThat(pilotoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
