package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Carrera.
 */
@Entity
@Table(name = "carrera")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Carrera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_circuito", unique = true)
    private String nombreCircuito;

    @Column(name = "pais")
    private String pais;

    @ManyToOne
    @JsonIgnoreProperties(value = { "carrerasGanadas", "segundosPuestos", "tercerPuestos", "carrerasInacabadas" }, allowSetters = true)
    private Piloto ganador;

    @ManyToOne
    @JsonIgnoreProperties(value = { "carrerasGanadas", "segundosPuestos", "tercerPuestos", "carrerasInacabadas" }, allowSetters = true)
    private Piloto segundoPuesto;

    @ManyToOne
    @JsonIgnoreProperties(value = { "carrerasGanadas", "segundosPuestos", "tercerPuestos", "carrerasInacabadas" }, allowSetters = true)
    private Piloto tercerPuesto;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_carrera__no_terminan",
        joinColumns = @JoinColumn(name = "carrera_id"),
        inverseJoinColumns = @JoinColumn(name = "no_terminan_id")
    )
    @JsonIgnoreProperties(value = { "carrerasGanadas", "segundosPuestos", "tercerPuestos", "carrerasInacabadas" }, allowSetters = true)
    private Set<Piloto> noTerminans = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrera id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombreCircuito() {
        return this.nombreCircuito;
    }

    public Carrera nombreCircuito(String nombreCircuito) {
        this.nombreCircuito = nombreCircuito;
        return this;
    }

    public void setNombreCircuito(String nombreCircuito) {
        this.nombreCircuito = nombreCircuito;
    }

    public String getPais() {
        return this.pais;
    }

    public Carrera pais(String pais) {
        this.pais = pais;
        return this;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Piloto getGanador() {
        return this.ganador;
    }

    public Carrera ganador(Piloto piloto) {
        this.setGanador(piloto);
        return this;
    }

    public void setGanador(Piloto piloto) {
        this.ganador = piloto;
    }

    public Piloto getSegundoPuesto() {
        return this.segundoPuesto;
    }

    public Carrera segundoPuesto(Piloto piloto) {
        this.setSegundoPuesto(piloto);
        return this;
    }

    public void setSegundoPuesto(Piloto piloto) {
        this.segundoPuesto = piloto;
    }

    public Piloto getTercerPuesto() {
        return this.tercerPuesto;
    }

    public Carrera tercerPuesto(Piloto piloto) {
        this.setTercerPuesto(piloto);
        return this;
    }

    public void setTercerPuesto(Piloto piloto) {
        this.tercerPuesto = piloto;
    }

    public Set<Piloto> getNoTerminans() {
        return this.noTerminans;
    }

    public Carrera noTerminans(Set<Piloto> pilotos) {
        this.setNoTerminans(pilotos);
        return this;
    }

    public Carrera addNoTerminan(Piloto piloto) {
        this.noTerminans.add(piloto);
        piloto.getCarrerasInacabadas().add(this);
        return this;
    }

    public Carrera removeNoTerminan(Piloto piloto) {
        this.noTerminans.remove(piloto);
        piloto.getCarrerasInacabadas().remove(this);
        return this;
    }

    public void setNoTerminans(Set<Piloto> pilotos) {
        this.noTerminans = pilotos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Carrera)) {
            return false;
        }
        return id != null && id.equals(((Carrera) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Carrera{" +
            "id=" + getId() +
            ", nombreCircuito='" + getNombreCircuito() + "'" +
            ", pais='" + getPais() + "'" +
            "}";
    }
}
