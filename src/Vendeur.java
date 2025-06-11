import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Vendeur extends Personne {

    private Magasin magasin;

    public Vendeur(String nom, String prenom, ConnexionMySQL connexion) {
        super(nom, prenom, connexion);
    }

    public void ajouterLivre(Livre livre, int quantite){
        try {
            PreparedStatement ps = this.connexion.prepareStatement("SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?");
            ps.setString(1, this.magasin.getIdMagasin());
            ps.setString(2, livre.getIsbn());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int nouvelleQte = rs.getInt("qte") + quantite;
                PreparedStatement psUpdate = this.connexion.prepareStatement("UPDATE POSSEDER SET qte = ? WHERE idmag = ? AND isbn = ?");
                psUpdate.setInt(1, nouvelleQte);
                psUpdate.setString(2, this.magasin.getIdMagasin());
                psUpdate.setString(3, livre.getIsbn());
                psUpdate.executeUpdate();
                psUpdate.close();
            } else {
                PreparedStatement psInsert = this.connexion.prepareStatement("INSERT INTO POSSEDER (idmag, isbn, qte) VALUES (?, ?, ?)");
                psInsert.setString(1, this.magasin.getIdMagasin());
                psInsert.setString(2, livre.getIsbn());
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
            ps.setString(2, this.magasin.getIdMagasin());
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
            ps.setString(1, this.magasin.getIdMagasin());
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

    public void transfertLivre(Livre livre, int quantite, Magasin magasinDest){
        try {
            PreparedStatement psCheck = this.connexion.prepareStatement(
                "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
            );
            psCheck.setString(1, this.magasin.getIdMagasin());
            psCheck.setString(2, livre.getIsbn());
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt("qte") >= quantite) {
                PreparedStatement psUpdateOrig = this.connexion.prepareStatement(
                    "UPDATE POSSEDER SET qte = qte - ? WHERE idmag = ? AND isbn = ?"
                );
                psUpdateOrig.setInt(1, quantite);
                psUpdateOrig.setString(2, this.magasin.getIdMagasin());
                psUpdateOrig.setString(3, livre.getIsbn());
                psUpdateOrig.executeUpdate();
                psUpdateOrig.close();

                PreparedStatement psCheckDest = this.connexion.prepareStatement(
                    "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
                );
                psCheckDest.setString(1, magasinDest.getIdMagasin());
                psCheckDest.setString(2, livre.getIsbn());
                ResultSet rsDest = psCheckDest.executeQuery();
                if (rsDest.next()) {
                    PreparedStatement psUpdateDest = this.connexion.prepareStatement(
                        "UPDATE POSSEDER SET qte = qte + ? WHERE idmag = ? AND isbn = ?"
                    );
                    psUpdateDest.setInt(1, quantite);
                    psUpdateDest.setString(2, magasinDest.getIdMagasin());
                    psUpdateDest.setString(3, livre.getIsbn());
                    psUpdateDest.executeUpdate();
                    psUpdateDest.close();
                } else {
                    PreparedStatement psInsertDest = this.connexion.prepareStatement(
                        "INSERT INTO POSSEDER (idmag, isbn, qte) VALUES (?, ?, ?)"
                    );
                    psInsertDest.setString(1, magasinDest.getIdMagasin());
                    psInsertDest.setString(2, livre.getIsbn());
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

    public void passerCommandePourClient(boolean enLigne,String typeLivraison,List<Livre> livres,Client client){

    }
}
