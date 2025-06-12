import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Livre {
    private String isbn;

    public Livre(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitre(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT titre FROM LIVRE WHERE isbn = ?");
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String titre = rs.getString("titre");
                rs.close();
                ps.close();
                return titre;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getTitre : " + e.getMessage());
        }
        return null;
    }

    public String getClassification(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement(
                "SELECT iddewey FROM THEMES WHERE isbn = ?"
            );
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String iddewey = rs.getString("iddewey");
                rs.close();
                ps.close();
                return iddewey;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getClassification : " + e.getMessage());
        }
        return null;
    }

    public Double getPrix(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT prix FROM LIVRE WHERE isbn = ?");
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double prix = rs.getDouble("prix");
                rs.close();
                ps.close();
                return prix;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getPrix : " + e.getMessage());
        }
        return null;
    }

    // À adapter selon vos classes Auteur et Editeur
    public List<Auteur> getAuteurs(Connection connexion) {
        List<Auteur> auteurs = new ArrayList<>();
        try {
            PreparedStatement ps = connexion.prepareStatement(
                "SELECT idauteur FROM ECRIRE WHERE isbn = ?"
            );
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String idauteur = rs.getString("idauteur");
                auteurs.add(new Auteur(idauteur));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getAuteurs : " + e.getMessage());
        }
        return auteurs;
    }

    public Editeur getEditeur(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement(
                "SELECT idedit FROM EDITER WHERE isbn = ?"
            );
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idedit = rs.getInt("idedit");
                rs.close();
                ps.close();
                return new Editeur(idedit);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getEditeur : " + e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn;
    }
}