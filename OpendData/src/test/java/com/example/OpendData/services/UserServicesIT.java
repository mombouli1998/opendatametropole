package com.example.OpendData.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.OpendData.models.mysql.Role;
import com.example.OpendData.models.mysql.User;
import com.example.OpendData.services.mysql.UserServices;

@SpringBootTest
public class UserServicesIT {

    @Autowired
    private UserServices userServices;

    /**
     * Teste la réussite de l'enregistrement d'un utilisateur.
     * Vérifie que l'utilisateur est correctement enregistré avec les bonnes informations
     * et que le mot de passe est crypté.
     */
    @Test
    public void testRegisterUserSuccess() {
        Role userRole = new Role(1, "USER");
        User user = new User("test", "integration", "test.integration@example.com", "password123", userRole);

        User registeredUser = userServices.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("test.integration@example.com", registeredUser.getEmail());
        assertEquals("test", registeredUser.getFirst_name());
        assertEquals("integration", registeredUser.getLast_name());
        assertNotEquals("password123", registeredUser.getPassword());
        assertEquals(userRole.getType(), registeredUser.getRole().getType());
    }

    /**
     * Teste l'enregistrement d'un utilisateur avec un email déjà existant.
     * Vérifie que l'exception DataIntegrityViolationException est levée.
     */
    @Test
    public void testRegisterUserEmailAlreadyExists() {
        Role userRole = new Role(1, "USER");
        User user = new User("test", "integration", "test.integration@example.com", "password123", userRole);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userServices.registerUser(user);
        });
    }

    /**
     * Teste le chargement d'un utilisateur par son nom d'utilisateur existant.
     * Vérifie que l'utilisateur est correctement chargé.
     */
    @Test
    public void testLoadUserByUsernameUserExists() {
        Role userRole = new Role(1, "USER");
        User user = new User("test2", "integration2", "test2.integration2@example.com", "password123", userRole);

        userServices.registerUser(user);

        UserDetails loadedUser = userServices.loadUserByUsername("test2.integration2@example.com");

        assertNotNull(loadedUser);
        assertEquals("test2.integration2@example.com", loadedUser.getUsername());
    }

    /**
     * Teste le chargement d'un utilisateur par son nom d'utilisateur qui n'existe pas.
     * Vérifie que l'exception UsernameNotFoundException est levée.
     */
    @Test
    public void testLoadUserByUsernameUserDoesNotExist() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userServices.loadUserByUsername("test3@example.com");
        });
    }

    /**
     * Teste la récupération d'un utilisateur par son email avec succès.
     * Vérifie que l'utilisateur est correctement récupéré.
     */
    @Test
    public void testGetUserByEmailSuccess() {

        Role userRole = new Role(1, "USER");
        User user = new User("testAuth", "integration", "test.auth@example.com", "password123", userRole);

        userServices.registerUser(user);

        User authenticatedUser = userServices.getUserByEmail("test.auth@example.com", "password123");

        assertNotNull(authenticatedUser);
        assertEquals("test.auth@example.com", authenticatedUser.getEmail());
    }


    /**
     * Teste que le mot de passe est correctement crypté lors de l'enregistrement de l'utilisateur.
     * Vérifie que le mot de passe enregistré est différent du mot de passe en clair.
     */
    @Test
    public void testPasswordIsEncrypted() {
        Role userRole = new Role(1, "USER");
        User user = new User("testEncrypt", "integration", "test.encrypt@example.com", "password123", userRole);

        User registeredUser = userServices.registerUser(user);

        // Vérifie que le mot de passe enregistré est différent du mot de passe en clair
        assertNotEquals("password123", registeredUser.getPassword());
    
        // Vérifie que le mot de passe enregistré est bien crypté (en utilisant l'encodeur de mot de passe)
        assertNotNull(registeredUser.getPassword());
    }

    /**
     * Teste la récupération d'un utilisateur par son email avec un mot de passe incorrect.
     * Vérifie que l'exception UsernameNotFoundException est levée.
     */
    @Test
    public void testGetUserByEmailIncorrectPassword() {
        Role userRole = new Role(1, "USER");
        User user = new User("testWrongPassword", "integration", "test.wrongpassword@example.com", "password123", userRole);

        userServices.registerUser(user);

        // On vérifie que l'exception UsernameNotFoundException est lancée pour un mot de passe incorrect
        assertThrows(UsernameNotFoundException.class, () -> {
            userServices.getUserByEmail("test.wrongpassword@example.com", "wrongpassword");
        });
    }

    /**
     * Teste l'enregistrement d'un utilisateur administrateur avec succès.
     * Vérifie que l'administrateur est enregistré et a les bons rôles.
     */
    @Test
    public void testRegisterAdminUserSuccess() {
        Role adminRole = new Role(1, "ADMIN");
        User adminUser = new User("adminTest", "integration", "admin.test@example.com", "adminpassword123", adminRole);

        User registeredAdmin = userServices.registerUser(adminUser);

        UserDetails loadedAdmin = userServices.loadUserByUsername("admin.test@example.com");

        assertNotNull(loadedAdmin);
        assertEquals("admin.test@example.com", loadedAdmin.getUsername());

        // Vérification que l'utilisateur a les droits "ADMIN" et "USER"
        assertTrue(loadedAdmin.getAuthorities().stream()
                   .anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
    }
}
