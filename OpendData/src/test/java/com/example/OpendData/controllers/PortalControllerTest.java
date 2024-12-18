package com.example.OpendData.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class PortalControllerTest {
       @InjectMocks
    private PortalController portalController;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void homePageTest() {
        String viewName = portalController.homePage(model, null);
        verify(model).addAttribute("title", "Portail Open Data Métropolitain");

        assertEquals("index", viewName);
    }
}
