package com.example.OpendData.services.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.OpendData.exceptions.PasswordException;
import com.example.OpendData.models.mysql.User;
import com.example.OpendData.repositories.mysql.UserRepositorie;

/**
 * Classe de services pour les utilisateurs
 * @author Anthony
 */
@Service
public class UserServices implements UserDetailsService {

    @Autowired   
    private UserRepositorie userRepositorie;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private java.util.List<GrantedAuthority> granteds;
    private java.util.List<GrantedAuthority> adminAuthorities;

    /**
     * Constructeur de la classe UserServices
     */
    public UserServices() {
        this.granteds =  AuthorityUtils.createAuthorityList("USER");
        this.adminAuthorities = AuthorityUtils.createAuthorityList("ADMIN");
    }

    /**
     * Enregistre un utilisateur
     * @param user l'utilisateur à enregistrer
     * @return l'utilisateur enregistré
     * @throws DataIntegrityViolationException si l'email existe déjà
     */
    public User registerUser(User user) throws DataIntegrityViolationException {
        // Création de l'utilisateur avec le mot de passe crypté
        User user_create = new User(user.getFirst_name(), user.getLast_name(), user.getEmail(), passwordEncoder.encode(user.getPassword()), user.getRole());

        try {
            // Enregistrement de l'utilisateur dans la bdd MySQL avec le mot de passe crypté
            return userRepositorie.save(user_create);
        } 
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Email already exists", e);
        }
    } 
    

    /**
     * Charge un utilisateur par son adresse e-mail pour l'authentification.
     *
     * Cette méthode est utilisée par Spring Security pour récupérer les informations de l'utilisateur à partir de la base de données
     * en fonction de son e-mail. Si l'utilisateur existe, ses informations (e-mail, mot de passe, et rôles) sont retournées sous forme
     * d'un objet {@link UserDetails}. Si l'utilisateur n'existe pas, une exception est levée.
     *
     * @param email L'adresse e-mail de l'utilisateur à rechercher.
     * @return Un objet {@link UserDetails} contenant les informations de l'utilisateur (e-mail, mot de passe et rôles).
     * @throws UsernameNotFoundException Si l'utilisateur avec l'adresse e-mail spécifiée n'est pas trouvé.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userData = this.userRepositorie.findByEmail(email);

        if(userData == null){
            throw new UsernameNotFoundException("User does not exist");
        }

        java.util.List<GrantedAuthority> authorities = granteds;
        if(userData.getRole().getType().equals("ADMIN")){
            authorities = adminAuthorities;
        }

        return new org.springframework.security.core.userdetails.User(userData.getEmail(), userData.getPassword(), authorities);
    }

    /**
     * Récupère un utilisateur par son adresse e-mail.
     *
     * @param email L'adresse e-mail de l'utilisateur à rechercher.
     * @return L'objet {@link User} correspondant à l'adresse e-mail spécifiée.
     * @throws UsernameNotFoundException Si l'utilisateur avec l'adresse e-mail spécifiée n'est pas trouvé.
     * @author Anthony MICHAUD
     */
    public User getUserByEmail(String email) throws UsernameNotFoundException{
        try
        {
            return userRepositorie.findByEmail(email);
        }
        catch (Exception e)
        {
            throw new UsernameNotFoundException("User does not exist");
        }
    }

    /**
     * Méthode de mise à jour et d'enregistrement dans la base de données de la nouvelle adresse mail de l'utilisateur
     * @param currentEmail adresse email actuelle de l'utilisateur
     * @param newEmail ancienne adresse email de l'utilisateur
     * @param currentPassword mot de passe de l'utilisateur, pour valider le changement de l'adresse mail
     * @throws PasswordException Si le mot de passe entré par l'utilisateur n'est pas correct
     * @author Alan Guilleminot
     */
    public User updateEmail(String currentEmail,String newEmail, String currentPassword) throws Exception{

        try{
            User currentUser = userRepositorie.findByEmail(currentEmail);
            if(passwordEncoder.matches(currentPassword,currentUser.getPassword())){
                currentUser.setEmail(newEmail);
                userRepositorie.save(currentUser);
                return currentUser;
            }
            else{
                throw new PasswordException("Old password is incorrect");
            }

        }
        catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Email already exists", e);
        }
        catch (Exception e){
            throw new PasswordException("Old password is incorrect");
        }

    }   

    /**
     * Méthode de mise à jour et d'enregistrement dans la base de données du nouveau mot de passe de l'utilisateur
     * @param email adresse email de l'utilisateur
     * @param oldPassword ancien mot de passe de l'utilisateur
     * @param newPassword nouveau mot de passe de l'utilisateur
     * @throws PasswordException Si le mot de passe entré par l'utilisateur n'est pas correct
     * @return l'utilisateur avec le nouveau mot de passe
     * @author Imane CHOUKRI
     */
    public User updatePassword(String email, String oldPassword, String newPassword) throws PasswordException {
        User user = userRepositorie.findByEmail(email);
        
        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword)); // Encoder le nouveau mot de passe
            userRepositorie.save(user); // Sauvegarder les changements
            return user;
        } else {
            throw new PasswordException("Old password is incorrect");
        }
    }
    

    /**
     * Récupère un utilisateur par son email et vérifie si le mot de passe est correct.
     * @param email    L'email de l'utilisateur que l'on souhaite récupérer.
     * @param password Le mot de passe en clair qui sera comparé avec le mot de passe crypté stocké dans la base de données.
     * @return         L'objet {@link User} si l'email et le mot de passe sont corrects.
     * @throws UsernameNotFoundException Si l'email n'existe pas dans la base de données ou si le mot de passe est incorrect.
     */
    public User getUserByEmail(String email, String password) {
        try
        {
            User user = userRepositorie.findByEmail(email);
            if (passwordEncoder.matches(password, user.getPassword())) {
                    return user;
            }
            else {
                    throw new UsernameNotFoundException("Password is incorrect");   
            }
        }
        catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User does not exist");
        }
    } 
    
}
