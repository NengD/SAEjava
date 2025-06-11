import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Stock {
    private Magasin magasin;
    private Map<Livre,Integer> quantiteLivre;
    private ConnexionMySQL connexion;

    
    public Stock(Magasin magasin,ConnexionMySQL connexion) {
        this.magasin = magasin;
        this.quantiteLivre = new HashMap<>();
        this.connexion = connexion;
    }

    public void ajouterLivre(Livre livre, int quantite) {
        try{
            Statement s = this.connexion.createStatement();
            ResultSet rs = s.executeQuery("select * from STOCK where isbn = '" + livre.getIsbn() + "'");
            if (rs.next()) {
                System.out.println("Le livre est déjà présent dans le stock.");
            } else{
            PreparedStatement ps = this.connexion.prepareStatement("insert into STOCK (isbn,quantite) values(?,?)");
            ps.setString(1,livre.getIsbn());
            ps.setInt(2, quantite);
        }
        } 
            catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du stock : " + e.getMessage());
        }
}

    public void majQuantiteLivre(Livre livre, int quantite) {
        try{
            Statement s = this.connexion.createStatement();
            ResultSet rs = s.executeQuery("select * from STOCK where isbn = '" + livre.getIsbn() + "'");
            if (rs.next()) {
                PreparedStatement ps = this.connexion.prepareStatement("update STOCK set quantite = quantite + ? where isbn = ?");
                ps.setInt(1, quantite);
                ps.setString(2, livre.getIsbn());
                ps.executeUpdate();
            } else {
                System.out.println("Le livre n'est pas présent dans le stock.");
            }
        }catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la quantité du livre : " + e.getMessage());
    }
}

    public Double getValeurStock() {
        Double valeur = 0.0;
        
        try {
            Statement s = this.connexion.createStatement();
            ResultSet rs = s.executeQuery("select * from STOCK");
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                int quantite = rs.getInt("quantite");
                if (isbn != null && quantite > 0) {
                ResultSet rs1 = s.executeQuery("select prix from Livre where isbn = '" + isbn + "'");
                double prix = rs1.getDouble("prix");
                valeur += prix * quantite;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la valeur du stock : " + e.getMessage());
        }
        
        return valeur;
    }

    public Integer getQuantiteLivre() {
        try{
            Statement s = this.connexion.createStatement();
            ResultSet rs = s.executeQuery("select * from STOCK");
            int quantite = 0;
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                if (isbn != null) {
                    quantite += rs.getInt("quantite");
                }
            }
            return quantite;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la quantité totale de livres : " + e.getMessage());
            return null;
        }
    }
}

