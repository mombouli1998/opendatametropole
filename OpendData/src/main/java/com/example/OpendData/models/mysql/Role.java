package com.example.OpendData.models.mysql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Classe représentant un role d'un compte dans la base de données MySQL
 * @author Anthony Michaud
 */
@Entity
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;

    public Role() {
    }

    /**
     * Constructeur d'un role
     * @param id l'id du role
     * @param type le type du role
     */
    public Role(int id,String type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Setter de l'id du role
     * @param id l'id du role
     */
    public void setId(int id){
        this.id=id;
    }

    /**
     * Setter du type du role
     * @param type le type du role
     */
    public void setType(String type){
        this.type=type;
    }

    /**
     * Getter de l'id du role
     * @return l'id du role
     */
    public int getId() {
        return id;
    }

    /**
     * Getter du type du role
     * @return le type du role
     */
    public String getType() {
        return type;
    }
}
