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
 * A Piloto.
 */
@Entity
@Table(name = "piloto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Piloto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Pattern(regexp = "^[a-zA-Z ]*$")
    @Column(name = "apellido", unique = true)
    private String apellido;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "coche")
    private String coche;

    @OneToMany(mappedBy = "ganador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ganador", "segundoPuesto", "tercerPuesto", "noTerminans" }, allowSetters = true)
    private Set<Carrera> carrerasGanadas = new HashSet<>();

    @OneToMany(mappedBy = "segundoPuesto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ganador", "segundoPuesto", "tercerPuesto", "noTerminans" }, allowSetters = true)
    private Set<Carrera> segundosPuestos = new HashSet<>();

    @OneToMany(mappedBy = "tercerPuesto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ganador", "segundoPuesto", "tercerPuesto", "noTerminans" }, allowSetters = true)
    private Set<Carrera> tercerPuestos = new HashSet<>();

    @ManyToMany(mappedBy = "noTerminans")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ganador", "segundoPuesto", "tercerPuesto", "noTerminans" }, allowSetters = true)
    private Set<Carrera> carrerasInacabadas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Piloto id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Piloto nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Piloto apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getEdad() {
        return this.edad;
    }

    public Piloto edad(Integer edad) {
        this.edad = edad;
        return this;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCoche() {
        return this.coche;
    }

    public Piloto coche(String coche) {
        this.coche = coche;
        return this;
    }

    public void setCoche(String coche) {
        this.coche = coche;
    }

    public Set<Carrera> getCarrerasGanadas() {
        return this.carrerasGanadas;
    }

    public Piloto carrerasGanadas(Set<Carrera> carreras) {
        this.setCarrerasGanadas(carreras);
        return this;
    }

    public Piloto addCarrerasGanadas(Carrera carrera) {
        this.carrerasGanadas.add(carrera);
        carrera.setGanador(this);
        return this;
    }

    public Piloto removeCarrerasGanadas(Carrera carrera) {
        this.carrerasGanadas.remove(carrera);
        carrera.setGanador(null);
        return this;
    }

    public void setCarrerasGanadas(Set<Carrera> carreras) {
        if (this.carrerasGanadas != null) {
            this.carrerasGanadas.forEach(i -> i.setGanador(null));
        }
        if (carreras != null) {
            carreras.forEach(i -> i.setGanador(this));
        }
        this.carrerasGanadas = carreras;
    }

    public Set<Carrera> getSegundosPuestos() {
        return this.segundosPuestos;
    }

    public Piloto segundosPuestos(Set<Carrera> carreras) {
        this.setSegundosPuestos(carreras);
        return this;
    }

    public Piloto addSegundosPuestos(Carrera carrera) {
        this.segundosPuestos.add(carrera);
        carrera.setSegundoPuesto(this);
        return this;
    }

    public Piloto removeSegundosPuestos(Carrera carrera) {
        this.segundosPuestos.remove(carrera);
        carrera.setSegundoPuesto(null);
        return this;
    }

    public void setSegundosPuestos(Set<Carrera> carreras) {
        if (this.segundosPuestos != null) {
            this.segundosPuestos.forEach(i -> i.setSegundoPuesto(null));
        }
        if (carreras != null) {
            carreras.forEach(i -> i.setSegundoPuesto(this));
        }
        this.segundosPuestos = carreras;
    }

    public Set<Carrera> getTercerPuestos() {
        return this.tercerPuestos;
    }

    public Piloto tercerPuestos(Set<Carrera> carreras) {
        this.setTercerPuestos(carreras);
        return this;
    }

    public Piloto addTercerPuestos(Carrera carrera) {
        this.tercerPuestos.add(carrera);
        carrera.setTercerPuesto(this);
        return this;
    }

    public Piloto removeTercerPuestos(Carrera carrera) {
        this.tercerPuestos.remove(carrera);
        carrera.setTercerPuesto(null);
        return this;
    }

    public void setTercerPuestos(Set<Carrera> carreras) {
        if (this.tercerPuestos != null) {
            this.tercerPuestos.forEach(i -> i.setTercerPuesto(null));
        }
        if (carreras != null) {
            carreras.forEach(i -> i.setTercerPuesto(this));
        }
        this.tercerPuestos = carreras;
    }

    public Set<Carrera> getCarrerasInacabadas() {
        return this.carrerasInacabadas;
    }

    public Piloto carrerasInacabadas(Set<Carrera> carreras) {
        this.setCarrerasInacabadas(carreras);
        return this;
    }

    public Piloto addCarrerasInacabadas(Carrera carrera) {
        this.carrerasInacabadas.add(carrera);
        carrera.getNoTerminans().add(this);
        return this;
    }

    public Piloto removeCarrerasInacabadas(Carrera carrera) {
        this.carrerasInacabadas.remove(carrera);
        carrera.getNoTerminans().remove(this);
        return this;
    }

    public void setCarrerasInacabadas(Set<Carrera> carreras) {
        if (this.carrerasInacabadas != null) {
            this.carrerasInacabadas.forEach(i -> i.removeNoTerminan(this));
        }
        if (carreras != null) {
            carreras.forEach(i -> i.addNoTerminan(this));
        }
        this.carrerasInacabadas = carreras;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piloto)) {
            return false;
        }
        return id != null && id.equals(((Piloto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Piloto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", edad=" + getEdad() +
            ", coche='" + getCoche() + "'" +
            "}";
    }
}
