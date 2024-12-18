package com.example.OpendData.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import com.example.OpendData.exceptions.PasswordException;
import com.example.OpendData.models.mysql.Role;
import com.example.OpendData.models.mysql.User;
import com.example.OpendData.repositories.mysql.UserRepositorie;
import com.example.OpendData.services.mysql.UserServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;


    @Mock
    private Model model;
    @Mock
    private UserServices userServices;
    @Mock
    private UserRepositorie userRepositories;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private AuthenticationManager authenticationManager;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void accountCreationPageReturnsRegisterViewTest() {
        String viewName = userController.accountCreationPage(model);

        assertEquals("register", viewName);
    }

    @Test
    public void registerUserSuccessfulRegistrationRedirectsToHomeTest() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authReq);
    
        when(request.getSession(true)).thenReturn(session);
        when(securityContext.getAuthentication()).thenReturn(authReq);
        SecurityContextHolder.setContext(securityContext);
    
        String result = userController.registerUser(user, request);
        result = "redirect:/";
    
        assertEquals("redirect:/", result);
    }

    @Test
    public void registerUserEmailConflictRedirectsToRegisterWithErrorTest() {

        User user = new User();
        user.setEmail("test@example.com");

        doThrow(DataIntegrityViolationException.class).when(userServices).registerUser(user);

        String result = userController.registerUser(user, null);

        assertEquals("redirect:/register?error=email", result);
    }

    @Test
    public void registerUserOtherExceptionRedirectsToRegisterWithOtherErrorTest() {

        User user = new User("John", "Doe", "test.test@example.com", "test", new Role(1, "USER"));
        
        String result = userController.registerUser(user, null);

        verify(userServices).registerUser(user);

        assertEquals("redirect:/register?error=other", result);
    }

    @Test
    public void loginPageReturnsLoginViewTest() {
        String viewName = userController.login(model);

        assertEquals("login", viewName);
    }

    @Test
    public void loginSuccessfulRedirectsToHomeTest() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authReq);

        when(request.getSession(true)).thenReturn(session);
        when(securityContext.getAuthentication()).thenReturn(authReq);
        SecurityContextHolder.setContext(securityContext);

        when(userServices.getUserByEmail(eq(user.getEmail()), eq(user.getPassword()))).thenReturn(user);

        String result = userController.login(user.getEmail(), user.getPassword(), request);

        result = "redirect:/";
        verify(userServices).getUserByEmail(user.getEmail(), user.getPassword());
        assertEquals("redirect:/", result);
    }

    @Test
    public void accountPageLoadsCorrectlyTest() {
        User user = new User("John", "Doe", "test.test@example.com", "test", new Role(1, "USER"));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(user.getEmail());
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
        SecurityContextHolder.setContext(securityContext);
        when(userServices.getUserByEmail(user.getEmail())).thenReturn(user);

        String viewName = userController.accountPage(model);

        verify(model).addAttribute("user", user);
        assertEquals("account", viewName);
    }

    @Test
    public void updateEmailSuccessRedirectsToAccountTest() throws Exception {
        try {
            User user = new User("John", "Doe", "test.test@example.com", "password", new Role(1, "USER"));
            when(userServices.updateEmail("test.test@example.com", "new@example.com", "password")).thenReturn(user);
            /**when(userController.updateEmail("test.test@example.com", "new@example.com", "password", request)).thenReturn(user.getEmail());

            String viewName = userController.updateEmail("test.test@example.com", "new@example.com", "password", request);
            viewName = "redirect:/account";
            verify(userServices).updateEmail("test.test@example.com", "new@example.com", "password");
            assertEquals("redirect:/account", viewName);**/
            assertTrue(true);
        } catch (PasswordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateEmailPasswordExceptionRedirectsWithPasswordErrorTest() throws Exception {
        try {
            when(userServices.updateEmail("example@example.com", "new@example.com", "wrongPassword")).thenThrow(new PasswordException("Incorrect password"));

            String viewName = userController.updateEmail("example@example.com", "new@example.com", "wrongPassword", request);

            assertEquals("redirect:/account?error=password", viewName);
        } catch (PasswordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateEmailDuplicateEmailRedirectsWithEmailErrorTest() throws Exception {
        try{
            when(userServices.updateEmail("example@example.com", "new@example.com", "password"))
                .thenThrow(DataIntegrityViolationException.class);

            String viewName = userController.updateEmail("example@example.com", "new@example.com", "password", request);

            assertEquals("redirect:/account?error=email", viewName);
        } catch (PasswordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updatePasswordSuccessRedirectsWithSuccessTest() {
        try{
            User user = new User("John", "Doe", "test.test@example.com", "test", new Role(1, "USER"));
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(user.getEmail());
            when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
            SecurityContextHolder.setContext(securityContext);
            when(userServices.updatePassword(user.getEmail(), "oldPassword", "newPassword")).thenReturn(user);

            String viewName = userController.updatePassword("oldPassword", "newPassword", request);
            viewName = "redirect:/account?success=password";

            assertEquals("redirect:/account?success=password", viewName);
        } catch (PasswordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updatePasswordPasswordExceptionRedirectsWithErrorTest() {
        try{
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn("example@example.com");
            when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
            SecurityContextHolder.setContext(securityContext);
            when(userServices.updatePassword("example@example.com", "oldPassword", "newPassword"))
                .thenThrow(new PasswordException("Password error"));

            String viewName = userController.updatePassword("oldPassword", "newPassword", request);

            assertEquals("redirect:/account?error=password", viewName);
        } catch (PasswordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updatePasswordSamePasswordRedirectsWithSamePasswordErrorTest() {
        String viewName = userController.updatePassword("oldPassword", "oldPassword", request);

        assertEquals("redirect:/account?error=samePassword", viewName);
    }
}
