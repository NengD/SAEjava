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
        this.stock = new Stock(this, null);
        this.idMagasin=idMagasin;
    }

    public Magasin(String nom, Stock stock) {
        this.nom = nom;
        this.vendeurs = new ArrayList<>();
        this.commandes = new ArrayList<>();
        this.stock = stock;
        this.idMagasin = idMagasin;
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

    public String editerFacture(){
        String facture = "";
        facture +=" _______      ___         ______ .___________. __    __  .______         _______ \n";
        facture +="|   ____|    /   \\      /      ||           ||  |  |  | |   _  \\      |   ____|\n";
        facture +="|  |__      /  ^  \\    |  ,----'`---|  |----`|  |  |  | |  |_)  |      |  |__   \n";
        facture +="|   __|    /  /_\\ \\   |  |         |  |     |  |  |  | |      /       |   __|  \n";
        facture +="|  |      /  ____   \\  |  `----.    |  |     |  `--'  | |  |\\  \\---. |  |____ \n";       
        facture +="|__|     /__/     \\__\\ \\______|   |__|      \\______/ | _| `.______| |_______|\n";
        for(int i=0; i< 4; i++){
            facture += "                                                                                \n";       
        }
        facture +="=================================================================================\n";
        facture +="||                                                                             ||\n";
        facture +="|| Facture du magasin"+this.nom+" ID:"+this.idMagasin+"                        ||\n";
        facture +="||                                                                             ||\n";
        facture +="=================================================================================\n";
        facture +="||                                                                             ||\n";
        facture +="|| Commandes :                                                                 ||\n";
        double total = 0.0;
        for (Commande commande : this.commandes) {
            if (commande.getDetailCommande() != null) {
                Livre livre = commande.getDetailCommande().getLivre();
                int quantite = commande.getDetailCommande().getQuantite();
                double prix = livre.getPrix();
                double prixTotal = prix * quantite;
                facture += "|| Livre : " + livre.getTitre()
                        + " | Quantité : " + quantite
                        + " | Prix unitaire : " + prix
                        + " | Total : " + prixTotal + " €      ||\n";
                total += prixTotal;
            }
            facture += "=================================================================================\n";
            facture +="|| Total à payer"+total+"                                                       ||\n";
            facture += "=================================================================================\n";
        }
    return facture;
    }

    //public String qtatistiqueMagasin(){}

}
