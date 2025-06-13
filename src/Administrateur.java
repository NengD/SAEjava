import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Administrateur {

    private int idadmin;
    private ConnexionMySQL connexion;

    public Administrateur(int idadmin, ConnexionMySQL connexion) {
        this.idadmin = idadmin;
        this.connexion = connexion;
    }

    public void creerVendeur(String nom, String prenom, int idmag) {
        try {
            // Générer un nouvel idvendeur
            Connection conn = this.connexion.getConnection();
            int idven = -1;
            PreparedStatement psNum = conn.prepareStatement("SELECT COALESCE(MAX(idven), 0) + 1 AS nextId FROM VENDEUR");
            ResultSet rsNum = psNum.executeQuery();
            if (rsNum.next()) {
                idven = rsNum.getInt("nextId");
            }
            rsNum.close();
            psNum.close();

            // Insérer le vendeur dans la base
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO VENDEUR (idven, nomven, prenomven, idmag) VALUES (?, ?, ?, ?)"
            );
            ps.setInt(1, idven);
            ps.setString(2, nom);
            ps.setString(3, prenom);
            ps.setInt(4, idmag);
            ps.executeUpdate();
            ps.close();
            System.out.println("Vendeur créé !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du vendeur : " + e.getMessage());
        }
    }

    public void ajouterLibrairie(String nom, String ville) {
        try {
            Connection conn = this.connexion.getConnection();
            // Générer un nouvel idmag
            int idmag = -1;
            PreparedStatement psNum = conn.prepareStatement("SELECT COALESCE(MAX(idmag), 0) + 1 AS nextId FROM MAGASIN");
            ResultSet rsNum = psNum.executeQuery();
            if (rsNum.next()) {
                idmag = rsNum.getInt("nextId");
            }
            rsNum.close();
            psNum.close();

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO MAGASIN (idmag, nommag, villemag) VALUES (?, ?, ?)"
            );
            ps.setInt(1, idmag);
            ps.setString(2, nom);
            ps.setString(3, ville);
            ps.executeUpdate();
            ps.close();
            System.out.println("Librairie ajoutée !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la librairie : " + e.getMessage());
        }
    }

    // public String consulterStatistique() { ... }
}