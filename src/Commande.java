public class Commande {
    private static int numCommande;
    private boolean enLigne;
    private char typeLivraison;
    private String dateCommande;
    private Client client;
    private DetailCommande detailCommande;

    public Commande(boolean enLigne, char typeLivraison, Client client) {
        this.numCommande++;
        this.enLigne = enLigne;
        this.typeLivraison = typeLivraison;
        this.client = client;
    }
    public int getNumCommande() {
        return this.numCommande;
    }
    public boolean isEnLigne() {
        return this.enLigne;
    }
    public char getTypeLivraison() {
        return this.typeLivraison;
    }
    public String getDateCommande() {
        return this.dateCommande;
    }
    public Client getClient() {
        return this.client;
    }
    public DetailCommande getDetailCommande() {
        return this.detailCommande;
    }
    
}

