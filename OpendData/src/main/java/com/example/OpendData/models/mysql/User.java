package com.example.OpendData.models.mysql;

import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Classe représentant un utilisateur dans la base de données MySQL
 * @author Anthony Michaud
 */
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User() {
    }

    /**
     * Constructeur de la classe User
     * @param first_name le prénom de l'utilisateur
     * @param last_name le nom de l'utilisateur
     * @param email l'email de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @param role le role de l'utilisateur
     */
    public User(String first_name, String last_name, String email, String password, Role role) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Getter de l'id de l'utilisateur
     * @return l'id de l'utilisateur
     */
    public int getId() {
        return id;
    }

    /**
     * Getter du prénom de l'utilisateur
     * @return le prénom de l'utilisateur
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Setter du prénom de l'utilisateur
     * @param first_name le prénom de l'utilisateur
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Getter du nom de l'utilisateur
     * @return le nom de l'utilisateur
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Setter du nom de l'utilisateur
     * @param last_name le nom de l'utilisateur
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Getter de l'email de l'utilisateur
     * @return l'email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter de l'email de l'utilisateur
     * @param email l'email de l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter du mot de passe de l'utilisateur
     * @return le mot de passe de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter du mot de passe de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter de la date de création de l'utilisateur
     * @return la date de création de l'utilisateur
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter du role de l'utilisateur
     * @return le role de l'utilisateur
     */
    public Role getRole() {
        return role;
    }

    /**
     * Setter du role de l'utilisateur
     * @param role le role de l'utilisateur
     */
    public void setRole(Role role) {
        this.role = role;
    }

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         return java.util.Collections.singletonList(new SimpleGrantedAuthority(role.getType()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    
}
