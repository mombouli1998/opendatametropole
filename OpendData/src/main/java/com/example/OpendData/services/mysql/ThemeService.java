package com.example.OpendData.services.mysql;

import java.util.List;
import com.example.OpendData.models.mysql.Theme;

/**
 * Déclaration de l'interface `ThemeService`. Elle définit un contrat (ou ensemble de méthodes) que toute classe implémentant cette interface devra fournir.
 */
public interface ThemeService {

  /**
   * Méthode pour obtenir une liste de tous les objets `Theme`.
   * @return une liste de type `List<Theme>`.
  */
  List<Theme> listeTheme();

  /**
   * Méthode pour récupérer un objet `Theme` en fonction de son nom
   * @param name le nom du thème
   * @return un objet `Theme`
  */
  Theme getNames(String name);

  /**
   * Méthode pour récupérer un objet `Theme` en fonction de son identifiant
   * @param id l'identifiant du thème
   * @return un objet `Theme`
   */
  Theme getTheme(int id);

  /**
   * Méthode pour ajouter un objet `Theme`
   * @param Theme l'objet `Theme` à ajouter
   * @return l'objet `Theme` ajouté
   */
  Theme add(Theme Theme);

  /**
   * Méthode pour supprimer un objet `Theme` en fonction de son identifiant
   * @param id l'identifiant du thème
   */
  void delete(int id);
}
