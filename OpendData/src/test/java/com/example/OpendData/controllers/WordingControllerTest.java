package com.example.OpendData.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.models.mysql.Wording;
import com.example.OpendData.services.mysql.ThemeService;
import com.example.OpendData.services.mysql.WordingService;
import org.springframework.http.ResponseEntity;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WordingControllerTest {

    @Mock
    private WordingService wordingService;

    @Mock
    private ThemeService themeService;

    @Mock
    private Model model;

    @InjectMocks
    private WordingController wordingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for GET /wording
    @Test
    public void testAddWording() {
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme("Démographie"));

        when(themeService.listeTheme()).thenReturn(themes);

        String viewName = wordingController.addwording(model);

        verify(model).addAttribute("S", themes);
        assertEquals("wording", viewName);
    }

    // Test for POST /wording
    @Test
    public void testAddWordingWithUniqueName() throws Exception {
        MockMultipartFile pdfFile = new MockMultipartFile("pdf", "test.pdf", "application/pdf", "dummy data".getBytes());
        MockMultipartFile xmlFile = new MockMultipartFile("xml", "test.xml", "application/xml", "<test>data</test>".getBytes());
        MockMultipartFile csvFile = new MockMultipartFile("csv", "test.csv", "text/csv", "col1;col2\nval1;val2".getBytes());

        when(wordingService.getWording("UniqueName")).thenReturn(null);
        when(themeService.getNames("Démographie")).thenReturn(new Theme("Démographie"));

        String result = wordingController.add(
                "UniqueName", "Paris", LocalDate.now(), "License", "Description",
                "Démographie", pdfFile, xmlFile, csvFile, "Français", model);

        verify(wordingService, times(1)).add(any(Wording.class));
        assertEquals("redirect:/", result);
    }

    // Test for POST /wording with duplicate name
    @Test
    public void testAddWordingWithDuplicateName() {
        when(wordingService.getWording("DuplicateName")).thenReturn(new Wording());

MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

String result = wordingController.add(
        "DuplicateName", "Paris", LocalDate.now(), "License", "Description",
        "Démographie", emptyFile, emptyFile, emptyFile, "Français", model);

        assertEquals("redirect:/wording?error=libelle", result);
    }

    // Test for downloading CSV file
    @Test
    public void testDownloadCSV() {
        Wording wording = new Wording();
        wording.setCsv("col1;col2\nval1;val2".getBytes(StandardCharsets.UTF_8));

        when(wordingService.getWordingById(1)).thenReturn(wording);

        ResponseEntity<byte[]> response = wordingController.downloadCSV(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("col1;col2\nval1;val2", new String(response.getBody(), StandardCharsets.UTF_8));
    }

    // Test for downloading JSON file
    @Test
    public void testDownloadJSON() {
        Wording wording = new Wording();
        wording.setCsv("col1;col2\nval1;val2".getBytes(StandardCharsets.UTF_8));

        when(wordingService.getWordingById(1)).thenReturn(wording);

        ResponseEntity<byte[]> response = wordingController.downloadJSON(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test for downloading XML file when XML content is null
    @Test
    public void testDownloadXMLWhenXmlIsNull() throws Exception {
        Wording wording = new Wording();
        wording.setCsv("col1;col2\nval1;val2".getBytes(StandardCharsets.UTF_8));
        wording.setXml(null);

        when(wordingService.getWordingById(1)).thenReturn(wording);

        ResponseEntity<byte[]> response = wordingController.downloadXML(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
