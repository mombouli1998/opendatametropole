package com.example.OpendData.models.mysql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Classe `Theme` qui représente un thème de données.
 */
@Entity  // Annotation qui indique que cette classe est une entité JPA, mappée à une table de base de données.
public class Theme {
    @Id  // Annotation qui indique que le champ `id` est la clé primaire de cette entité.
    @GeneratedValue(strategy=GenerationType.IDENTITY)  // Spécifie que la valeur du champ `id` sera générée automatiquement par la base de données avec la stratégie d'auto-incrémentation `IDENTITY`.
    private int id;  // Champ `id` pour stocker l'identifiant unique de l'objet `Theme`.

    private String name;  // Champ pour stocker le nom du `Theme`.

    public Theme() {
    }

    /**
     * Constructeur de la classe `Theme`.
     * @param name Nom du thème.
     */
    public Theme(String name) {
        this.name = name;
    }

    /**
     * Setter pour modifier la valeur de l'identifiant `id`.
     * @param id Nouvelle valeur de l'identifiant.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter pour accéder à la valeur de l'identifiant `id`.
     * @return Valeur de l'identifiant.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter pour accéder à la valeur du champ `name`.
     * @return Valeur du champ `name`.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter pour modifier la valeur du champ `name`.
     * @param name Nouvelle valeur du champ `name`.
     */
    public void setName(String name) {
        this.name = name;
    }
}
