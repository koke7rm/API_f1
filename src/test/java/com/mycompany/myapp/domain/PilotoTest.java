package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PilotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Piloto.class);
        Piloto piloto1 = new Piloto();
        piloto1.setId(1L);
        Piloto piloto2 = new Piloto();
        piloto2.setId(piloto1.getId());
        assertThat(piloto1).isEqualTo(piloto2);
        piloto2.setId(2L);
        assertThat(piloto1).isNotEqualTo(piloto2);
        piloto1.setId(null);
        assertThat(piloto1).isNotEqualTo(piloto2);
    }
}
