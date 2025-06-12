import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Auteur {
    private String idauteur;

    public Auteur(String idauteur) {
        this.idauteur = idauteur;
    }

    public String getIdAuteur() {
        return idauteur;
    }

    public String getNom(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT nomauteur FROM AUTEUR WHERE idauteur = ?");
            ps.setString(1, idauteur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nomauteur");
                rs.close();
                ps.close();
                return nom;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getNom Auteur : " + e.getMessage());
        }
        return null;
    }

    public String getPrenom(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT prenomauteur FROM AUTEUR WHERE idauteur = ?");
            ps.setString(1, idauteur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String prenom = rs.getString("prenomauteur");
                rs.close();
                ps.close();
                return prenom;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getPrenom Auteur : " + e.getMessage());
        }
        return null;
    }

    public List<Livre> getLivres(Connection connexion) {
        List<Livre> livres = new ArrayList<>();
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT isbn FROM ECRIRE WHERE idauteur = ?");
            ps.setString(1, idauteur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                livres.add(new Livre(isbn));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getLivres Auteur : " + e.getMessage());
        }
        return livres;
    }
}