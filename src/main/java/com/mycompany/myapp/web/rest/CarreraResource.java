package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Carrera;
import com.mycompany.myapp.repository.CarreraRepository;
import com.mycompany.myapp.service.CarreraService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Carrera}.
 */
@RestController
@RequestMapping("/api")
public class CarreraResource {

    private final Logger log = LoggerFactory.getLogger(CarreraResource.class);

    private static final String ENTITY_NAME = "carrera";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarreraService carreraService;

    private final CarreraRepository carreraRepository;

    public CarreraResource(CarreraService carreraService, CarreraRepository carreraRepository) {
        this.carreraService = carreraService;
        this.carreraRepository = carreraRepository;
    }

    /**
     * {@code POST  /carreras} : Create a new carrera.
     *
     * @param carrera the carrera to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carrera, or with status {@code 400 (Bad Request)} if the carrera has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/carreras")
    public ResponseEntity<Carrera> createCarrera(@Valid @RequestBody Carrera carrera) throws URISyntaxException {
        log.debug("REST request to save Carrera : {}", carrera);
        if (carrera.getId() != null) {
            throw new BadRequestAlertException("A new carrera cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Carrera result = carreraService.save(carrera);
        return ResponseEntity
            .created(new URI("/api/carreras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /carreras/:id} : Updates an existing carrera.
     *
     * @param id the id of the carrera to save.
     * @param carrera the carrera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carrera,
     * or with status {@code 400 (Bad Request)} if the carrera is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carrera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/carreras/{id}")
    public ResponseEntity<Carrera> updateCarrera(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Carrera carrera
    ) throws URISyntaxException {
        log.debug("REST request to update Carrera : {}, {}", id, carrera);
        if (carrera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carrera.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carreraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Carrera result = carreraService.save(carrera);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carrera.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /carreras/:id} : Partial updates given fields of an existing carrera, field will ignore if it is null
     *
     * @param id the id of the carrera to save.
     * @param carrera the carrera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carrera,
     * or with status {@code 400 (Bad Request)} if the carrera is not valid,
     * or with status {@code 404 (Not Found)} if the carrera is not found,
     * or with status {@code 500 (Internal Server Error)} if the carrera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/carreras/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Carrera> partialUpdateCarrera(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Carrera carrera
    ) throws URISyntaxException {
        log.debug("REST request to partial update Carrera partially : {}, {}", id, carrera);
        if (carrera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carrera.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carreraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Carrera> result = carreraService.partialUpdate(carrera);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carrera.getId().toString())
        );
    }

    @GetMapping(path = "getcarrerasganadasdeunpiloto/{apellido}")
    public List<Carrera> getCarrerasGanadasDeUnPiloto(@PathVariable(name = "apellido") String apellido) {
        return carreraService.getCarrerasGanadasDeUnPiloto(apellido);
    }

    @GetMapping(path = "getnumeroganadasdeunpiloto/{apellido}/{edad}")
    public int getNumeroGanaddasDeUnPiloto(@PathVariable(name = "apellido") String apellido, @PathVariable(name = "edad") int edad) {
        return carreraService.getNumeroGanaddasDeUnPiloto(apellido, edad);
    }

    @GetMapping(path = "getsegundopuestodeunpiloto/{nombre}/{apellido}/{coche}")
    public List<Carrera> getCarrerasSegundoPuestoDeUnPiloto(
        @PathVariable(name = "nombre") String nombre,
        @PathVariable(name = "apellido") String apellido,
        @PathVariable(name = "coche") String coche
    ) {
        return carreraService.getCarrerasSegundoPuestoDeUnPiloto(nombre, apellido, coche);
    }

    /**
     * {@code GET  /carreras} : get all the carreras.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carreras in body.
     */
    @GetMapping("/carreras")
    public List<Carrera> getAllCarreras(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Carreras");
        return carreraService.findAll();
    }

    /**
     * {@code GET  /carreras/:id} : get the "id" carrera.
     *
     * @param id the id of the carrera to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carrera, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/carreras/{id}")
    public ResponseEntity<Carrera> getCarrera(@PathVariable Long id) {
        log.debug("REST request to get Carrera : {}", id);
        Optional<Carrera> carrera = carreraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carrera);
    }

    /**
     * {@code DELETE  /carreras/:id} : delete the "id" carrera.
     *
     * @param id the id of the carrera to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/carreras/{id}")
    public ResponseEntity<Void> deleteCarrera(@PathVariable Long id) {
        log.debug("REST request to delete Carrera : {}", id);
        carreraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
