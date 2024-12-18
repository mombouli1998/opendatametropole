package com.example.OpendData.services.mysql;

import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.models.mysql.Wording;
import java.util.List;

/**
 *  Déclaration de l'interface `WordingService`. Elle définit un ensemble de méthodes pour la gestion des objets `Wording`.
 */
public interface WordingService {

    /**
     * Déclaration d'une méthode `lstWording` qui ne prend aucun paramètre et retourne une liste d'objets `Wording`.
     * @return List<Wording>
     */
    List<Wording> lstWording();

    /**
     * Méthode pour obtenir une liste de tous les objets `Wording` liés à un objet `Theme`.
     * @param Theme Theme
     * @return List<Wording>
     */
    List<Wording> listeWording(Theme Theme);

    /**
     * Méthode pour récupérer un objet `Wording` en fonction de son nom.
     * @param name String
     * @return Wording
     */
    Wording getWording(String name);

    /**
     * Méthode pour ajouter un nouvel objet `Wording`.
     * @param wording Wording
     * @return Wording
     */
    Wording add(Wording wording);

    /**
     * Méthode pour obtenir un objet `Wording` en fonction de son identifiant.
     * @param id int
     * @return Wording
     */
    Wording getWordingById(int id);

    /**
     * Méthode pour modifier le nombre de téléchargements d'un objet `Wording`.
     * @param wording Wording
     * @return Wording
     */
    Wording updateNbDownload(Wording wording);

    /**
     * Méthode pour modifier un objet `Wording` existant
     * @param wording Wording
     * @return Wording
     */
    Wording update(Wording wording);
}
