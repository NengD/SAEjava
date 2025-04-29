import java.util.List;
import java.util.ArrayList;

public class Client extends Personne {

    private String adresse;
    private List<Commande> commandes;


    public Client(String nom, String prenom, String adresse) {
        super(nom, prenom);
        this.adresse = adresse;
        this.commandes = new ArrayList<>();
    }

    public void passserCommande(boolean enLigne,String typeLivraison,List<Livre> livres){

    }

    public List<Livre> consulterCatalogue(){

    }

    public List<Livre> onVousRecommande(){
        
    }


}
