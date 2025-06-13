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

    public void transfertLivreEntreMagasins(String isbn, int quantite, int idMagasinSource, int idMagasinDest) {
        try {
            Connection conn = this.connexion.getConnection();

            // Vérifier la quantité disponible dans le magasin source
            PreparedStatement psCheck = conn.prepareStatement(
                "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
            );
            psCheck.setInt(1, idMagasinSource);
            psCheck.setString(2, isbn);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                int qteSource = rs.getInt("qte");
                if (qteSource < quantite) {
                    System.out.println("Quantité insuffisante dans le magasin source.");
                    rs.close();
                    psCheck.close();
                    return;
                }
            } else {
                System.out.println("Livre non trouvé dans le magasin source.");
                rs.close();
                psCheck.close();
                return;
            }
            rs.close();
            psCheck.close();

            // Retirer la quantité du magasin source
            PreparedStatement psUpdateSource = conn.prepareStatement(
                "UPDATE POSSEDER SET qte = qte - ? WHERE idmag = ? AND isbn = ?"
            );
            psUpdateSource.setInt(1, quantite);
            psUpdateSource.setInt(2, idMagasinSource);
            psUpdateSource.setString(3, isbn);
            psUpdateSource.executeUpdate();
            psUpdateSource.close();

            // Ajouter ou mettre à jour la quantité dans le magasin destination
            PreparedStatement psCheckDest = conn.prepareStatement(
                "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
            );
            psCheckDest.setInt(1, idMagasinDest);
            psCheckDest.setString(2, isbn);
            ResultSet rsDest = psCheckDest.executeQuery();
            if (rsDest.next()) {
                PreparedStatement psUpdateDest = conn.prepareStatement(
                    "UPDATE POSSEDER SET qte = qte + ? WHERE idmag = ? AND isbn = ?"
                );
                psUpdateDest.setInt(1, quantite);
                psUpdateDest.setInt(2, idMagasinDest);
                psUpdateDest.setString(3, isbn);
                psUpdateDest.executeUpdate();
                psUpdateDest.close();
            } else {
                PreparedStatement psInsertDest = conn.prepareStatement(
                    "INSERT INTO POSSEDER (idmag, isbn, qte) VALUES (?, ?, ?)"
                );
                psInsertDest.setInt(1, idMagasinDest);
                psInsertDest.setString(2, isbn);
                psInsertDest.setInt(3, quantite);
                psInsertDest.executeUpdate();
                psInsertDest.close();
            }
            rsDest.close();
            psCheckDest.close();

            System.out.println("Transfert effectué avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors du transfert : " + e.getMessage());
        }
    }

    public void creerLivre(String isbn, String titre, int nbpages, int datepubli, double prix, String iddewey) {
        try {
            Connection conn = this.connexion.getConnection();
            // Vérifier si le livre existe déjà
            PreparedStatement psCheck = conn.prepareStatement(
                "SELECT isbn FROM LIVRE WHERE isbn = ?"
            );
            psCheck.setString(1, isbn);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                System.out.println("Ce livre existe déjà dans la base.");
                rs.close();
                psCheck.close();
                return;
            }
            rs.close();
            psCheck.close();

            // Insérer le livre
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO LIVRE (isbn, titre, nbpages, datepubli, prix) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setString(1, isbn);
            ps.setString(2, titre);
            ps.setInt(3, nbpages);
            ps.setInt(4, datepubli);
            ps.setDouble(5, prix);
            ps.executeUpdate();
            ps.close();

            if (iddewey != null && !iddewey.isEmpty()) {
            PreparedStatement psCheckDewey = conn.prepareStatement(
                "SELECT iddewey FROM CLASSIFICATION WHERE iddewey = ?"
            );
            psCheckDewey.setString(1, iddewey);
            ResultSet rsDewey = psCheckDewey.executeQuery();
            if (rsDewey.next()) {
                PreparedStatement psTheme = conn.prepareStatement(
                    "INSERT INTO THEMES (isbn, iddewey) VALUES (?, ?)"
                );
                psTheme.setString(1, isbn);
                psTheme.setString(2, iddewey);
                psTheme.executeUpdate();
                psTheme.close();
            } else {
                System.out.println("Erreur : l'ID Dewey n'existe pas dans la table CLASSIFICATION.");
            }
            rsDewey.close();
            psCheckDewey.close();
        }
            System.out.println("Livre ajouté à la base !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    public void afficherChiffreAffaires() {
        try {
            Connection conn = this.connexion.getConnection();
            // Chiffre d'affaires par magasin (correction des jointures et noms de colonnes)
            PreparedStatement ps = conn.prepareStatement(
                "SELECT m.idmag, m.nommag, SUM(d.qte * d.prixvente) AS ca " +
                "FROM MAGASIN m " +
                "LEFT JOIN COMMANDE c ON m.idmag = c.idmag " +
                "LEFT JOIN DETAILCOMMANDE d ON c.numcom = d.numcom " +
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
                "SELECT l.isbn, l.titre, SUM(d.qte) AS total_vendu " +
                "FROM LIVRE l " +
                "JOIN DETAILCOMMANDE d ON l.isbn = d.isbn " +
                "GROUP BY l.isbn, l.titre " +
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