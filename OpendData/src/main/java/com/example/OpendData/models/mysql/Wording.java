package com.example.OpendData.models.mysql;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Classe `Wording` qui représente une entité JPA liée à la table `wording` dans la base de données MySQL.
 */
@Entity  // Annotation qui indique que cette classe est une entité JPA, et correspond à une table dans une base de données.
public class Wording {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)  // Annotation qui spécifie que la valeur de la clé primaire `id` sera générée automatiquement (auto-incrémentée) par la base de données avec la stratégie `IDENTITY`.
    private int id;

    private String name;

    private int download;

    private String license;

    private LocalDateTime modification; 

    @ManyToOne  // Annotation qui définit une relation "Many-to-One" (plusieurs instances de `Wording` peuvent être associées à un seul `Theme`).
    @JoinColumn(name = "theme_id") 
    private Theme theme; 

    @Lob
    private String descriptions; 

    private String territory;

    @Lob  // Annotation qui indique que ce champ contient un type de données volumineux (Large Object). Ici, il stocke un fichier PDF sous forme de tableau d'octets (`byte[]`).
    private byte[] pdf;

    @Lob 
    private byte[] csv; 

    @Lob 
    private byte[] xml; 

    private String langue;
    public Wording() {
        // Constructeur sans argument. Il est nécessaire pour permettre à JPA de créer une instance de l'entité via réflexion.
    }

    /**
     * Constructeur de la classe `Wording`.
     * @param name Nom du jeu de données.
     * @param download Nombre de téléchargements.
     * @param license Licence du jeu de données.
     * @param modification Date de dernière modification.
     * @param theme Thème du jeu de données.
     * @param descriptions Description du jeu de données.
     * @param pdf Contenu du fichier PDF.
     * @param csv Contenu du fichier CSV.
     * @param xml Contenu du fichier XML.
     * @param langue Langue du jeu de données.
     * @param territory Territoire du jeu de données.
     * @return Une instance de la classe `Wording`.
     */
    public Wording(String name, int download, String license, LocalDateTime modification, Theme theme, String descriptions, byte[] pdf, byte[] csv, byte[] xml,String langue, String territory) {
        this.name = name;
        this.download = download;
        this.license = license;
        this.modification = modification;
        this.theme = theme;
        this.descriptions = descriptions;
        this.pdf = pdf;
        this.csv = csv;
        this.xml = xml;
        this.langue=langue;
        this.territory=territory;
    }

    /**
     * Getter pour accéder au champ `territory`.
     * @return Le territoire du jeu de données.
     */
    public String getTerritory() {
        return territory;
    }

    /**
     * Setter pour modifier la valeur du champ `territory`.
     * @param territory Le territoire du jeu de données.
     */
    public void setTerritory(String territory) {
        this.territory = territory;
    }
    
    /**
     * Getter pour accéder au champ `name`.
     * @return Le nom du jeu de données.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter pour modifier la valeur du champ `name`.
     * @param name Le nom du jeu de données.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter pour accéder au champ `descriptions`.
     * @return La description du jeu de données.
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * Setter pour modifier la valeur du champ `descriptions`.
     * @param descriptions La description du jeu de données.
     */
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Getter pour accéder au champ `Theme`.
     * @return Le thème du jeu de données.
     */
    public Theme getTheme() {
        return theme;
    }

    /**
     * Setter pour modifier la valeur du champ `Theme`.
     * @param Theme Le thème du jeu de données.
     */
    public void setTheme(Theme Theme) {
        this.theme = Theme;
    }

    /**
     * Getter pour accéder au champ `pdf`.
     * @return Le contenu du fichier PDF.
     */
    public byte[] getPdf() {
        return pdf;
    }

    /**
     * Setter pour modifier la valeur du champ `pdf`.
     * @param pdf Le contenu du fichier PDF.
     */
    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    /**
     * Getter pour accéder au champ `csv`.
     * @return Le contenu du fichier CSV.
     */
    public byte[] getCsv() {
        return csv;
    }

    /**
     * Setter pour modifier la valeur du champ `csv`.
     * @param csv Le contenu du fichier CSV.
     */
    public void setCsv(byte[] csv) {
        this.csv = csv;
    }

    /**
     * Getter pour accéder au champ `xml`.
     * @return Le contenu du fichier XML.
     */
    public byte[] getXml() {
        return xml;
    }

    /**
     * Setter pour modifier la valeur du champ `xml`.
     * @param xml Le contenu du fichier XML.
     */
    public void setXml(byte[] xml) {
        this.xml = xml;
    }

    /**
     * Getter pour accéder au champ `id`.
     * @return L'identifiant du jeu de données.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter pour accéder au champ `download`.
     * @return Le nombre de téléchargements du jeu de données.
     */
    public int getDownload() {
        return download;
    }

    /**
     * Setter pour modifier la valeur du champ `download`.
     * @param download Le nombre de téléchargements du jeu de données.
    */
    public void setDownload(int download) {
        this.download = download;
    }

    /**
     * Getter pour accéder au champ `license`.
     * @return La licence du jeu de données.
     */
    public String getLicense() {
        return license;
    }

    /**
     * Setter pour modifier la valeur du champ `license`.
     * @param license La licence du jeu de données.
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * Getter pour accéder au champ `modification`.
     * @return La date de dernière modification du jeu de données.
     */
    public LocalDateTime getModification() {
        return modification;
    }

    /**
     * Setter pour modifier la valeur du champ `modification`.
     * @param modification La date de dernière modification du jeu de données.
     */
    public void setModification(LocalDateTime modification) {
        this.modification = modification;
    }

    /**
     * Getter pour accéder au champ `langue`.
     * @return La langue du jeu de données.
     */
    public String getLangue() {
        return langue;
    }

    /**
     * Setter pour modifier la valeur du champ `langue`.
     * @param langue La langue du jeu de données.
     */
    public void setLangue(String langue) {
        this.langue = langue;
    }
}
