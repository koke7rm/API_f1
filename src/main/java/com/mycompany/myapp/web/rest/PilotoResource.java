package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Piloto;
import com.mycompany.myapp.repository.PilotoRepository;
import com.mycompany.myapp.service.PilotoService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Piloto}.
 */
@RestController
@RequestMapping("/api")
public class PilotoResource {

    private final Logger log = LoggerFactory.getLogger(PilotoResource.class);

    private static final String ENTITY_NAME = "piloto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PilotoService pilotoService;

    private final PilotoRepository pilotoRepository;

    public PilotoResource(PilotoService pilotoService, PilotoRepository pilotoRepository) {
        this.pilotoService = pilotoService;
        this.pilotoRepository = pilotoRepository;
    }

    /**
     * {@code POST  /pilotos} : Create a new piloto.
     *
     * @param piloto the piloto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new piloto, or with status {@code 400 (Bad Request)} if the piloto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pilotos")
    public ResponseEntity<Piloto> createPiloto(@Valid @RequestBody Piloto piloto) throws URISyntaxException {
        log.debug("REST request to save Piloto : {}", piloto);
        if (piloto.getId() != null) {
            throw new BadRequestAlertException("A new piloto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Piloto result = pilotoService.save(piloto);
        return ResponseEntity
            .created(new URI("/api/pilotos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pilotos/:id} : Updates an existing piloto.
     *
     * @param id the id of the piloto to save.
     * @param piloto the piloto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piloto,
     * or with status {@code 400 (Bad Request)} if the piloto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the piloto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pilotos/{id}")
    public ResponseEntity<Piloto> updatePiloto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Piloto piloto
    ) throws URISyntaxException {
        log.debug("REST request to update Piloto : {}, {}", id, piloto);
        if (piloto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, piloto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pilotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Piloto result = pilotoService.save(piloto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, piloto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pilotos/:id} : Partial updates given fields of an existing piloto, field will ignore if it is null
     *
     * @param id the id of the piloto to save.
     * @param piloto the piloto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piloto,
     * or with status {@code 400 (Bad Request)} if the piloto is not valid,
     * or with status {@code 404 (Not Found)} if the piloto is not found,
     * or with status {@code 500 (Internal Server Error)} if the piloto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pilotos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Piloto> partialUpdatePiloto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Piloto piloto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Piloto partially : {}, {}", id, piloto);
        if (piloto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, piloto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pilotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Piloto> result = pilotoService.partialUpdate(piloto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, piloto.getId().toString())
        );
    }

    /**
     * {@code GET  /pilotos} : get all the pilotos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pilotos in body.
     */
    @GetMapping("/pilotos")
    public List<Piloto> getAllPilotos() {
        log.debug("REST request to get all Pilotos");
        return pilotoService.findAll();
    }

    /**
     * {@code GET  /pilotos/:id} : get the "id" piloto.
     *
     * @param id the id of the piloto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the piloto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pilotos/{id}")
    public ResponseEntity<Piloto> getPiloto(@PathVariable Long id) {
        log.debug("REST request to get Piloto : {}", id);
        Optional<Piloto> piloto = pilotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(piloto);
    }

    /**
     * {@code DELETE  /pilotos/:id} : delete the "id" piloto.
     *
     * @param id the id of the piloto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pilotos/{id}")
    public ResponseEntity<Void> deletePiloto(@PathVariable Long id) {
        log.debug("REST request to delete Piloto : {}", id);
        pilotoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
