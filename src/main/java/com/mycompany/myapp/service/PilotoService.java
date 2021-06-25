package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Piloto;
import com.mycompany.myapp.repository.PilotoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Piloto}.
 */
@Service
@Transactional
public class PilotoService {

    private final Logger log = LoggerFactory.getLogger(PilotoService.class);

    private final PilotoRepository pilotoRepository;

    public PilotoService(PilotoRepository pilotoRepository) {
        this.pilotoRepository = pilotoRepository;
    }

    /**
     * Save a piloto.
     *
     * @param piloto the entity to save.
     * @return the persisted entity.
     */
    public Piloto save(Piloto piloto) {
        log.debug("Request to save Piloto : {}", piloto);
        return pilotoRepository.save(piloto);
    }

    /**
     * Partially update a piloto.
     *
     * @param piloto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Piloto> partialUpdate(Piloto piloto) {
        log.debug("Request to partially update Piloto : {}", piloto);

        return pilotoRepository
            .findById(piloto.getId())
            .map(
                existingPiloto -> {
                    if (piloto.getNombre() != null) {
                        existingPiloto.setNombre(piloto.getNombre());
                    }
                    if (piloto.getApellido() != null) {
                        existingPiloto.setApellido(piloto.getApellido());
                    }
                    if (piloto.getEdad() != null) {
                        existingPiloto.setEdad(piloto.getEdad());
                    }
                    if (piloto.getCoche() != null) {
                        existingPiloto.setCoche(piloto.getCoche());
                    }

                    return existingPiloto;
                }
            )
            .map(pilotoRepository::save);
    }

    /**
     * Get all the pilotos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Piloto> findAll() {
        log.debug("Request to get all Pilotos");
        return pilotoRepository.findAll();
    }

    /**
     * Get one piloto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Piloto> findOne(Long id) {
        log.debug("Request to get Piloto : {}", id);
        return pilotoRepository.findById(id);
    }

    /**
     * Delete the piloto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Piloto : {}", id);
        pilotoRepository.deleteById(id);
    }
}
