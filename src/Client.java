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

    public List<String> consulterCatalogue(){
        List<String> livres=new ArrayList<>();
        Statement s=this.connexion.createStatement();
		ResultSet rs=s.executeQuery("SELECT titre,nomclass,prix,nomedit,nomauteur FROM LIVRE NATURAL JOIN AUTEUR NATURAL JOIN EDITEUR NATURAL JOIN CLASSIFICATION");	
		while (rs.next()){
            String titre=rs.getString("titre");
            String classification=rs.getString("classification");
            String prix=Double.toString(rs.getDouble("prix"));
            String nomEditeur=rs.getString("nomedit");
            String auteurs=rs.getString("nomauteur");
            livres.add(titre+" "+classification+" "+prix+" "+nomEditeur);
        }
		rs.close();
		return livres;
    }

    //public List<Livre> onVousRecommande(){}


}
