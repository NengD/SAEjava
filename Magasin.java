import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Magasin {
    private String nom;

    /**
     * Constructeur de la classe Magasin.
     * @param nom nom du magasin
     */
    public Magasin(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le nom du magasin.
     * @return nom du magasin
     */
    public String getNom() {
        return nom;
    }

    /**
     * Récupère l'identifiant du magasin depuis la base de données.
     * @param connexion connexion à la base de données
     * @return identifiant du magasin ou null si non trouvé
     */
    public String getIdMagasin(Connection connexion) {
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT idmag FROM MAGASIN WHERE nommag = ?");
            ps.setString(1, this.nom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String id = rs.getString("idmag");
                rs.close();
                ps.close();
                return id;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getIdMagasin : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère la liste des commandes passées dans ce magasin.
     * @param connexion connexion à la base de données
     * @return liste des commandes du magasin
     */ 
    public List<Commande> getCommandes(Connection connexion) {
        List<Commande> commandes = new ArrayList<>();
        String idMagasin = getIdMagasin(connexion);
        if (idMagasin == null) return commandes;
        try {
            PreparedStatement ps = connexion.prepareStatement(
                "SELECT numcom FROM COMMANDE WHERE idmag = ?"
            );
            ps.setString(1, idMagasin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int numcom = rs.getInt("numcom");
                commandes.add(new Commande(numcom));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getCommandes : " + e.getMessage());
        }
        return commandes;
    }

    /**
     * Récupère le stock du magasin (ISBN et quantité).
     * @param connexion connexion à la base de données
     * @return liste des stocks sous forme de chaînes
     */
    public List<String> getStock(Connection connexion) {
        List<String> stock = new ArrayList<>();
        String idMagasin = getIdMagasin(connexion);
        if (idMagasin == null) return stock;
        try {
            PreparedStatement ps = connexion.prepareStatement("SELECT isbn, qte FROM POSSEDER WHERE idmag = ?");
            ps.setString(1, idMagasin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                int qte = rs.getInt("qte");
                stock.add("ISBN: " + isbn + ", Quantité: " + qte);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erreur getStock : " + e.getMessage());
        }
        return stock;
    }

    /**
     * Génère une facture détaillée pour le magasin.
     * @param connexion connexion à la base de données
     * @return facture sous forme de chaîne de caractères
     */
    public String editerFacture(Connection connexion) {
    StringBuilder facture = new StringBuilder();
    String idMagasin = getIdMagasin(connexion);

    facture.append(" _______      ___         ______ .___________. __    __  .______         _______ \n");
    facture.append("|   ____|    /   \\      /      ||           ||  |  |  | |   _  \\      |   ____|\n");
    facture.append("|  |__      /  ^  \\    |  ,----'`---|  |----`|  |  |  | |  |_)  |      |  |__   \n");
    facture.append("|   __|    /  /_\\ \\   |  |         |  |     |  |  |  | |      /       |   __|  \n");
    facture.append("|  |      /  ____   \\  |  `----.    |  |     |  `--'  | |  |\\  \\---. |  |____ \n");
    facture.append("|__|     /__/     \\__\\ \\______|   |__|      \\______/ | _| `.______| |_______|\n");
    for(int i=0; i< 4; i++){
        facture.append("                                                                                \n");
    }
    facture.append("=================================================================================\n");
    facture.append("||                                                                             ||\n");
    facture.append("|| Facture du magasin ").append(this.nom).append(" ID:").append(idMagasin).append("                        ||\n");
    facture.append("||                                                                             ||\n");
    facture.append("=================================================================================\n");
    facture.append("|| Commandes :                                                                 ||\n");

    double total = 0.0;
    List<Commande> commandes = getCommandes(connexion);
    for (Commande commande : commandes) {
        List<DetailCommande> details = commande.getDetailsCommande(connexion);
        for (DetailCommande detail : details) {
            int quantite = detail.getQuantite(connexion);
            double prix = detail.getPrixVente(connexion);
            double prixTotal = prix * quantite;
            String isbn = detail.getIsbn();
            String titre = "";
            try {
                PreparedStatement ps = connexion.prepareStatement("SELECT titre FROM LIVRE WHERE isbn = ?");
                ps.setString(1, isbn);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    titre = rs.getString("titre");
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                titre = "Titre inconnu";
            }
            facture.append("|| Livre : ").append(titre)
                    .append(" | Quantité : ").append(quantite)
                    .append(" | Prix unitaire : ").append(prix)
                    .append(" | Total : ").append(prixTotal).append(" €      ||\n");
            total += prixTotal;
        }
    }
    facture.append("=================================================================================\n");
    facture.append("|| Total à payer : ").append(total).append(" €\n");
    facture.append("=================================================================================\n");
    return facture.toString();
}

  

}
