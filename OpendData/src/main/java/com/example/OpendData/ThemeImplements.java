package com.example.OpendData;

//@tm430209
import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.repositories.mysql.Themerepositorie;
import com.example.OpendData.services.mysql.ThemeService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Classe `ThemeImplements` qui implémente l'interface `ThemeService`.
 */
@Service  // Annotation `@Service` qui indique que cette classe est un composant de service Spring. Elle est utilisée pour implémenter la logique métier.
public class ThemeImplements implements ThemeService {

    private Themerepositorie themerepositorie;

    /**
     * Constructeur qui permet l'injection de dépendance de `Themerepositorie` dans cette classe.
     * @param themerepositorie : `Themerepositorie` qui est injecté par Spring.
     */
    public ThemeImplements(Themerepositorie themerepositorie) {
        this.themerepositorie = themerepositorie; 
    }

    @Override
    public List<Theme> listeTheme() {
        return themerepositorie.findAll(); 
    }

    @Override
    public Theme getNames(String name) {
        Theme themes=new Theme();
        themerepositorie.findAll().forEach(element->{
            if (element.getName().equals(name)) {
                themes.setId(element.getId());
                themes.setName(element.getName());
            }

        });
        return  themes;
    }

    @Override
    public Theme getTheme(int id) {
        return themerepositorie.findById(id).orElseThrow(null);
    }

    @Override  // Annotation qui indique que cette méthode redéfinit une méthode de l'interface parent (ici, l'interface ThemeService).
    public Theme add(Theme theme) {
        return themerepositorie.save(theme);
    }

    @Override
    public void delete(int id) {
        themerepositorie.deleteById(id);
    }
}
