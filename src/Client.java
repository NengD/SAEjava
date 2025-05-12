import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Client extends Personne {

    private String adresse;
    private List<Commande> commandes;
    private ConnexionMySQL connexion;

    public Client(String nom, String prenom, String adresse, ConnexionMySQL connexion) {
        super(nom, prenom);
        this.adresse = adresse;
        this.commandes = new ArrayList<>();
        this.connexion = connexion;
    }

    public void passerCommande(boolean enLigne, String typeLivraison, List<Livre> livres) {
        
    }

    public List<String> consulterCatalogue() {
        List<String> livres = new ArrayList<>();
        String requete = "SELECT titre, nomclass, prix, nomedit, nomauteur FROM LIVRE NATURAL JOIN AUTEUR NATURAL JOIN EDITEUR NATURAL JOIN CLASSIFICATION";
        try (Statement s = this.connexion.createStatement();
             ResultSet rs = s.executeQuery(requete)) {
            while (rs.next()) {
                String titre = rs.getString("titre");
                String classification = rs.getString("nomclass");
                String prix = Double.toString(rs.getDouble("prix"));
                String nomEditeur = rs.getString("nomedit");
                String auteurs = rs.getString("nomauteur");
                if (titre == null) titre = "Inconnu";
                if (classification == null) classification = "Inconnu";
                if (nomEditeur == null) nomEditeur = "Inconnu";
                if (auteurs == null) auteurs = "Inconnu";
                livres.add(titre + " " + classification + " " + prix + " " + nomEditeur + " " + auteurs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livres;
    }
}

    //public List<Livre> onVousRecommande(){}

