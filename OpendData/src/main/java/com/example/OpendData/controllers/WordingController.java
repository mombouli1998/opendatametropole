package com.example.OpendData.controllers;

//@tm430209
import java.nio.file.Files;
import com.example.OpendData.models.mysql.Theme;
import com.example.OpendData.models.mysql.Wording;
import com.example.OpendData.services.mysql.ThemeService;
import com.example.OpendData.services.mysql.WordingService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Déclaration de la classe `WordingController`, qui traite les requêtes liées aux objets `Wording`.
 */
@Controller 
public class WordingController {

    private WordingService wordingService;
    private ThemeService themeService;
    private List<Map<String, Object>> licences ;

    /**
     * Constructeur de la classe `WordingController`.
     */
    public WordingController(WordingService wordingService, ThemeService themeService) { 
        this.wordingService = wordingService;
        this.themeService = themeService;

        // Chargement des licences depuis le fichier JSON
        try {
                ClassPathResource resource = new ClassPathResource("licences.json");
                try (InputStream inputStream = resource.getInputStream()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        this.licences = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
                    } catch (StreamReadException e) {
                        e.printStackTrace();
                    } catch (DatabindException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    throw e;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode qui retourne la vue pour l'ajout d'un libellé.
     */
    @GetMapping("/wording")
    public String addwording(Model model) {
        List<Theme> lstSuject = themeService.listeTheme(); // Récupère la liste des sujets à partir du service `ThemeService`.

        model.addAttribute("S", lstSuject);
        model.addAttribute("licences", licences);
        // Ajoute la liste des sujets au modèle, pour qu'elle soit accessible dans la vue.

        return "wording";
    }

    /**
     * Méthode qui traite l'ajout d'un libellé avec les paramètres fournis par le formulaire.
     * @param name Paramètre pour le name du libellé
     * @param city Paramètre pour la city du wording
     * @param date Paramètre pour la date de modification
     * @param license Paramètre pour la license du wording
     * @param description Paramètre pour la description du wording
     * @param Theme Paramètre pour le theme associé au wording
     * @param pdf Paramètre pour le fichier PDF uploadé
     * @param xml Paramètre pour le fichier XML uploadé
     * @param csv  Paramètre pour le fichier CSV uploadé
     * @param language Paramètre pour la langue du wording
     * @param model Modèle pour ajouter des attributs à la vue
     * @return Retourne le name de la vue ou l'instruction de redirection
     */
    @PostMapping("/wording")
    public String add(@RequestParam("name") String name, @RequestParam("city")String city, @RequestParam("date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("license") String license, @RequestParam("descriptions") String description,  @RequestParam("Theme") String Theme, @RequestParam("pdf") MultipartFile pdf, @RequestParam("xml") MultipartFile xml, @RequestParam("csv") MultipartFile csv, @RequestParam("language") String language, Model model) { 

        String t = "";  // Déclaration d'une variable `t` pour stocker le name de la vue de redirection.

        byte[] pdfBytes = null; 
        byte[] xmlBytes = null;
        byte[] csvBytes = null;

        try {
                if (!pdf.isEmpty()) {
                    pdfBytes = pdf.getBytes(); 
                }
                if (!xml.isEmpty()) {
                    xmlBytes = xml.getBytes(); 
                }
                if (!csv.isEmpty()) {
                    csvBytes = csv.getBytes(); 
                }
        } catch (IOException e) {
            e.printStackTrace();  // Affiche la trace de l'exception en cas d'erreur de conversion.
        }

        // Si un libellé avec le même name existe déjà,
        if (wordingService.getWording(name) != null) {
                t = "redirect:/wording?error=libelle";  // Prépare la redirection vers la page d'accueil.
        }

        // Vérifie si un libellé avec le même name n'existe pas déjà.
        if (wordingService.getWording(name) == null) {
            // Si aucun libellé n'est trouvé avec ce name,
            Theme Theme1 = themeService.getNames(Theme);
            // Récupère le sujet correspondant au name du thème fourni.
            Wording wording= new Wording(name,0,license, date.atStartOfDay(),Theme1,description,pdfBytes,csvBytes,xmlBytes, language,city);
            wordingService.add(wording);  // Ajoute le nouvel objet `Wording` à la base de données.
            t = "redirect:/";  // Prépare la redirection vers la page d'accueil (ou une autre page de votre choix).
        }

        return t;
    }

    /**
     * Méthode pas disponible pour le public pour charger les données dans la base de données.
     * @param model Modèle pour ajouter des attributs à la vue
     * @return
     */
    @GetMapping("/load") 
    public String load(Model model) {

        String theme = "Démographie et Analyse du territoire";
        Theme theme1 = themeService.getNames(theme);

        try {
            String csvFilePath = System.getProperty("user.dir") + "/scripts/amenagements-cyclables.csv";
            Path path = Paths.get(csvFilePath);
            byte[] csvBytes = Files.readAllBytes(path);
            Wording wording1 = new Wording("Aménagements cyclables", 0, "Licence Ouverte v2.0", LocalDate.now().atStartOfDay(),theme1, "Dispositif de voirie destiné à organiser la circulation des vélos. Ces aménagements peuvent prendre la forme de chaussées dédiées aux cyclistes, restreintes à certains usagers dont les cyclistes, de panneaux indicateurs spécifiques ou de facilités de circulation.", null, csvBytes, null, "Français","Dijon Métropole");
            wordingService.add(wording1);

            csvFilePath = System.getProperty("user.dir") + "/scripts/waze-alerts.csv";
            path = Paths.get(csvFilePath);
            csvBytes = Files.readAllBytes(path);
            Wording wording2 = new Wording("Alertes Waze", 0, "Licence Ouverte v2.0", LocalDate.now().atStartOfDay(),theme1, "Données des incidents de circulation en temps réel sur Dijon Métropole relevés par les utilisateurs de l'application Waze.", null, csvBytes, null, "Français","Dijon Métropole");
            wordingService.add(wording2);

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "redirect:/";
    }

    /**
     * Méthode qui retourne la vue pour la recherche de libellés.
     */
    @GetMapping("/search")
    public String searches(Model model){
        String t="";
        List<Wording>lst=wordingService.lstWording();
        if(lst!=null){
            model.addAttribute("search",lst);
            List<Theme> lstSuject = themeService.listeTheme();
            List<String> lstTerritory = lst.stream().map(Wording::getTerritory).distinct().collect(Collectors.toList());
            List<String> lstLanguage = lst.stream().map(Wording::getLangue).distinct().collect(Collectors.toList());

            model.addAttribute("subjects", lstSuject);
            model.addAttribute("languages", lstLanguage);
            model.addAttribute("licences", licences);
            model.addAttribute("territories", lstTerritory);
            t="search";
        }
        else{
            t = "redirect:/";
        }
        
        return t;
    }


    /**
     * Méthode pour le téléchargement d'un fichier CSV.
     * @param id Identifiant du libellé
     * @return
     */
    @PostMapping("/wording/download/csv")
    public ResponseEntity<byte[]> downloadCSV(@RequestParam("id") int id) {
        Wording wording = wordingService.getWordingById(id);
        int n=wording.getDownload()+1;
        wording.setDownload(n);
        wordingService.updateNbDownload(wording);
        byte[] csvContent = wording.getCsv();

        // Si le fichier n'existe pas regarder si il existe en xml et le convertir en csv
        if (csvContent == null) {
            byte[] xmlContent = wording.getXml();
            if (xmlContent != null) {
                // Convertir le XML en CSV
                String xmlString = new String(xmlContent, StandardCharsets.UTF_8);
                StringBuilder csvBuilder = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(new StringReader(xmlString))) {
                    String ligne;
                    String headerLine = reader.readLine();
                    if (headerLine != null) {
                        String[] headers = headerLine.split(";");

                        csvBuilder.append(String.join(";", headers)).append("\n");

                        while ((ligne = reader.readLine()) != null) {
                            String[] values = ligne.split(";");
                            csvBuilder.append(String.join(";", values)).append("\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                csvContent = csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
            }
        }

        // Configuration des en-têtes HTTP pour le téléchargement
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("file.csv").build());

        return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
    }

    /**
     * Méthode pour le téléchargement d'un fichier XML.
     * @param id Identifiant du libellé
     */
    @PostMapping("/wording/download/xml")
    public ResponseEntity<byte[]> downloadXML(@RequestParam("id") int id) throws ParserConfigurationException {
        Wording wording = wordingService.getWordingById(id);
        int n=wording.getDownload()+1;
        wording.setDownload(n);
        wordingService.updateNbDownload(wording);
        byte[] xmlContent = wording.getXml();

        // Si le fichier n'existe pas regarder si il existe en csv et le convertir en xml
        if (xmlContent == null) {
            xmlContent = convertCsvToXml(wording.getCsv()).getBytes(StandardCharsets.UTF_8);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDisposition(ContentDisposition.attachment().filename("file.xml").build());

        return new ResponseEntity<>(xmlContent, headers, HttpStatus.OK);
    }

    private String convertCsvToXml(byte[] csvContent) throws ParserConfigurationException {
        String csvString = new String(csvContent, StandardCharsets.UTF_8);
        StringBuilder xmlBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new StringReader(csvString))) {
            String ligne;
            String headerLine = reader.readLine();
            if (headerLine != null) {
                String[] headers = headerLine.split(";");

                xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                xmlBuilder.append("<data>\n");

                while ((ligne = reader.readLine()) != null) {
                    String[] values = ligne.split(";");
                    xmlBuilder.append("  <row>\n");

                    for (int i = 0; i < headers.length; i++) {
                        xmlBuilder.append("    <").append(headers[i]).append(">")
                                .append(values[i]).append("</").append(headers[i]).append(">\n");
                    }

                    xmlBuilder.append("  </row>\n");
                }

                xmlBuilder.append("</data>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlBuilder.toString();
    }

    /**
     * Méthode pour le téléchargement d'un fichier JSON.
     * @param id Identifiant du libellé
     */
    @GetMapping("/wording/download/json/{id}")
    public ResponseEntity<byte[]> downloadJSON(@PathVariable("id") int id) {
        Wording wording = wordingService.getWordingById(id);
        int n=wording.getDownload()+1;
        wording.setDownload(n);
        wordingService.updateNbDownload(wording);
        byte[] jsonContent = null;

        if (wording.getCsv() != null) {
            jsonContent = convertCsvToJson(new String(wording.getCsv(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(ContentDisposition.attachment().filename("file.json").build());

        return new ResponseEntity<>(jsonContent, headers, HttpStatus.OK);

    }

    /**
     * Méthode pour convertir un fichier CSV en JSON.
     */
    private String convertCsvToJson(String csvContent) {
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            String ligne;
            String headerLine = reader.readLine();
            if (headerLine != null) {
                String[] headers = headerLine.split(";");

                jsonBuilder.append("[\n");

                while ((ligne = reader.readLine()) != null) {
                    String[] values = ligne.split(";");
                    jsonBuilder.append("  {\n");

                    for (int i = 0; i < headers.length; i++) {
                        jsonBuilder.append("    \"").append(headers[i]).append("\": \"")
                                .append(values[i]).append("\"");
                        if (i < headers.length - 1) {
                            jsonBuilder.append(",");
                        }
                        jsonBuilder.append("\n");
                    }

                    jsonBuilder.append("  }");
                    if (reader.ready()) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\n");
                }

                jsonBuilder.append("]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBuilder.toString();
    }


    /**
     * Méthode pour afficher les détails d'une donnée.
     * @param id Identifiant du libellé
     * @param model
     * @return
     */
   @GetMapping("/wording/{id}")
   public String getWording(@PathVariable int id, Model model) {
       Wording wording = wordingService.getWordingById(id);
       if (wording == null) {
           return "error"; // Rediriger vers une page d'erreur si le wording n'est pas trouvé
       }
       if (wording.getPdf() != null) {
           String pdfBase64 = "data:application/pdf;base64," + Base64.getEncoder().encodeToString(wording.getPdf());
           model.addAttribute("pdfBase64", pdfBase64); // Stocker l'URL complète en base64
       }
       // Encodage du CSV en base64
       if (wording.getCsv() != null) {
        String csvContent = new String(wording.getCsv(), StandardCharsets.UTF_8);
    
        // Transformer le CSV en liste de Map
        List<Map<String, String>> donneesCsv = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            String ligne;
    
            // Lire la première ligne pour les en-têtes
            String headerLine = reader.readLine();
            if (headerLine != null) { // Vérifier si les en-têtes existent
                String[] headers = headerLine.split(";"); // Lecture des en-têtes
    
                while ((ligne = reader.readLine()) != null) {
                    String[] values = ligne.split(";");
                    Map<String, String> ligneMap = new HashMap<>();
    
                    for (int i = 0; i < headers.length; i++) {
                        if (i < values.length) {
                            ligneMap.put(headers[i], values[i]);
                        } else {
                            ligneMap.put(headers[i], ""); // Valeur par défaut si une colonne est manquante
                        }
                    }
    
                    donneesCsv.add(ligneMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
            // Ajouter les données CSV au modèle
            model.addAttribute("donneesCsv", donneesCsv);
        } else if (wording.getXml() != null) {
            String xmlContent = new String(wording.getXml(), StandardCharsets.UTF_8);
            List<Map<String, String>> donneesCsv = convertXmlToCsv(xmlContent);
            model.addAttribute("donneesCsv", donneesCsv);
        }


       // Encodage du XML en base64
       if (wording.getXml() != null) {
           String xmlBase64 = "data:text/xml;base64," + Base64.getEncoder().encodeToString(wording.getXml());
           model.addAttribute("xmlBase64", xmlBase64);

       }

       if (wording.getModification() != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = wording.getModification().format(formatter);
        model.addAttribute("formattedDate", formattedDate);
      } else {
            model.addAttribute("formattedDate", "Date non disponible");
        }

       model.addAttribute("wording", wording);
       model.addAttribute("licences", licences);
       return "wordingid"; // name de la vue Thymeleaf
   }

   /**
    * Convertir un contenu XML en CSV
    * @param xmlContent Contenu XML
    * @return
    */
   private List<Map<String, String>> convertXmlToCsv(String xmlContent) {
        List<Map<String, String>> donneesCsv = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlContent));
            Document document = builder.parse(is);

            NodeList rowNodes = document.getElementsByTagName("row");
            for (int i = 0; i < rowNodes.getLength(); i++) {
                Element rowElement = (Element) rowNodes.item(i);
                Map<String, String> ligneMap = new HashMap<>();

                NodeList children = rowElement.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        ligneMap.put(child.getNodeName(), child.getTextContent());
                    }
                }
                donneesCsv.add(ligneMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donneesCsv;
    }

    /**
     * Méthode pour télécharger un fichier PDF.
     * @param id Identifiant du libellé
     * @return
     */
    @PostMapping("/wording/download/pdf")
    public ResponseEntity<ByteArrayResource> downloadPdf(@RequestParam("id") int id) {
        Wording wording = wordingService.getWordingById(id);
        if (wording != null && wording.getPdf() != null) {
            int n=wording.getDownload()+1;
            wording.setDownload(n);
            wordingService.updateNbDownload(wording);

            // download le pdf
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + wording.getName() + ".pdf")
                    .body(new ByteArrayResource(wording.getPdf()));
            
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Méthode pour la modification du fichier PDF.
     * @param id Identifiant du libellé
     * @param pdf Fichier PDF
     * @return
     */
    @PostMapping("/wording/edit/pdf")
    public String editPdf(@RequestParam("id") int id, @RequestParam("pdf") MultipartFile pdf) {
        Wording wording = wordingService.getWordingById(id);
        if (wording != null) {
            try {
                wording.setPdf(pdf.getBytes());
                wordingService.update(wording);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/wording/" + id;
    }

    /**
     * Méthode pour la modification du fichier CSV.
     * @param id Identifiant du libellé
     * @param csv Fichier CSV
     * @return
     */
    @PostMapping("/wording/edit/csv")
    public String editCsv(@RequestParam("id") int id, @RequestParam("csv") MultipartFile csv) {
        Wording wording = wordingService.getWordingById(id);
        if (wording != null) {
            try {
                wording.setCsv(csv.getBytes());
                wordingService.update(wording);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/wording/" + id;
    }

    /**
     * Méthode pour la modification du fichier XML.
     * @param id Identifiant du libellé
     * @param xml Fichier XML
     * @return
     */
    @PostMapping("/wording/edit/xml")
    public String editXml(@RequestParam("id") int id, @RequestParam("xml") MultipartFile xml) {
        Wording wording = wordingService.getWordingById(id);
        if (wording != null) {
            try {
                wording.setXml(xml.getBytes());
                wordingService.update(wording);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/wording/" + id;
    }
    
}


