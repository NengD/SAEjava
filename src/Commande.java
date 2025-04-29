public class Commande {
    private int numCommande;
    private boolean enLigne;
    private char typeLivraison;
    private Client client;
    private DetailCommande detailCommande;

    public Commande(int numCommande, boolean enLigne, char typeLivraison, Client client) {
        this.numCommande = numCommande;
        this.enLigne = enLigne;
        this.typeLivraison = typeLivraison;
        this.client = client;
    }
}
