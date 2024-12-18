package com.example.OpendData.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.example.OpendData.models.mysql.Role;
import com.example.OpendData.models.mysql.User;
import com.example.OpendData.repositories.mysql.UserRepositorie;

@SpringBootTest
public class UserRepositorieIT {
    
    @Autowired
    private UserRepositorie userRepository;

    @Test
    public void testSaveUser() {
        User userToSave = new User("test", "integration", "test@example.com", "password123", new Role(1, "USER"));

        User savedUser = userRepository.save(userToSave);
        
        assertEquals(savedUser != null, true);
        assertEquals(savedUser.getEmail(), "test@example.com");
        assertEquals(savedUser.getRole().getType(), "USER");
        
    }
}
