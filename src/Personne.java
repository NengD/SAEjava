
public abstract  class Personne {
    protected String nom;
    protected String prenom;
    protected ConnexionMySQL connexion;

    public Personne(String nom, String prenom, ConnexionMySQL connexion) {
        this.nom = nom;
        this.prenom = prenom;
        this.connexion = connexion;
    }

    

}
