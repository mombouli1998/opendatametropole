package com.example.OpendData.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import com.example.OpendData.models.mysql.Role;
import com.example.OpendData.models.mysql.User;

/**
 * Classe de tests pour la classe User
 */
public class UserTests {
    
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    public void testUserConstructorGettersAndSetters() {
        User user = new User("John", "Doe", "test.test@example.com", "test", new Role(1, "USER"));
        
        assertEquals("John", user.getFirst_name());
        user.setFirst_name("Jane");
        assertEquals("Jane", user.getFirst_name());

        assertEquals("Doe", user.getLast_name());
        user.setLast_name("Smith");
        assertEquals("Smith", user.getLast_name());

        assertEquals("test.test@example.com", user.getEmail());
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());

        assertEquals("test", user.getPassword());
        assertEquals(1, user.getRole().getId());
        assertEquals("USER", user.getRole().getType());
        assertEquals(0, user.getId());

        assertNotNull(user.getCreatedAt());
        assertEquals("test@example.com", user.getUsername());

        Role role = new Role(2, "ADMIN");
        user.setRole(role);
        assertEquals(2, user.getRole().getId());
        assertEquals("ADMIN", user.getRole().getType());
        assertEquals(role, user.getRole());

        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        assertEquals(hashPassword, user.getPassword());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.iterator().next().getAuthority()); 

    }
}
