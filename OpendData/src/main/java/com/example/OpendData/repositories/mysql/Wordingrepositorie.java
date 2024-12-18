package com.example.OpendData.repositories.mysql;

import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.models.mysql.Wording;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  Déclaration de l'interface `Wordingrepositorie` qui étend l'interface `JpaRepository`.
 *  `JpaRepository` fournit des méthodes CRUD (Create, Read, Update, Delete) prêtes à l'emploi pour manipuler les entités.
 *   Cette interface gère les entités de type `Wording` avec un identifiant de type `Integer`.
*/
public interface Wordingrepositorie extends JpaRepository<Wording, Integer> {

    /**
     * Méthode personnalisée pour rechercher un objet `Wording` par son nom.
     * @param nom le nom du `Wording`
     * @return le `Wording` correspondant au nom
     */
    Wording findByName(String nom);

    /**
     * Méthode personnalisée pour rechercher un objet `Wording` par son thème.
     * @param theme le thème du `Wording`
     * @return le `Wording` correspondant au thème
     */
    Wording findByTheme(Theme theme);
}
