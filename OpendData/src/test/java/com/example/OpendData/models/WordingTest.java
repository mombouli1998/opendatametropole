package com.example.OpendData.models;

import org.junit.jupiter.api.Test;
import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.models.mysql.Wording;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class WordingTest {

    @Test
    void testWordingDefaultConstructor() {
        Wording wording = new Wording();

        assertThat(wording.getId()).isEqualTo(0); // Par défaut pour les int
        assertThat(wording.getName()).isNull();
        assertThat(wording.getDescriptions()).isNull();
        assertThat(wording.getTheme()).isNull();
        assertThat(wording.getPdf()).isNull();
        assertThat(wording.getCsv()).isNull();
        assertThat(wording.getXml()).isNull();
        assertThat(wording.getLangue()).isNull();
        assertThat(wording.getTerritory()).isNull();
        assertThat(wording.getDownload()).isEqualTo(0);
        assertThat(wording.getLicense()).isNull();
        assertThat(wording.getModification()).isNull();
    }

    @Test
    void testWordingParameterizedConstructor() {
        Theme theme = new Theme();
        LocalDateTime now = LocalDateTime.now();
        byte[] pdfData = new byte[]{1, 2, 3};
        byte[] csvData = new byte[]{4, 5, 6};
        byte[] xmlData = new byte[]{7, 8, 9};

        Wording wording = new Wording(
                "Test Name",
                10,
                "GPL",
                now,
                theme,
                "Description",
                pdfData,
                csvData,
                xmlData,
                "FR",
                "Europe"
        );

        assertThat(wording.getName()).isEqualTo("Test Name");
        assertThat(wording.getDownload()).isEqualTo(10);
        assertThat(wording.getLicense()).isEqualTo("GPL");
        assertThat(wording.getModification()).isEqualTo(now);
        assertThat(wording.getTheme()).isSameAs(theme);
        assertThat(wording.getDescriptions()).isEqualTo("Description");
        assertThat(wording.getPdf()).isEqualTo(pdfData);
        assertThat(wording.getCsv()).isEqualTo(csvData);
        assertThat(wording.getXml()).isEqualTo(xmlData);
        assertThat(wording.getLangue()).isEqualTo("FR");
        assertThat(wording.getTerritory()).isEqualTo("Europe");
    }

    @Test
    void testSettersAndGetters() {
        Wording wording = new Wording();
        Theme theme = new Theme();
        LocalDateTime now = LocalDateTime.now();

        wording.setName("Updated Name");
        wording.setDownload(5);
        wording.setLicense("MIT");
        wording.setModification(now);
        wording.setTheme(theme);
        wording.setDescriptions("Updated Description");
        wording.setPdf(new byte[]{10, 11, 12});
        wording.setCsv(new byte[]{13, 14, 15});
        wording.setXml(new byte[]{16, 17, 18});
        wording.setLangue("EN");
        wording.setTerritory("USA");

        assertThat(wording.getName()).isEqualTo("Updated Name");
        assertThat(wording.getDownload()).isEqualTo(5);
        assertThat(wording.getLicense()).isEqualTo("MIT");
        assertThat(wording.getModification()).isEqualTo(now);
        assertThat(wording.getTheme()).isSameAs(theme);
        assertThat(wording.getDescriptions()).isEqualTo("Updated Description");
        assertThat(wording.getPdf()).isEqualTo(new byte[]{10, 11, 12});
        assertThat(wording.getCsv()).isEqualTo(new byte[]{13, 14, 15});
        assertThat(wording.getXml()).isEqualTo(new byte[]{16, 17, 18});
        assertThat(wording.getLangue()).isEqualTo("EN");
        assertThat(wording.getTerritory()).isEqualTo("USA");
    }
}

