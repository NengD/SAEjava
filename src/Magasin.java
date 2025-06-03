import java.util.ArrayList;
import java.util.List;

public class Magasin {
    private String nom;
    private List<Vendeur> vendeurs;
    private List<Commande> commandes;
    public Stock stock;
    private static int numMagasin = 0;

    public Magasin(String nom, Stock stock) {
        this.nom = nom;
        this.vendeurs = new ArrayList<>();
        this.commandes = new ArrayList<>();
        this.stock = stock;
        numMagasin++;
    }
    public int getNumMagasin() {
        return this.numMagasin;
    }
    public void ajouterVendeur(Vendeur vendeur) {
        this.vendeurs.add(vendeur);
    }
    public void ajouteCommande(Commande commande) {
        this.commandes.add(commande);
    }

    //public String editerFacture(){}

    //public String qtatistiqueMagasin(){}

}
