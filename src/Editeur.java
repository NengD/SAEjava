import java.util.ArrayList;
import java.util.List;

public class Editeur {
    private String nom;
    private List<Livre> livres;

    public Editeur(String nom) {
        this.nom = nom;
        this.livres = new ArrayList<>();
    }

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
    }

}
