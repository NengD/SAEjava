import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe représentant un administrateur de la librairie.
 * Permet la gestion des vendeurs, librairies, transferts de livres et statistiques.
 */
public class Administrateur {

    private int idadmin;
    private ConnexionMySQL connexion;

    /**
     * Constructeur Administrateur.
     * @param idadmin identifiant de l'administrateur
     * @param connexion connexion à la base de données
     */
    public Administrateur(int idadmin, ConnexionMySQL connexion) {
        this.idadmin = idadmin;
        this.connexion = connexion;
    }

    /**
     * Crée un nouveau vendeur dans la base de données.
     * @param nom nom du vendeur
     * @param prenom prénom du vendeur
     * @param idmag identifiant du magasin d'affectation
     */
    public void creerVendeur(String nom, String prenom, int idmag) {
        try {
            Connection conn = this.connexion.getConnection();
            int idven = -1;
            PreparedStatement psNum = conn.prepareStatement(
                "SELECT COALESCE(MAX(idven), 0) + 1 AS nextId FROM VENDEUR");
            ResultSet rsNum = psNum.executeQuery();
            if (rsNum.next()) {
                idven = rsNum.getInt("nextId");
            }
            rsNum.close();
            psNum.close();

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO VENDEUR (idven, nomven, prenomven, idmag) VALUES (?, ?, ?, ?)");
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

    /**
     * Ajoute une nouvelle librairie (magasin) dans la base de données.
     * @param nom nom de la librairie
     * @param ville ville de la librairie
     */
    public void ajouterLibrairie(String nom, String ville) {
        try {
            Connection conn = this.connexion.getConnection();
            int idmag = -1;
            PreparedStatement psNum = conn.prepareStatement(
                "SELECT COALESCE(MAX(idmag), 0) + 1 AS nextId FROM MAGASIN");
            ResultSet rsNum = psNum.executeQuery();
            if (rsNum.next()) {
                idmag = rsNum.getInt("nextId");
            }
            rsNum.close();
            psNum.close();

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO MAGASIN (idmag, nommag, villemag) VALUES (?, ?, ?)");
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

    /**
     * Transfère une quantité de livre d'un magasin source vers un magasin destination.
     * @param isbn ISBN du livre à transférer
     * @param quantite quantité à transférer
     * @param idMagasinSource identifiant du magasin source
     * @param idMagasinDest identifiant du magasin destination
     */
    public void transfertLivreEntreMagasins(String isbn, int quantite, int idMagasinSource, int idMagasinDest) {
        try {
            Connection conn = this.connexion.getConnection();

            // Vérifier la quantité disponible dans le magasin source
            PreparedStatement psVerif = conn.prepareStatement(
                "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?");
            psVerif.setInt(1, idMagasinSource);
            psVerif.setString(2, isbn);
            ResultSet rs = psVerif.executeQuery();
            if (rs.next()) {
                int qteSource = rs.getInt("qte");
                if (qteSource < quantite) {
                    System.out.println("Quantité insuffisante dans le magasin source.");
                    rs.close();
                    psVerif.close();
                    return;
                }
            } else {
                System.out.println("Livre non trouvé dans le magasin source.");
                rs.close();
                psVerif.close();
                return;
            }
            rs.close();
            psVerif.close();

            // Retirer la quantité du magasin source
            PreparedStatement psUpdateSource = conn.prepareStatement(
                "UPDATE POSSEDER SET qte = qte - ? WHERE idmag = ? AND isbn = ?");
            psUpdateSource.setInt(1, quantite);
            psUpdateSource.setInt(2, idMagasinSource);
            psUpdateSource.setString(3, isbn);
            psUpdateSource.executeUpdate();
            psUpdateSource.close();

            // Ajouter ou mettre à jour la quantité dans le magasin destination
            PreparedStatement psVerifDest = conn.prepareStatement(
                "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?");
            psVerifDest.setInt(1, idMagasinDest);
            psVerifDest.setString(2, isbn);
            ResultSet rsDest = psVerifDest.executeQuery();
            if (rsDest.next()) {
                PreparedStatement psUpdateDest = conn.prepareStatement(
                    "UPDATE POSSEDER SET qte = qte + ? WHERE idmag = ? AND isbn = ?");
                psUpdateDest.setInt(1, quantite);
                psUpdateDest.setInt(2, idMagasinDest);
                psUpdateDest.setString(3, isbn);
                psUpdateDest.executeUpdate();
                psUpdateDest.close();
            } else {
                PreparedStatement psInsertDest = conn.prepareStatement(
                    "INSERT INTO POSSEDER (idmag, isbn, qte) VALUES (?, ?, ?)");
                psInsertDest.setInt(1, idMagasinDest);
                psInsertDest.setString(2, isbn);
                psInsertDest.setInt(3, quantite);
                psInsertDest.executeUpdate();
                psInsertDest.close();
            }
            rsDest.close();
            psVerifDest.close();

            System.out.println("Transfert effectué avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors du transfert : " + e.getMessage());
        }
    }

    /**
     * Crée un nouveau livre dans la base de données, et l'associe à une classification si précisée.
     * @param isbn ISBN du livre
     * @param titre titre du livre
     * @param nbpages nombre de pages
     * @param datepubli année de publication
     * @param prix prix du livre
     * @param iddewey identifiant de la classification (peut être null)
     */
    public void creerLivre(String isbn, String titre, int nbpages, int datepubli, double prix, String iddewey) {
        try {
            Connection conn = this.connexion.getConnection();
            // Vérifier si le livre existe déjà
            PreparedStatement psVerif = conn.prepareStatement(
                "SELECT isbn FROM LIVRE WHERE isbn = ?");
            psVerif.setString(1, isbn);
            ResultSet rs = psVerif.executeQuery();
            if (rs.next()) {
                System.out.println("Ce livre existe déjà dans la base.");
                rs.close();
                psVerif.close();
                return;
            }
            rs.close();
            psVerif.close();

            // Insérer le livre
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO LIVRE (isbn, titre, nbpages, datepubli, prix) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, isbn);
            ps.setString(2, titre);
            ps.setInt(3, nbpages);
            ps.setInt(4, datepubli);
            ps.setDouble(5, prix);
            ps.executeUpdate();
            ps.close();

            // Associer à une classification si précisée
            if (iddewey != null && !iddewey.isEmpty()) {
                PreparedStatement psVerifDewey = conn.prepareStatement(
                    "SELECT iddewey FROM CLASSIFICATION WHERE iddewey = ?");
                psVerifDewey.setString(1, iddewey);
                ResultSet rsDewey = psVerifDewey.executeQuery();
                if (rsDewey.next()) {
                    PreparedStatement psTheme = conn.prepareStatement(
                        "INSERT INTO THEMES (isbn, iddewey) VALUES (?, ?)");
                    psTheme.setString(1, isbn);
                    psTheme.setString(2, iddewey);
                    psTheme.executeUpdate();
                    psTheme.close();
                } else {
                    System.out.println("Erreur : l'ID Dewey n'existe pas dans la table CLASSIFICATION.");
                }
                rsDewey.close();
                psVerifDewey.close();
            }
            System.out.println("Livre ajouté à la base !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    /**
     * Affiche le chiffre d'affaires par magasin et le total.
     */
    public void afficherChiffreAffaires() {
        try {
            Connection conn = this.connexion.getConnection();
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

    /**
     * Affiche le livre le plus vendu (bestseller) toutes boutiques confondues.
     */
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

    /**
     * Affiche les statistiques principales : chiffre d'affaires et bestseller.
     */
    public void consulterStatisques() {
        System.out.println("=== Statistiques ===");
        afficherChiffreAffaires();
        bestseller();
    }
}