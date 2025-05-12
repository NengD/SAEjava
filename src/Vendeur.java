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

    //public boolean livreDisponible(String nomLivre){}

    public void passerCommandePourClient(boolean enLigne,String typeLivraison,List<Livre> livres,Client client){

    }
}
