package com.example.OpendData.repositories.mysql;

import com.example.OpendData.models.mysql.Theme;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Déclaration de l'interface `Themerepositorie` qui étend l'interface `JpaRepository`.
 */
public interface Themerepositorie extends JpaRepository<Theme, Integer> {

}
