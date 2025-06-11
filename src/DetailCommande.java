
public class DetailCommande {
    private Commande commande;
    private Livre livre;
    private int quantite;

    public DetailCommande(Commande commande, Livre livre, int quantite) {
        this.livre = livre;
        this.quantite = quantite;
        this.commande = commande;
    }

    public Double prixTotal(){
        return livre.getPrix() * quantite;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public Livre getLivre() {
        return this.livre;
    }

    public int getQuantite() {
        return this.quantite;
    }
    

}
