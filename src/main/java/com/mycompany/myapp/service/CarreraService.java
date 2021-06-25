package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Carrera;
import com.mycompany.myapp.repository.CarreraRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Carrera}.
 */
@Service
@Transactional
public class CarreraService {

    private final Logger log = LoggerFactory.getLogger(CarreraService.class);

    private final CarreraRepository carreraRepository;

    public CarreraService(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    /**
     * Save a carrera.
     *
     * @param carrera the entity to save.
     * @return the persisted entity.
     */
    public Carrera save(Carrera carrera) {
        log.debug("Request to save Carrera : {}", carrera);
        return carreraRepository.save(carrera);
    }

    /**
     * Partially update a carrera.
     *
     * @param carrera the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Carrera> partialUpdate(Carrera carrera) {
        log.debug("Request to partially update Carrera : {}", carrera);

        return carreraRepository
            .findById(carrera.getId())
            .map(
                existingCarrera -> {
                    if (carrera.getNombreCircuito() != null) {
                        existingCarrera.setNombreCircuito(carrera.getNombreCircuito());
                    }
                    if (carrera.getPais() != null) {
                        existingCarrera.setPais(carrera.getPais());
                    }

                    return existingCarrera;
                }
            )
            .map(carreraRepository::save);
    }

    public List<Carrera> getCarrerasGanadasDeUnPiloto(String apellido) {
        return carreraRepository.findByGanador_apellido(apellido);
    }

    public int getNumeroGanaddasDeUnPiloto(String apellido, int edad) {
        return carreraRepository.countByGanador_apellidoAndGanador_edad(apellido, edad);
    }

    public List<Carrera> getCarrerasSegundoPuestoDeUnPiloto(String nombre, String apellido, String coche) {
        return carreraRepository.findBySegundoPuesto_nombreAndSegundoPuesto_apellidoAndSegundoPuesto_coche(nombre, apellido, coche);
    }

    /**
     * Get all the carreras.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Carrera> findAll() {
        log.debug("Request to get all Carreras");
        return carreraRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the carreras with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Carrera> findAllWithEagerRelationships(Pageable pageable) {
        return carreraRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one carrera by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Carrera> findOne(Long id) {
        log.debug("Request to get Carrera : {}", id);
        return carreraRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the carrera by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Carrera : {}", id);
        carreraRepository.deleteById(id);
    }
}
