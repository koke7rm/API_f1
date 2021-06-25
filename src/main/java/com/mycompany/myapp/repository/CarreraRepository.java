package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Carrera;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Carrera entity.
 */
@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    @Query(
        value = "select distinct carrera from Carrera carrera left join fetch carrera.noTerminans",
        countQuery = "select count(distinct carrera) from Carrera carrera"
    )
    Page<Carrera> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct carrera from Carrera carrera left join fetch carrera.noTerminans")
    List<Carrera> findAllWithEagerRelationships();

    @Query("select carrera from Carrera carrera left join fetch carrera.noTerminans where carrera.id =:id")
    Optional<Carrera> findOneWithEagerRelationships(@Param("id") Long id);

    List<Carrera> findByGanador_apellido(String apellido);
    int countByGanador_apellidoAndGanador_edad(String apellido, int edad);
    List<Carrera> findBySegundoPuesto_nombreAndSegundoPuesto_apellidoAndSegundoPuesto_coche(String nombre, String apellido, String coche);
    boolean existsByGanador_apellido(String apellido);
}
