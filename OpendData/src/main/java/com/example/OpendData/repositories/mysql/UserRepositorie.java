package com.example.OpendData.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.OpendData.models.mysql.User;

/**
 * Interface pour les utilisateurs
 * @author Kévin Pradier
 */
@Repository
public interface UserRepositorie extends JpaRepository<User, Integer> {
    
    /**
     * Trouver un utilisateur par son email
     * @param email l'email de l'utilisateur
     * @return l'utilisateur correspondant à l'email
     * @author Kévin Pradier
     */
    public User findByEmail(String email);
}
