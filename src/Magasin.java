import java.util.ArrayList;
import java.util.List;

public class Magasin {
    private String nom;
    private List<Vendeur> vendeurs;
    private List<Commande> commandes;
    public Stock stock;
    private String idMagasin;

    public Magasin(String nom) {
        this.nom = nom;
        this.vendeurs = new ArrayList<>();
        this.commandes = new ArrayList<>();
        this.stock = new Stock(this);
        this.idMagasin=idMagasin;
    }

    public Magasin(String nom, Stock stock) {
        this.nom = nom;
        this.vendeurs = new ArrayList<>();
        this.commandes = new ArrayList<>();
        this.stock = stock;
        this.idMagasin=idMagasin;
    }

    public String getIdMagasin() {
        return this.idMagasin;
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
