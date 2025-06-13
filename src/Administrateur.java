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


    public void afficherChiffreAffaires() {
        try {
            Connection conn = this.connexion.getConnection();
            // Chiffre d'affaires par magasin
            PreparedStatement ps = conn.prepareStatement(
                "SELECT m.idmag, m.nommag, SUM(d.qte * d.prixvente) AS ca " +
                "FROM MAGASIN m " +
                "LEFT JOIN VENDEUR v ON m.idmag = v.idmag " +
                "LEFT JOIN COMMANDE c ON v.idven = c.idven " +
                "LEFT JOIN DETAILCOMMANDE d ON c.idcom = d.idcom " +
                "GROUP BY m.idmag, m.nommag"
            );
            ResultSet rs = ps.executeQuery();
            double caTotal = 0.0;
            System.out.println("Chiffre d'affaires par magasin :");
            while (rs.next()) {
                String nomMag = rs.getString("nommag");
                double ca = rs.getDouble("ca");
                System.out.printf("- %s : %.2f €\n", nomMag, ca);
                caTotal += ca;
            }
            System.out.printf("Chiffre d'affaires total : %.2f €\n", caTotal);
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage du chiffre d'affaires : " + e.getMessage());
        }
    }



    public void bestseller() {
        try {
            Connection conn = this.connexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT l.idlivre, l.titre, SUM(d.qte) AS total_vendu " +
                "FROM LIVRE l " +
                "JOIN DETAILCOMMANDE d ON l.idlivre = d.idlivre " +
                "GROUP BY l.idlivre, l.titre " +
                "ORDER BY total_vendu DESC " +
                "LIMIT 1"
            );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String titre = rs.getString("titre");
                int quantite = rs.getInt("total_vendu");
                System.out.println("Meilleur livre toutes boutiques confondues : " + titre + " (" + quantite + " exemplaires vendus)");
            } else {
                System.out.println("Aucun livre vendu.");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du bestseller : " + e.getMessage());
        }
    }

   public void consulterStatisques(){
    System.out.println("=== Statistiques ===");
    afficherChiffreAffaires();
    bestseller();
   }



}