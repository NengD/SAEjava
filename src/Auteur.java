import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Auteur {
    private String idauteur;

    /**
     * Constructeur de la classe Auteur.
     * @param idauteur identifiant de l'auteur
     */
    public Auteur(String idauteur) {
        this.idauteur = idauteur;
    }

     /**
     * Retourne l'identifiant de l'auteur.
     * @return identifiant de l'auteur
     */
    public String getIdAuteur() {
        return idauteur;
    }

    /**
     * Récupère le nom de l'auteur depuis la base de données.
     * @param connexion connexion à la base de données
     * @return nom de l'auteur ou null si non trouvé
     */
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

    /**
     * Récupère le prénom de l'auteur depuis la base de données.
     * @param connexion connexion à la base de données
     * @return prénom de l'auteur ou null si non trouvé
     */
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

     /**
     * Récupère la liste des livres écrits par cet auteur.
     * @param connexion connexion à la base de données
     * @return liste des livres de l'auteur
     */
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