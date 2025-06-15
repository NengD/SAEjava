import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe représentant un vendeur de la librairie.
 * Permet la gestion du stock, des transferts de livres et la prise de commandes pour les clients.
 */
public class Vendeur {

    private int idVendeur;
    private int idmagasin;
    private ConnexionMySQL connexion;

    /**
     * Constructeur Vendeur.
     * @param idVendeur identifiant du vendeur
     * @param connexion connexion à la base de données
     * @param idmagasin identifiant du magasin associé
     */
    public Vendeur(int idVendeur, ConnexionMySQL connexion, int idmagasin) {
        this.connexion = connexion;
        this.idVendeur = idVendeur;
        this.idmagasin = idmagasin;
    }

    /** @return l'identifiant du vendeur */
    public int getIdVendeur() {
        return idVendeur;
    }

    /** @param idVendeur nouvel identifiant du vendeur */
    public void setIdVendeur(int idVendeur) {
        this.idVendeur = idVendeur;
    }

    /** @return l'identifiant du magasin associé */
    public int getIdMagasin() {
        return this.idmagasin;
    }

    /**
     * Ajoute un livre au stock du magasin.
     * Si le livre existe déjà, augmente la quantité.
     * @param idLivre ISBN du livre
     * @param quantite quantité à ajouter
     */
    public void ajouterLivre(String idLivre, int quantite) {
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

    /**
     * Met à jour la quantité d'un livre dans le stock du magasin.
     * @param livre livre concerné
     * @param quantite quantité à ajouter (peut être négative)
     */
    public void majQuantiteLivre(Livre livre, int quantite) {
        try {
            PreparedStatement ps = this.connexion.prepareStatement(
                "UPDATE POSSEDER SET qte = qte + ? WHERE idmag = ? AND isbn = ?"
            );
            ps.setInt(1, quantite);
            ps.setInt(2, this.idmagasin);
            ps.setString(3, livre.getIsbn());
            int nbLigne = ps.executeUpdate();
            ps.close();
            if (nbLigne == 0) {
                System.out.println("Livre non trouvé dans le stock.");
            } else {
                System.out.println("Quantité mise à jour.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la quantité : " + e.getMessage());
        }
    }

    /**
     * Vérifie si un livre est disponible dans le stock du magasin.
     * @param nomLivre titre du livre
     * @return true si disponible, false sinon
     */
    public boolean livreDisponible(String nomLivre) {
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

    /**
     * Transfère une quantité de livre du magasin courant vers un autre magasin.
     * @param isbn ISBN du livre
     * @param quantite quantité à transférer
     * @param idMagasinDest identifiant du magasin de destination
     */
    public void transfertLivre(String isbn, int quantite, int idMagasinDest) {
        try {
            int idMagOrig = this.idmagasin;

            // Vérifier la quantité disponible dans le magasin d'origine
            PreparedStatement psVerif = this.connexion.prepareStatement(
                "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
            );
            psVerif.setInt(1, idMagOrig);
            psVerif.setString(2, isbn);
            ResultSet rs = psVerif.executeQuery();
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
                PreparedStatement psVerifDest = this.connexion.prepareStatement(
                    "SELECT qte FROM POSSEDER WHERE idmag = ? AND isbn = ?"
                );
                psVerifDest.setInt(1, idMagasinDest);
                psVerifDest.setString(2, isbn);
                ResultSet rsDest = psVerifDest.executeQuery();
                if (rsDest.next()) {
                    PreparedStatement psUpdateDest = this.connexion.prepareStatement(
                        "UPDATE POSSEDER SET qte = qte + ? WHERE idmag = ? AND isbn = ?"
                    );
                    psUpdateDest.setInt(1, quantite);
                    psUpdateDest.setInt(2, idMagasinDest);
                    psUpdateDest.setString(3, isbn);
                    psUpdateDest.executeUpdate();
                    psUpdateDest.close();
                } else {
                    PreparedStatement psInsertDest = this.connexion.prepareStatement(
                        "INSERT INTO POSSEDER (idmag, isbn, qte) VALUES (?, ?, ?)"
                    );
                    psInsertDest.setInt(1, idMagasinDest);
                    psInsertDest.setString(2, isbn);
                    psInsertDest.setInt(3, quantite);
                    psInsertDest.executeUpdate();
                    psInsertDest.close();
                }
                rsDest.close();
                psVerifDest.close();
                System.out.println("Transfert effectué.");
            } else {
                System.out.println("Quantité insuffisante dans le stock du magasin d'origine.");
            }
            rs.close();
            psVerif.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors du transfert : " + e.getMessage());
        }
    }

    /**
     * Passe une commande pour un client en magasin.
     * @param enLigne true si la commande est en ligne, false sinon
     * @param typeLivraison type de livraison ('M', 'C')
     * @param livres liste des livres à commander
     * @param client client concerné
     */
    public void passerCommandePourClient(boolean enLigne, String typeLivraison, List<Livre> livres, Client client) {
        char typeLiv = typeLivraison.charAt(0);
        client.passerCommande(enLigne, typeLiv, livres, this.idmagasin);
        System.out.println("Commande passée pour le client.");
    }
}