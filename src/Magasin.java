import java.util.ArrayList;
import java.util.List;

public class Magasin {
    private String nom;
    private List<Vendeur> vendeurs;
    private List<Commande> commandes;
    public Stock stock;

    public Magasin(String nom, Stock stock) {
        this.nom = nom;
        this.vendeurs = new ArrayList<>();
        this.commandes = new ArrayList<>();
        this.stock = stock;
    }

    //public String editerFacture(){}

    //public String qtatistiqueMagasin(){}

}
