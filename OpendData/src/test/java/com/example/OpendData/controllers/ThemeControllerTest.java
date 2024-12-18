package com.example.OpendData.controllers;

import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.services.mysql.ThemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ThemeControllerTest {

    @Mock
    private ThemeService themeService;

    @Mock
    private Model model;

    @InjectMocks
    private ThemeController themeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTheme_WhenThemeAlreadyExists() {
        // Arrange: Simuler qu'un thème existe déjà avec le nom fourni
        Theme existingTheme = new Theme("DuplicateTheme");
        existingTheme.setId(1);  // Indiquer un ID non nul pour représenter un thème existant

        when(themeService.getNames("DuplicateTheme")).thenReturn(existingTheme);

        // Act: Appeler la méthode `addtheme` avec un nom de thème déjà existant
        String viewName = themeController.addtheme("DuplicateTheme", model);

        // Assert: Vérifier que la vue est "add" et que le message d'erreur est bien ajouté au modèle
        assertEquals("add", viewName);
        verify(model).addAttribute(eq("erreur"), eq("Le thème est déjà présent"));
        verify(themeService, never()).add(any(Theme.class));
    }

    @Test
    void testAddTheme_WhenThemeDoesNotExist() {
        // Simuler l'absence d'un thème avec le nom donné
        when(themeService.getNames("NewTheme")).thenReturn(new Theme()); // Retourne un Theme avec un id = 0

        // Simuler l'ajout du thème et retourner un nouveau thème
        Theme newTheme = new Theme("NewTheme");
        when(themeService.add(any(Theme.class))).thenReturn(newTheme);

        // Appeler la méthode et vérifier le résultat
        String viewName = themeController.addtheme("NewTheme", model);

        // Vérifier que le contrôleur retourne la vue correcte
        assertEquals("redirect:/", viewName);
    }

    @Test
    void testAddView_ShouldReturnAddPage() {
        // Act: Appeler la méthode `add` qui retourne le nom de la vue de l'ajout
        String viewName = themeController.add(model);

        // Assert: Vérifier que la vue est bien "add"
        assertEquals("add", viewName);
    }
}
