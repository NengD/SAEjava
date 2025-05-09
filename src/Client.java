import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;   


public class Client extends Personne {

    private String adresse;
    private List<Commande> commandes;
    private ConnexionMySQL connexion;


    public Client(String nom, String prenom, String adresse) {
        super(nom, prenom);
        this.adresse = adresse;
        this.commandes = new ArrayList<>();
    }
    public void passserCommande(boolean enLigne,String typeLivraison,List<Livre> livres){

    }

    public List<Livre> consulterCatalogue(){
        List<Livre> livres=new ArrayList<>();
        Statement s=laConnexion.createStatement();
		ResultSet rs=s.executeQuery("SELECT * FROM LIVRE");	
		while (rs.next()){
            String titre=rs.getString("titre");
            String classification=rs.getString("classification");
            Double prix=rs.getDouble("prix");
            int idEditeur=rs.getInt("idEditeur");
            Editeur editeur=new Editeur(idEditeur);
            List<Auteur> auteurs=new ArrayList<>();
            Livre livre=new Livre(titre, classification, prix, auteurs, editeur);
            livres.add(livre);
        }
		rs.close();
		return livres;
    }

    public List<Livre> onVousRecommande(){
        
    }


}
