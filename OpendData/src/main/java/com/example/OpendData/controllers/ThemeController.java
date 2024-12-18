package com.example.OpendData.controllers;
//@tm430209
import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.services.mysql.ThemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Déclaration de la classe `ThemeController`, qui traite les requêtes liées aux objets `Theme`.
 */
@Controller  // Annotation qui indique que cette classe est un contrôleur Spring, gérant les requêtes HTTP et les interactions avec les vues.
public class ThemeController {

    private ThemeService themeService;

    /**
     * Constructeur qui permet l'injection de dépendance de `ThemeService` dans cette classe de contrôleur.
     * @param themeService Service qui gère les opérations sur les objets `Theme`.
     */
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    /**
     * Méthode qui retourne la vue pour l'ajout d'un sujet.
     * @param model Modèle qui contient les données à afficher dans la vue.
     * @return Retourne le nom de la vue "add" pour afficher le formulaire d'ajout d'un sujet.
     */
    @GetMapping("/add")  // Annotation qui gère les requêtes HTTP GET à l'URL "/add".
    public String add(Model model) {
        return "add"; 
    }

    /**
     * Méthode qui traite l'ajout d'un sujet avec le nom passé en paramètre.
     * @param name Nom du sujet à ajouter.
     * @param model Modèle qui contient les données à afficher dans la vue.
     * @return Retourne le nom de la vue ou l'instruction de redirection.
     */
    @PostMapping("/add") 
    public String addtheme(@RequestParam("name") String name, Model model) {

        String t = ""; 
        Theme theme = themeService.getNames(name);

        // Appelle la méthode `getName` du `ThemeService` pour vérifier si un sujet avec ce nom existe déjà.
        if (theme.getId()!= 0) {
            // Si un sujet avec le même nom existe déjà,
            model.addAttribute("erreur", "Le thème est déjà présent");
            // Ajoute un attribut d'erreur au modèle pour informer l'utilisateur que le sujet existe déjà.
            t = "add";  // Prépare le nom de la vue à retourner pour afficher le formulaire d'ajout à nouveau.
            System.out.println(theme);

        } else {
            // Si aucun sujet avec ce nom n'existe,
            Theme Theme1 = new Theme(name);
            // Crée un nouvel objet `Theme` avec le nom fourni.
            themeService.add(Theme1);
            System.out.println(Theme1.getId());
            // Appelle la méthode `add` du `ThemeService` pour ajouter le nouveau sujet à la base de données.
            t = "redirect:/";  // Prépare la redirection vers la page d'accueil (ou une autre page de votre choix).
        }
        return t; 
    }
}

