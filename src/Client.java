
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client extends Personne {

    private String adresse;
    private List<Commande> commandes;
    private static int numClient = 0;

    public Client(String nom, String prenom, String adresse, ConnexionMySQL connexion) {
        super(nom, prenom, connexion);
        this.adresse = adresse;
        this.commandes = new ArrayList<>();
        numClient++;
    }

    public void passerCommande(boolean enLigne, char typeLivraison, List<Livre> livres, Magasin magasin) {
        Commande commande = new Commande(enLigne, typeLivraison, this);
        magasin.ajouteCommande(commande);
        try {
            PreparedStatement ps1 = this.connexion.prepareStatement("INSERT INTO COMMANDE (numcom, datecom, enligne, livraison, idcli, idmag) VALUES (?, ?, ?, ?, ?, ?)");
            ps1.setInt(1, commande.getNumCommande());
            ps1.setString(2, LocalDate.now().toString());
            if (enLigne) {
                ps1.setString(3, "O");
            } else {
                ps1.setString(3, "N");
            }
            ps1.setString(4, String.valueOf(typeLivraison));
            ps1.setInt(5, this.numClient);
            ps1.setString(6, magasin.getIdMagasin());
            ps1.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de la commande : " + e.getMessage());
        }

        Map<Livre, Integer> dictLivres = new HashMap<>();
        for (Livre livre : livres) {
            dictLivres.put(livre, dictLivres.getOrDefault(livre, 0) + 1);
        }

        int numlig = 1;
        for (Livre livre : dictLivres.keySet()) {
            try {
                PreparedStatement ps2 = connexion.prepareStatement(
                        "INSERT INTO DETAILCOMMANDE (numcom, numlig, isbn) VALUES (?, ?, ?)"
                );
                ps2.setInt(1, commande.getNumCommande());
                ps2.setInt(2, numlig);
                ps2.setString(3, livre.getIsbn().toString());
                ps2.executeUpdate();
                ps2.close();

                magasin.stock.majQuantiteLivre(livre, -dictLivres.get(livre));
                numlig++;
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'insertion du détail de la commande : " + e.getMessage());
            }
        }
    }

    public List<Livre> consulterCatalogue() {
        List<Livre> livres = new ArrayList<>();
        try {
            Statement s = this.connexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT l.isbn, l.titre, c.nomclass, l.prix FROM LIVRE l LEFT JOIN CLASSIFICATION c ON l.classification = c.iddewey");
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String titre = rs.getString("titre");
                String classification = rs.getString("nomclass");
                Double prix = rs.getDouble("prix");
                Livre livre = new Livre(isbn, titre, classification, prix, new ArrayList<>(), null);
                livres.add(livre);
            }
            rs.close();
            s.close();
            return livres;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation du catalogue : " + e.getMessage());
            return null;
        }
    }

    /*public List<String> onVousRecommande() {
        List<String> recommandations = new ArrayList<>();
        try {
            // 1. Récupérer les idLivre achetés par ce client
            String livresClientQuery = "SELECT idLivre FROM COMMANDE NATURAL JOIN DETAILCOMMANDE WHERE idClient = " + this.getId();
            List<Integer> livresClient = new ArrayList<>();
            try (Statement s = this.connexion.createStatement(); ResultSet rs = s.executeQuery(livresClientQuery)) {
                while (rs.next()) {
                    livresClient.add(rs.getInt("idLivre"));
                }
            }

            if (livresClient.isEmpty()) {
                return recommandations;
            }

            // 2. Trouver les autres clients ayant au moins 3 livres en commun
            String inLivres = String.join(",", livresClient.stream().map(String::valueOf).toArray(String[]::new));
            String clientsSimilairesQuery = "SELECT idClient FROM DETAILCOMMANDE WHERE idLivre IN (" + inLivres + ") AND idClient != " + this.getId()
                    + " GROUP BY idClient HAVING COUNT(DISTINCT idLivre) >= 3";
            List<Integer> clientsSimilaires = new ArrayList<>();
            try (Statement s = this.connexion.createStatement(); ResultSet rs = s.executeQuery(clientsSimilairesQuery)) {
                while (rs.next()) {
                    clientsSimilaires.add(rs.getInt("idClient"));
                }
            }

            if (clientsSimilaires.isEmpty()) {
                return recommandations;
            }

            // 3. Récupérer les livres achetés par ces clients mais pas par ce client
            String inClients = String.join(",", clientsSimilaires.stream().map(String::valueOf).toArray(String[]::new));
            String autresLivresQuery = "SELECT DISTINCT idLivre FROM DETAILCOMMANDE WHERE idClient IN (" + inClients + ") AND idLivre NOT IN (" + inLivres + ")";
            List<Integer> livresARecommander = new ArrayList<>();
            try (Statement s = this.connexion.createStatement(); ResultSet rs = s.executeQuery(autresLivresQuery)) {
                while (rs.next()) {
                    livresARecommander.add(rs.getInt("idLivre"));
                }
            }

            if (livresARecommander.isEmpty()) {
                return recommandations;
            }

            // 4. Récupérer les titres de ces livres
            String inLivresRec = String.join(",", livresARecommander.stream().map(String::valueOf).toArray(String[]::new));
            String titresQuery = "SELECT titre FROM LIVRE WHERE idLivre IN (" + inLivresRec + ")";
            try (Statement s = this.connexion.createStatement(); ResultSet rs = s.executeQuery(titresQuery)) {
                while (rs.next()) {
                    String titre = rs.getString("titre");
                    if (titre != null && !recommandations.contains(titre)) {
                        recommandations.add(titre);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommandations;
    }*/
}
