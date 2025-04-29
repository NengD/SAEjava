
import java.util.List;

public class DetailCommande {
    private int quantite;
    private Commande commande;
    private List<Livre> livres;

    public DetailCommande(int quantite, Commande commande, List<Livre> livres) {
        this.quantite = quantite;
        this.commande = commande;
        this.livres = livres;
    }
    
    
    public Double prixTotal(){

    }
}
