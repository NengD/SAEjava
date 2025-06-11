import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Client extends Personne {

    private String adresse;
    private List<Commande> commandes;
    private static int numClient = 0;

    public Client(String nom, String prenom, String adresse, ConnexionMySQL connexion) {
        super(nom, prenom, connexion);
        this.adresse = adresse;
        this.commandes = new ArrayList<>();
        numClient++;
    }

    public void passerCommande(boolean enLigne, char typeLivraison, List<Livre> livres, Magasin magasin) {
        Commande commande = new Commande(enLigne, typeLivraison, this);
        magasin.ajouteCommande(commande);
        try {
            PreparedStatement ps1 = this.connexion.prepareStatement("INSERT INTO COMMANDE (numcom, datecom, enligne, livraison, idcli, idmag) VALUES (?, ?, ?, ?, ?, ?)");
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
            dictLivres.put(livre, dictLivres.getOrDefault(livre, 0) + 1);
        }

        int numlig = 1;
        for (Livre livre : dictLivres.keySet()) {
            try {
                PreparedStatement ps2 = connexion.prepareStatement(
                    "INSERT INTO DETAILCOMMANDE (numcom, numlig, isbn) VALUES (?, ?, ?)"
                );
                ps2.setInt(1, commande.getNumCommande());
                ps2.setInt(2, numlig);
                ps2.setString(3, livre.getIsbn().toString());
                ps2.executeUpdate();
                ps2.close();

                // Mettre à jour le stock pour chaque livre et quantité
                magasin.stock.majQuantiteLivre(livre, -dictLivres.get(livre));
                numlig++;
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'insertion du détail de la commande : " + e.getMessage());
            }
        }
    }

    public List<String> consulterCatalogue() {
        List<String> livres=new ArrayList<>();
        try {
            Statement s = this.connexion.createStatement();
            ResultSet rs=s.executeQuery("SELECT titre, nomclass, prix, nomedit, nomauteur FROM LIVRE NATURAL JOIN AUTEUR NATURAL JOIN EDITEUR NATURAL JOIN CLASSIFICATION");
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

