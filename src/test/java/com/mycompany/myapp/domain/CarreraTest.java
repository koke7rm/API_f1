package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarreraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carrera.class);
        Carrera carrera1 = new Carrera();
        carrera1.setId(1L);
        Carrera carrera2 = new Carrera();
        carrera2.setId(carrera1.getId());
        assertThat(carrera1).isEqualTo(carrera2);
        carrera2.setId(2L);
        assertThat(carrera1).isNotEqualTo(carrera2);
        carrera1.setId(null);
        assertThat(carrera1).isNotEqualTo(carrera2);
    }
}
