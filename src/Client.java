import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class Client extends Personne {

    private String adresse;
    private List<Commande> commandes;
    private ConnexionMySQL connexion;
    private static int numClient = 0;

    public Client(String nom, String prenom, String adresse, ConnexionMySQL connexion) {
        super(nom, prenom);
        this.adresse = adresse;
        this.commandes = new ArrayList<>();
        this.connexion = connexion;
        numClient++;
    }

    public void passerCommande(boolean enLigne, char typeLivraison, List<Livre> livres, Magasin magasin) {
        Commande commande = new Commande(enLigne, typeLivraison, this);
        magasin.ajouteCommande(commande);
        try {
            PreparedStatement ps1 = connexion.prepareStatement("INSERT INTO COMMANDE (numcom, datecom, enligne, livraison, idcli, idmag) VALUES (?, ?, ?, ?, ?, ?)");
            ps1.setInt(1, commande.getNumCommande());
            ps1.setString(2, LocalDate.now().toString());
            if (enLigne) {
                ps1.setString(3, "O");
            } else {
                ps1.setString(3, "N");
            }
            ps1.setString(4, String.valueOf(typeLivraison));
            ps1.setInt(5, this.numClient);
            ps1.setInt(6, magasin.getNumMagasin());
            ps1.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de la commande : " + e.getMessage());
        }


        Map<Livre, Integer> dictLivres = new HashMap<>();
        for (Livre livre : livres) {
            if (dictLivres.containsKey(livre)) {
                dictLivres.put(livre, dictLivres.get(livre) + 1);
            } else {
                dictLivres.put(livre, 1);
            }
        }

        for (Livre livre : dictLivres.keySet()) {
            DetailCommande detailCommande = new DetailCommande(commande , livre, dictLivres.get(livre));
            try {
                PreparedStatement ps2 = connexion.prepareStatement("INSERT INTO DETAILCOMMANDE (numcom, numlig, qte, prixvente, isbn) VALUES (?, ?, ?, ?, ?)");
                ps2.setInt(1, commande.getNumCommande());
                ps2.setInt(2, detailCommande.getNumLigne());
                ps2.setInt(3, dictLivres.get(livre));
                ps2.setDouble(4, livre.getPrix());
                ps2.setString(5, livre.getIsbn().toString());
                ps2.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'insertion du détail de la commande : " + e.getMessage());
            }
        }
        
        magasin.stock.majQuantiteLivre(livres.get(0), -1);
    }

    public List<String> consulterCatalogue() {
        Statement s=connexion.createStatement();
        try {
            ResultSet rs=s.executeQuery("SELECT titre, nomclass, prix, nomedit, nomauteur FROM LIVRE NATURAL JOIN AUTEUR NATURAL JOIN EDITEUR NATURAL JOIN CLASSIFICATION");
            List<String> livres=new ArrayList<>();
            while (rs.next()) {
            String titre=rs.getString("titre");
            String nomClass=rs.getString("nomclass");
            double prix=rs.getDouble("prix");
            String nomEdit=rs.getString("nomedit");
            String nomAuteur=rs.getString("nomauteur");

            livres.add(titre+" - "+nomClass+" - "+prix+" - "+nomEdit+" - "+nomAuteur);
            }
            rs.close();
            return livres;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation du catalogue : " + e.getMessage());
            return null;
        }
    }
}
    //public List<Livre> onVousRecommande(){}

