package com.example.OpendData.models;

import org.junit.jupiter.api.Test;

import com.example.OpendData.models.mysql.Theme;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeTest {

    @Test
    void testThemeDefaultConstructor() {
        Theme theme = new Theme();

        assertThat(theme.getId()).isEqualTo(0); // Par défaut pour un champ int
        assertThat(theme.getName()).isNull();
    }

    @Test
    void testThemeParameterizedConstructor() {
        Theme theme = new Theme("Nature");

        assertThat(theme.getName()).isEqualTo("Nature");
        assertThat(theme.getId()).isEqualTo(0); // Par défaut, l'identifiant reste 0 tant qu'il n'est pas défini
    }

    @Test
    void testSettersAndGetters() {
        Theme theme = new Theme();

        theme.setId(1);
        theme.setName("Technology");

        assertThat(theme.getId()).isEqualTo(1);
        assertThat(theme.getName()).isEqualTo("Technology");
    }
}