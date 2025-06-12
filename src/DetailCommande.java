import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailCommande {
    private int numCommande;
    private String isbn;

    public DetailCommande(int numCommande, String isbn) {
        this.numCommande = numCommande;
        this.isbn = isbn;
    }

    public int getNumCommande() {
        return numCommande;
    }

    public String getIsbn() {
        return isbn;
    }

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

    public double prixTotal(Connection connexion) {
        return getPrixVente(connexion) * getQuantite(connexion);
    }
}