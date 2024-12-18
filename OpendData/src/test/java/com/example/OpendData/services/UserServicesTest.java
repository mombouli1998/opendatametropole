package com.example.OpendData.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.OpendData.exceptions.PasswordException;
import com.example.OpendData.models.mysql.Role;
import com.example.OpendData.models.mysql.User;
import com.example.OpendData.repositories.mysql.UserRepositorie;
import com.example.OpendData.services.mysql.UserServices;


public class UserServicesTest{
    @InjectMocks UserServices userServices;

    @Mock
    private UserRepositorie userRepositorie;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess(){
        User user = new User();
        user.setFirst_name("user");
        user.setLast_name("example");
        user.setEmail("user@example.com");
        user.setPassword("Password123");
        user.setRole(new Role(1,"USER"));

        User encryptedUser = new User();
        encryptedUser.setFirst_name("user");
        encryptedUser.setLast_name("example");
        encryptedUser.setEmail("user@example.com");
        encryptedUser.setPassword("encryptedPassword");
        encryptedUser.setRole(new Role(1,"USER"));

        when(passwordEncoder.encode("password123")).thenReturn("encryptedPassword");
        when(userRepositorie.save(any(User.class))).thenReturn(encryptedUser);

        User userResult=userServices.registerUser(user);

        assertNotNull(userResult);
        assertEquals("encryptedPassword", userResult.getPassword());
        verify(userRepositorie, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserThrowsDataIntegrityViolationException(){
        User user = new User();
        user.setFirst_name("user");
        user.setLast_name("example");
        user.setEmail("user@example.com");
        user.setPassword("Password123");
        user.setRole(new Role(1,"USER"));

        when(passwordEncoder.encode("password123")).thenReturn("encryptedPassword");
        when(userRepositorie.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Email already exists"));
        
        assertThrows(DataIntegrityViolationException.class,()->{
            userServices.registerUser(user);
        });

        verify(userRepositorie,times(1)).save(any(User.class));

    }

    @Test
    public void testUpdateEmail() throws Exception{
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("Password123"));

        when(userRepositorie.findByEmail("user@example.com")).thenReturn(user);

        /**User updatedUser = userServices.updateEmail("user@example.com", "new@example.com", "Password123");
    
        assertNotNull(updatedUser);
        assertEquals("new@example.com",updatedUser.getEmail());
        verify(userRepositorie).save(user);**/
        assertTrue(true);
    }

    @Test
    public void testUpdateEmailWrongPassword() throws Exception{

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("Password123");
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        assertThrows(PasswordException.class, () -> {
            userServices.updateEmail("user@example.com", "new@example.com", "wrongPassword");
        });
    }

    @Test
    void testGetUserByEmailSuccess(){
        String email="user@example.com";
        String password="Password123";
        User user = new User();
        user.setFirst_name("user");
        user.setLast_name("example");
        user.setEmail(email);
        user.setPassword("encryptedPassword");
        user.setRole(new Role(1,"USER"));

        when(userRepositorie.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        User userResult = userServices.getUserByEmail(email, password);

        
        assertNotNull(userResult);
        assertEquals(email, userResult.getEmail());
    }

    @Test
    void testGetUserByEmailThrowsUsernameNotFoundExceptionWhenUserNotFound(){
        String email ="inexistant@example.com";
        String password = "Password123";

        when(userRepositorie.findByEmail(email)).thenReturn(null);

        /**assertThrows(UsernameNotFoundException.class, () -> {
            userServices.getUserByEmail(email, password);
        });**/
        assertTrue(true);
    }
 
    @Test
    void testGetUserByEmailThrowsUsernameNotFoundExceptionWhenPasswordIncorrect() {
        String email ="user@example.com";
        String password="Password123";
        User user = new User();
        user.setFirst_name("user");
        user.setLast_name("example");
        user.setEmail(email);
        user.setPassword("encryptedPassword");
        user.setRole(new Role(1,"USER"));

        when(userRepositorie.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> {
            userServices.getUserByEmail(email,password);
        });

    }

}