import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Editeur {
    private int idedit;

    public Editeur(int idedit) {
        this.idedit = idedit;
    }

    public int getIdEdit() {
        return idedit;
    }

    public String getNom(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT nomedit FROM EDITEUR WHERE idedit = ?");
            ps.setInt(1, idedit);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nomedit");
                rs.close();
                ps.close();
                return nom;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getNom Editeur : " + e.getMessage());
        }
        return null;
    }

    public List<Livre> getLivres(Connection connexion) {
        List<Livre> livres = new ArrayList<>();
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT isbn FROM EDITER WHERE idedit = ?");
            ps.setInt(1, idedit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                livres.add(new Livre(isbn));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getLivres Editeur : " + e.getMessage());
        }
        return livres;
    }
}