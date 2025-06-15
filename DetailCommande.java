import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailCommande {
    private int numCommande;
    private String isbn;

    /**
    * Constructeur de la classe DetailCommande.
    * @param numCommande numéro de la commande
    * @param isbn ISBN du livre
    */
    public DetailCommande(int numCommande, String isbn) {
        this.numCommande = numCommande;
        this.isbn = isbn;
    }

    /**
    * Retourne le numéro de la commande.
    * @return numéro de la commande
    */
    public int getNumCommande() {
        return numCommande;
    }

    /**
     * Retourne l'ISBN du livre.
     * @return ISBN du livre
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Récupère la quantité commandée pour ce livre dans cette commande.
     * @param connexion connexion à la base de données
     * @return quantité commandée
     */
    public int getQuantite(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement(
                "SELECT qte FROM DETAILCOMMANDE WHERE numcom = ? AND isbn = ?"
            );
            ps.setInt(1, numCommande);
            ps.setString(2, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int qte = rs.getInt("qte");
                rs.close();
                ps.close();
                return qte;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getQuantite : " + e.getMessage());
        }
        return 0;
    }
    /**
     * Récupère le prix de vente du livre pour cette commande.
     * @param connexion connexion à la base de données
     * @return prix de vente
     */
    public double getPrixVente(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement(
                "SELECT prixvente FROM DETAILCOMMANDE WHERE numcom = ? AND isbn = ?"
            );
            ps.setInt(1, numCommande);
            ps.setString(2, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double prix = rs.getDouble("prixvente");
                rs.close();
                ps.close();
                return prix;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getPrixVente : " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Calcule le prix total pour ce livre dans cette commande.
     * @param connexion connexion à la base de données
     * @return prix total (prix de vente * quantité)
     */
    public double prixTotal(Connection connexion) {
        return getPrixVente(connexion) * getQuantite(connexion);
    }
}