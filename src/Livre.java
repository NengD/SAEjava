
import java.util.ArrayList;
import java.util.List;

public class Livre {
    private String titre;
    private int classification;
    private Double prix;
    private List<Auteur> auteurs;

    public Livre(String titre, int classification, Double prix) {
        this.titre = titre;
        this.classification = classification;
        this.prix = prix;
        this.auteurs = new ArrayList<>();
    }

    public void ajouterAuteur(Auteur auteur) {
        this.auteurs.add(auteur);
    }   


}