package com.example.OpendData;

import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.models.mysql.Wording;
import com.example.OpendData.repositories.mysql.Wordingrepositorie;
import com.example.OpendData.services.mysql.WordingService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe `WordingImplements` qui implémente l'interface `WordingService`.
 */
@Service 
public class WordingImplements implements WordingService {

    private Wordingrepositorie wordingrepositorie;

    /**
     * Constructeur de la classe `WordingImplements`.
     * @param wordingrepositorie : `Wordingrepositorie` qui est injecté par Spring.
     */
    public WordingImplements(Wordingrepositorie wordingrepositorie) {
        this.wordingrepositorie = wordingrepositorie;
    }

    @Override 
    public List<Wording> lstWording() {
        return wordingrepositorie.findAll();
    }

    @Override
    public List<Wording> listeWording(Theme Theme) {
        List<Wording> lstWording = new ArrayList<>();

        wordingrepositorie.findAll().forEach(wording -> {
            // Parcourt tous les objets `Wording` présents dans la base de données en utilisant la méthode `findAll()` de JPA.
            if (wording.getTheme().getName().equals(Theme.getName()) && wording.getTheme().getId() == Theme.getId()) {
                // Vérifie si le `Theme` associé à un `Wording` correspond au `Theme` passé en paramètre (comparaison par nom et identifiant).
                lstWording.add(wording);  // Si la condition est remplie, ajoute cet objet `Wording` à la liste `lstWording`.
            }
        });
        return lstWording;
    }

    @Override
    public Wording getWording(String name) {
        return wordingrepositorie.findByName(name);
    }

    @Override
    public Wording add(Wording wording) {

        return wordingrepositorie.save(wording);

    }

    @Override
    public Wording getWordingById(int id) {
        return wordingrepositorie.findById(id).orElse(null);
    }

    @Override
    public Wording updateNbDownload(Wording wording) {
        Wording w=wordingrepositorie.findById(wording.getId()).orElse(null);
        w.setDownload(wording.getDownload());

        return wordingrepositorie.save(w);
    }

    @Override
    public Wording update(Wording wording) {
        return wordingrepositorie.save(wording);
    }


}
