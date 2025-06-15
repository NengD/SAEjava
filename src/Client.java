import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe représentant un client de la librairie.
 * Permet de passer des commandes, consulter le catalogue et obtenir des recommandations.
 */
public class Client {
    private int idcli;
    private ConnexionMySQL connexion;

    /**
     * Constructeur Client.
     * @param idcli identifiant du client
     * @param connexion connexion à la base de données
     */
    public Client(int idcli, ConnexionMySQL connexion) {
        this.idcli = idcli;
        this.connexion = connexion;
    }

    /**
     * Retourne l'identifiant du client.
     * @return identifiant du client
     */
    public int getId() {
        return idcli;
    }

    /**
     * Permet au client de passer une commande.
     * @param enLigne true si la commande est en ligne, false sinon
     * @param typeLivraison type de livraison ('M', 'C')
     * @param livres liste des livres à commander
     * @param idmagasin identifiant du magasin
     */
    public void passerCommande(boolean enLigne, char typeLivraison, List<Livre> livres, int idmagasin) {
        Connection connexion = this.connexion.getConnection();
        try {
            // Générer un nouveau numéro de commande
            int numCommande = -1;
            PreparedStatement psNum = connexion.prepareStatement(
                "SELECT COALESCE(MAX(numcom), 0) + 1 AS nextNum FROM COMMANDE");
            ResultSet rsNum = psNum.executeQuery();
            if (rsNum.next()) {
                numCommande = rsNum.getInt("nextNum");
            }
            rsNum.close();
            psNum.close();

            // Insérer la commande
            PreparedStatement ps1 = connexion.prepareStatement(
                "INSERT INTO COMMANDE (numcom, datecom, enligne, livraison, idcli, idmag) VALUES (?, ?, ?, ?, ?, ?)");
            ps1.setInt(1, numCommande);
            ps1.setString(2, java.time.LocalDate.now().toString());
            ps1.setString(3, enLigne ? "O" : "N");
            ps1.setString(4, String.valueOf(typeLivraison));
            ps1.setInt(5, this.idcli);
            ps1.setInt(6, idmagasin);
            ps1.executeUpdate();
            ps1.close();

            // Calculer la quantité de chaque livre
            Map<String, Integer> dictLivres = new HashMap<>();
            for (Livre livre : livres) {
                dictLivres.put(livre.getIsbn(), dictLivres.getOrDefault(livre.getIsbn(), 0) + 1);
            }

            int numlig = 1;
            for (String isbn : dictLivres.keySet()) {
                int quantite = dictLivres.get(isbn);

                // Récupérer le prix du livre dynamiquement
                double prix = 0.0;
                PreparedStatement psPrix = connexion.prepareStatement(
                    "SELECT prix FROM LIVRE WHERE isbn = ?");
                psPrix.setString(1, isbn);
                ResultSet rsPrix = psPrix.executeQuery();
                if (rsPrix.next()) {
                    prix = rsPrix.getDouble("prix");
                }
                rsPrix.close();
                psPrix.close();

                // Insérer le détail de la commande
                PreparedStatement ps2 = connexion.prepareStatement(
                    "INSERT INTO DETAILCOMMANDE (numcom, numlig, isbn, qte, prixvente) VALUES (?, ?, ?, ?, ?)");
                ps2.setInt(1, numCommande);
                ps2.setInt(2, numlig);
                ps2.setString(3, isbn);
                ps2.setInt(4, quantite);
                ps2.setDouble(5, prix);
                ps2.executeUpdate();
                ps2.close();

                // Mettre à jour le stock du magasin
                PreparedStatement psMaj = connexion.prepareStatement(
                    "UPDATE POSSEDER SET qte = qte - ? WHERE idmag = ? AND isbn = ?");
                psMaj.setInt(1, quantite);
                psMaj.setInt(2, idmagasin);
                psMaj.setString(3, isbn);
                psMaj.executeUpdate();
                psMaj.close();
                numlig++;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du passage de la commande : " + e.getMessage());
        }
    }

    /**
     * Retourne la liste des livres du catalogue.
     * @param connexion connexion à la base de données
     * @return liste des livres
     */
    public static List<Livre> consulterCatalogue(Connection connexion) {
        List<Livre> livres = new ArrayList<>();
        try {
            String sql = "SELECT isbn FROM LIVRE ORDER BY isbn";
            Statement st = connexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                livres.add(new Livre(isbn));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des livres : " + e.getMessage());
        }
        return livres;
    }

    /**
     * Recommande des livres au client en fonction des achats de clients similaires.
     * @return liste des titres recommandés
     */
    public List<String> onVousRecommande() {
        List<String> recommandations = new ArrayList<>();
        try {
            // 1. Récupérer les ISBN achetés par ce client
            List<String> livresClient = new ArrayList<>();
            String livresClientSQL = "SELECT DISTINCT d.isbn FROM DETAILCOMMANDE d " +
                                      "JOIN COMMANDE c ON d.numcom = c.numcom WHERE c.idcli = ?";
            try (PreparedStatement ps = this.connexion.getConnection().prepareStatement(livresClientSQL)) {
                ps.setInt(1, this.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        livresClient.add(rs.getString("isbn"));
                    }
                }
            }
            if (livresClient.isEmpty()) return recommandations;

            // 2. Trouver les autres clients ayant au moins 3 livres en commun
            String inLivres = String.join(",", livresClient.stream().map(isbn -> "'" + isbn + "'").toArray(String[]::new));
            String clientsSimilairesSQL =
                "SELECT c.idcli FROM DETAILCOMMANDE d " +
                "JOIN COMMANDE c ON d.numcom = c.numcom " +
                "WHERE d.isbn IN (" + inLivres + ") AND c.idcli != ? " +
                "GROUP BY c.idcli HAVING COUNT(DISTINCT d.isbn) >= 3";
            List<Integer> clientsSimilaires = new ArrayList<>();
            try (PreparedStatement ps = this.connexion.getConnection().prepareStatement(clientsSimilairesSQL)) {
                ps.setInt(1, this.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        clientsSimilaires.add(rs.getInt("idcli"));
                    }
                }
            }
            if (clientsSimilaires.isEmpty()) return recommandations;

            // 3. Récupérer les livres achetés par ces clients mais pas par ce client
            String inClients = String.join(",", clientsSimilaires.stream().map(String::valueOf).toArray(String[]::new));
            String autresLivresSQL =
                "SELECT DISTINCT d.isbn FROM DETAILCOMMANDE d " +
                "JOIN COMMANDE c ON d.numcom = c.numcom " +
                "WHERE c.idcli IN (" + inClients + ") " +
                "AND d.isbn NOT IN (" + inLivres + ")";
            List<String> livresARecommander = new ArrayList<>();
            try (Statement s = this.connexion.getConnection().createStatement();
                 ResultSet rs = s.executeQuery(autresLivresSQL)) {
                while (rs.next()) {
                    livresARecommander.add(rs.getString("isbn"));
                }
            }
            if (livresARecommander.isEmpty()) return recommandations;

            // 4. Récupérer les titres de ces livres
            String inLivresRec = String.join(",", livresARecommander.stream().map(isbn -> "'" + isbn + "'").toArray(String[]::new));
            String titresSQL = "SELECT titre FROM LIVRE WHERE isbn IN (" + inLivresRec + ")";
            try (Statement s = this.connexion.getConnection().createStatement();
                 ResultSet rs = s.executeQuery(titresSQL)) {
                while (rs.next()) {
                    String titre = rs.getString("titre");
                    if (titre != null && !recommandations.contains(titre)) {
                        recommandations.add(titre);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la génération des recommandations : " + e.getMessage());
        }
        return recommandations;
    }
}