package com.example.OpendData.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 * Contrôleur pour le portail
 * @author Anthony Michaud
 */
@Controller
public class PortalController {

    /**
     * Page d'accueil
     * @param model
     * @return index.html
     */
    @GetMapping("/")
    public String homePage(Model model, Authentication authentication) {
        model.addAttribute("title", "Portail Open Data Métropolitain");
        return "index";
    }
}
