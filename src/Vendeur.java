import java.util.List;

public class Vendeur extends Personne {

    private Magasin magasin;

    public Vendeur(String nom, String prenom) {
        super(nom, prenom);
    }

    public void ajouterLivre(Livre livre, int quantite){
        this.magasin.stock.ajouterLivre(livre, quantite);
    }

    public void majQuantiteLivre(Livre livre, int quantite){
        this.magasin.stock.ajouterLivre(livre, quantite);
    }

    public boolean livreDisponible(String nomLivre){
        for (Livre livre : this.magasin.stock.getQuantiteLivre().keySet()) {
            if (livre.getTitre().equalsIgnoreCase(nomLivre)) {
                return this.magasin.stock.getQuantiteLivre().get(livre) > 0;
            }
        }
        return false;
    }

    public void transfertLivre(Livre livre, int quantite, Magasin magasinDest){
        if (this.magasin.stock.getQuantiteLivre().containsKey(livre) && this.magasin.stock.getQuantiteLivre().get(livre) >= quantite) {
            this.magasin.stock.majQuantiteLivre(livre, -quantite);
            magasinDest.stock.majQuantiteLivre(livre, quantite);
        } else {
            System.out.println("Quantité insuffisante dans le stock du magasin d'origine.");
        }
    }

    public void passerCommandePourClient(boolean enLigne,String typeLivraison,List<Livre> livres,Client client){
    }
}
