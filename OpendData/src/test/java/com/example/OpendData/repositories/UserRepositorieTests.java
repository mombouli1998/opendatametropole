package com.example.OpendData.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.OpendData.models.mysql.Role;
import com.example.OpendData.models.mysql.User;
import com.example.OpendData.repositories.mysql.UserRepositorie;

public class UserRepositorieTests {
    
    @Mock
    private UserRepositorie userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test de l'enregistrement d'un utilisateur
     */
    @Test
    public void testSaveUser() {
        User userSave = new User("John", "Doe", "test@example.com", passwordEncoder.encode("test"), new Role(1, "USER"));
        when(userRepository.save(userSave)).thenReturn(userSave);

        User savedUser = userRepository.save(userSave);

        assertEquals("test@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(savedUser);
    }
}
