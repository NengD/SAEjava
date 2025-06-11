import java.sql.PreparedStatement;

public class Administrateur extends Personne {


    public Administrateur(String nom, String prenom, ConnexionMySQL connexion) {
        super(nom, prenom, connexion);
    }

    public void creerVendeur(String nom, String prenom, Magasin magasin) {
        Vendeur vendeur = new Vendeur(nom, prenom, this.connexion);
        magasin.ajouterVendeur(vendeur);
    }

    public void ajouterLibrairie(String nom, String ville) {
        try {
            Magasin villeMag = new Magasin(nom);
            String idmag = String.valueOf(villeMag.getIdMagasin());

            PreparedStatement ps = this.connexion.prepareStatement("INSERT INTO MAGASIN (idmag, nommag, villemag) VALUES (?, ?, ?)");
            ps.setString(1, idmag);
            ps.setString(2, nom);
            ps.setString(3, ville);
            ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Erreur lors de l'ajout de la librairie : " + e.getMessage());
        }
    }

    //public String consulterStatistique(){}
}
