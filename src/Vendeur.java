import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Vendeur {

    private int idVendeur;
    private int idmagasin;
    private ConnexionMySQL connexion;

    public Vendeur(int idVendeur, ConnexionMySQL connexion,int idmagasin) {
        this.connexion = connexion;
        this.idVendeur = idVendeur;
        this.idmagasin = idmagasin;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public void setIdVendeur(int idVendeur) {
        this.idVendeur = idVendeur;
    }

    public int getIdMagasin() {
        return this.idmagasin;
    }

    public void ajouterLivre(String idLivre, int quantite){
        try {
            PreparedStatement ps = this.connexion.prepareStatement(
                "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
            );
            ps.setInt(1, this.idmagasin);
            ps.setString(2, idLivre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int nouvelleQte = rs.getInt("qte") + quantite;
                PreparedStatement psUpdate = this.connexion.prepareStatement(
                    "UPDATE POSSEDER SET qte = ? WHERE idmag = ? AND isbn = ?"
                );
                psUpdate.setInt(1, nouvelleQte);
                psUpdate.setInt(2, this.idmagasin);
                psUpdate.setString(3, idLivre);
                psUpdate.executeUpdate();
                psUpdate.close();
            } else {
                PreparedStatement psInsert = this.connexion.prepareStatement(
                    "INSERT INTO POSSEDER (idmag, isbn, qte) VALUES (?, ?, ?)"
                );
                psInsert.setInt(1, this.idmagasin);
                psInsert.setString(2, idLivre);
                psInsert.setInt(3, quantite);
                psInsert.executeUpdate();
                psInsert.close();
            }
            rs.close();
            ps.close();
            System.out.println("Livre ajouté au stock.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    public void majQuantiteLivre(Livre livre, int quantite){
        try {
            PreparedStatement ps = this.connexion.prepareStatement(
                "UPDATE POSSEDER SET qte = qte + ? WHERE idmag = ? AND isbn = ?"
            );
            ps.setInt(1, quantite);
            ps.setInt(2, this.idmagasin);
            ps.setString(3, livre.getIsbn());
            int rows = ps.executeUpdate();
            ps.close();
            if (rows == 0) {
                System.out.println("Livre non trouvé dans le stock.");
            } else {
                System.out.println("Quantité mise à jour.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la quantité : " + e.getMessage());
        }
    }

    public boolean livreDisponible(String nomLivre){
        try {
            PreparedStatement ps = this.connexion.prepareStatement(
                "SELECT qte FROM POSSEDER p JOIN LIVRE l ON p.isbn = l.isbn WHERE p.idmag = ? AND l.titre = ?"
            );
            ps.setInt(1, this.idmagasin);
            ps.setString(2, nomLivre);
            ResultSet rs = ps.executeQuery();
            boolean disponible = false;
            if (rs.next()) {
                disponible = rs.getInt("qte") > 0;
            }
            rs.close();
            ps.close();
            return disponible;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de disponibilité : " + e.getMessage());
            return false;
        }
    }

    public void transfertLivre(String isbn, int quantite, int idMagasinDest){
        try {
            int idMagOrig = this.idmagasin;
            int idMagDest = idMagasinDest;

            // Vérifier la quantité disponible dans le magasin d'origine
            PreparedStatement psCheck = this.connexion.prepareStatement(
                "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
            );
            psCheck.setInt(1, idMagOrig);
            psCheck.setString(2, isbn);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt("qte") >= quantite) {
                // Retirer la quantité du magasin d'origine
                PreparedStatement psUpdateOrig = this.connexion.prepareStatement(
                    "UPDATE POSSEDER SET qte = qte - ? WHERE idmag = ? AND isbn = ?"
                );
                psUpdateOrig.setInt(1, quantite);
                psUpdateOrig.setInt(2, idMagOrig);
                psUpdateOrig.setString(3, isbn);
                psUpdateOrig.executeUpdate();
                psUpdateOrig.close();

                // Ajouter ou mettre à jour la quantité dans le magasin de destination
                PreparedStatement psCheckDest = this.connexion.prepareStatement(
                    "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
                );
                psCheckDest.setInt(1, idMagDest);
                psCheckDest.setString(2, isbn);
                ResultSet rsDest = psCheckDest.executeQuery();
                if (rsDest.next()) {
                    PreparedStatement psUpdateDest = this.connexion.prepareStatement(
                        "UPDATE POSSEDER SET qte = qte + ? WHERE idmag = ? AND isbn = ?"
                    );
                    psUpdateDest.setInt(1, quantite);
                    psUpdateDest.setInt(2, idMagDest);
                    psUpdateDest.setString(3, isbn);
                    psUpdateDest.executeUpdate();
                    psUpdateDest.close();
                } else {
                    PreparedStatement psInsertDest = this.connexion.prepareStatement(
                        "INSERT INTO POSSEDER (idmag, isbn, qte) VALUES (?, ?, ?)"
                    );
                    psInsertDest.setInt(1, idMagDest);
                    psInsertDest.setString(2, isbn);
                    psInsertDest.setInt(3, quantite);
                    psInsertDest.executeUpdate();
                    psInsertDest.close();
                }
                rsDest.close();
                psCheckDest.close();
                System.out.println("Transfert effectué.");
            } else {
                System.out.println("Quantité insuffisante dans le stock du magasin d'origine.");
            }
            rs.close();
            psCheck.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors du transfert : " + e.getMessage());
        }
    }

    public void passerCommandePourClient(boolean enLigne, String typeLivraison, List<Livre> livres, Client client){
        char typeLiv = typeLivraison.charAt(0);
        client.passerCommande(enLigne, typeLiv, livres, this.idmagasin);
        System.out.println("Commande passée pour le client ");
    }
}