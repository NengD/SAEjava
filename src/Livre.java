
import java.util.ArrayList;
import java.util.List;

public class Livre {
    private String titre;
    private String classification;
    private Double prix;
    private List<Auteur> auteurs;
    private Editeur editeur;

    public Livre(String titre, String classification, Double prix, List<Auteur> auteurs, Editeur editeur) {
        this.titre = titre;
        this.classification = classification;
        this.prix = prix;
        this.auteurs = new ArrayList<>();
        this.editeur = editeur;
        for (Auteur auteur : auteurs) {
            auteur.ajouterLivre(this);
        }
    }

    public String getTitre() {
        return titre;
    }

    public String getClassification() {
        return classification;
    }

    public Double getPrix() {
        return prix;
    }

    public List<Auteur> getAuteurs() {
        return auteurs;
    }

    public Editeur getEditeur() {
        return editeur;
    }
    


}