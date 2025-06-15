import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int numCommande;

    /**
     * Constructeur de la classe Commande.
     * @param numCommande numéro de la commande
     */
    public Commande(int numCommande) {
        this.numCommande = numCommande;
    }

     /**
     * Retourne le numéro de la commande.
     * @return numéro de la commande
     */
    public int getNumCommande() {
        return this.numCommande;
    }

    /**
     * Indique si la commande a été passée en ligne.
     * @param connexion connexion à la base de données
     * @return true si la commande est en ligne, false sinon
     */
    public boolean isEnLigne(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT enligne FROM COMMANDE WHERE numcom = ?");
            ps.setInt(1, numCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String enligne = rs.getString("enligne");
                rs.close();
                ps.close();
                return "O".equalsIgnoreCase(enligne);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur isEnLigne : " + e.getMessage());
        }
        return false;
    }

     /**
     * Récupère le type de livraison de la commande.
     * @param connexion connexion à la base de données
     * @return type de livraison (caractère)
     */
    public char getTypeLivraison(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT livraison FROM COMMANDE WHERE numcom = ?");
            ps.setInt(1, numCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                char livraison = rs.getString("livraison").charAt(0);
                rs.close();
                ps.close();
                return livraison;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getTypeLivraison : " + e.getMessage());
        }
        return ' ';
    }

    /**
     * Récupère la date de la commande.
     * @param connexion connexion à la base de données
     * @return date de la commande (String) ou null si non trouvée
     */
    public String getDateCommande(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT datecom FROM COMMANDE WHERE numcom = ?");
            ps.setInt(1, numCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String date = rs.getString("datecom");
                rs.close();
                ps.close();
                return date;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getDateCommande : " + e.getMessage());
        }
        return null;
    }

     /**
     * Récupère l'identifiant du client ayant passé la commande.
     * @param connexion connexion à la base de données
     * @return identifiant du client ou -1 si non trouvé
     */
    public int getIdClient(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT idcli FROM COMMANDE WHERE numcom = ?");
            ps.setInt(1, numCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idcli = rs.getInt("idcli");
                rs.close();
                ps.close();
                return idcli;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getIdClient : " + e.getMessage());
        }
        return -1;
    }

    /**
     * Récupère la liste des détails de la commande.
     * @param connexion connexion à la base de données
     * @return liste des détails de la commande
     */
    public List<DetailCommande> getDetailsCommande(Connection connexion) {
        List<DetailCommande> details = new ArrayList<>();
        try {
            PreparedStatement ps = connexion.prepareStatement(
                "SELECT isbn FROM DETAILCOMMANDE WHERE numcom = ?"
            );
            ps.setInt(1, numCommande);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                details.add(new DetailCommande(numCommande, isbn));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getDetailsCommande : " + e.getMessage());
        }
        return details;
    }
    
}

