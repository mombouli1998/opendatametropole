package com.example.OpendData.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.OpendData.exceptions.PasswordException;
import com.example.OpendData.models.mysql.Role;
import com.example.OpendData.models.mysql.User;
import com.example.OpendData.services.mysql.UserServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



/**
 * Contrôleur pour les utilisateurs
 * @author Anthony Michaud
 */
@Controller
public class UserController {

    private UserServices userServices;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    /**
     * Constructeur du contrôleur
     * @param userServices
     */
    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    /**
     * Page de création d'un compte utilisateur
     * @param model
     * @return register.html
     * @author Kévin Pradier
     */
    @GetMapping("/register")
    public String accountCreationPage(Model model) {
        return "register";
    }

    /**
     * Création d'un compte utilisateur
     * @param user l'utilisateur à créer
     * @param req  la requête HTTP
     * @return redirection vers la page d'accueil si l'inscription est réussie, sinon redirection vers la page de création de compte avec un message d'erreur
     * @throws DataIntegrityViolationException si l'adresse email est déjà utilisée
     * @throws Exception si une autre erreur survient
     * @author Kévin Pradier
     */
    @PostMapping("/register")
    public String registerUser(User user, HttpServletRequest req) {

        Role role = new Role(2,"USER");
        user.setRole(role);
        try{
            userServices.registerUser(user); 
            connectUser(user, req);
        } 
        catch (DataIntegrityViolationException e) {
            return "redirect:/register?error=email";
        }
        catch (Exception e) {
            return "redirect:/register?error=other";
        }

        return "redirect:/";
    }

    /**
     * Page de gestion du compte utilisateur
     * @param model le modèle
     * @return account.html
     */
    @GetMapping("/account")
    public String accountPage(Model model) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userServices.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "account";
    }


    /**
     * Méthode pour mettre à jour l'adresse mail d'un utilisateur
     * @param currentEmail Adresse mail actuelle de l'utilisateur
     * @param newEmail Nouvelle adresse mail entrée par l'utilisateur
     * @param currentPassword Mot de passe actuel de l'utilisateur, pour valider le changement de mail
     * @param req requête HTTP
     * @return une page en fonction du résultat de la modification
     * @author Alan Guilleminot
     */
    @PostMapping("/account/update_email")
    public String updateEmail(@RequestParam String currentEmail,@RequestParam String newEmail,@RequestParam String currentPassword, HttpServletRequest req) {
        try{
            User user = userServices.updateEmail(currentEmail,newEmail,currentPassword);
            if(user != null){
                user.setPassword(currentPassword);
                connectUser(user, req);
            }
        }
        catch (PasswordException e) {
            return ("redirect:/account?error=password");
        }
        catch (DataIntegrityViolationException e) {
            return ("redirect:/account?error=email");
        }
        catch (Exception e) {
            return ("redirect:/account?error=other");
        }
        return "redirect:/account?success=email";
    }

    /**
     * Méthode pour mettre à jour le mot de passe d'un utilisateur
     * @param oldPassword Mot de passe actuel de l'utilisateur
     * @param newPassword Nouveau mot de passe
     * @param req
     * @return la page en fonction du résultat de la modification
     */
    @PostMapping("/account/update_password")
    public String updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpServletRequest req) {
        try {
            if (oldPassword.equals(newPassword)) {
                return "redirect:/account?error=samePassword"; // Redirection avec erreur
            }

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = userDetails.getUsername();
            
            User user = userServices.updatePassword(email, oldPassword, newPassword);

            if (user != null) {
                user.setPassword(newPassword);
                connectUser(user, req);
                return "redirect:/account?success=password";  
            } else {
                return "redirect:/account?error=password";  
            }
        } catch (PasswordException e) {
            return "redirect:/account?error=password";  
        } catch (Exception e) {
            return "redirect:/account?error=other";  
        }
    }

    /**
     * Affiche la page de connexion.
     *
     * Cette méthode est appelée lorsque l'utilisateur accède à l'URL "/login" via une requête HTTP GET.
     * Elle renvoie le nom de la vue qui affiche le formulaire de connexion.
     *
     * @param model L'objet {@link Model} qui permet d'ajouter des attributs à la vue si nécessaire.
     * @return Le nom de la vue "login", qui correspond à la page de connexion.
     */
    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    /**
     * Gère la soumission du formulaire de connexion.
     *
     * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire de connexion à l'URL "/login" via une requête HTTP POST.
     * Elle vérifie les informations d'identification fournies et, si elles sont valides, authentifie l'utilisateur et le redirige vers la page d'accueil.
     * En cas d'erreur, elle redirige l'utilisateur vers la page de connexion avec un message d'erreur.
     *
     * @param email L'adresse e-mail de l'utilisateur, extraite du formulaire de connexion.
     * @param password Le mot de passe de l'utilisateur, extraite du formulaire de connexion.
     * @param req L'objet {@link HttpServletRequest} permettant d'accéder à la session HTTP de l'utilisateur.
     * @return Une redirection vers la page d'accueil si l'authentification est réussie, ou vers la page de connexion avec un paramètre d'erreur si l'authentification échoue.
    */
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest req) {
        try{
            User user = userServices.getUserByEmail(email, password);
            if(user == null) {
                return "redirect:/login?error=invalid";
            }
            user.setPassword(password);
            connectUser(user, req);
            return "redirect:/";
        }
        catch (Exception e) {
            return "redirect:/login?error=invalid";
        }
    }

    /**
     * Connecte un utilisateur à l'aide de spring security
     * @param user l'utilisateur à connecter
     * @param req la requête HTTP
     * @author Anthony Michaud
     */
    private void connectUser(User user, HttpServletRequest req) {

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
            user.getEmail(),
            user.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setMaxInactiveInterval(300);
        session.setAttribute("SPRING_SECURITY_CONTEXT", sc);

        auth.getAuthorities().forEach(authority -> {
            System.out.println(authority.getAuthority());
        });
    }
    

    
}
