import java.util.ArrayList;
import java.util.List;

public class Auteur extends Personne {

    private List<Livre> livres;
    
    public Auteur(String nom, String prenom) {
        super(nom, prenom);
        this.livres = new ArrayList<>();
    }

    public List<Livre> aEcrit(){
        List<Livre> ecritPar = new ArrayList<>();
        Auteur auteurs = new Auteur(nom, prenom);
            for (Livre l : livres){
                if (l.getAuteurs() == auteurs){
                ecritPar.add(l);
                } 
            }
            return ecritPar;
    }

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
    }   
}
