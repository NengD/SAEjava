public class Administrateur extends Personne {


    public Administrateur(String nom, String prenom, String login, String motDePasse) {
        super(nom, prenom);
    }

    public void creerVendeur(String nom,String prenom,Magasin magasin) {
        Vendeur vendeur = new Vendeur(nom, prenom);
        magasin.ajouterVendeur(vendeur);
    }

    public void ajouterLibrairie(Magasin librairie) {
    }

    //public String consulterStatistique(){}
}
