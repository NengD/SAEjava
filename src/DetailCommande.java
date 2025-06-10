
public class DetailCommande {
    private static int numLigne = 0;
    private Commande commande;
    private Livre livre;
    private int quantite;

    public DetailCommande(Commande commande, Livre livre, int quantite) {
        this.livre = livre;
        this.quantite = quantite;
        this.commande = commande;
        this.numLigne++;
    }
    public void resetNumLigne(){
        this.numLigne = 0;
    }
    public int getNumLigne() {
        return numLigne;
    }

    public Double prixTotal(){
        return livre.getPrix() * quantite;
    }
}
