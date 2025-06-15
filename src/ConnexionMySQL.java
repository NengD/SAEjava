import java.sql.*;

public class ConnexionMySQL {

    private Connection mysql = null;
    private boolean connecte = false;

    /**
    * Constructeur de la classe ConnexionMySQL.
    * Charge le driver MariaDB pour la connexion à la base de données.
    * @throws ClassNotFoundException si le driver n'est pas trouvé
    */
    public ConnexionMySQL() throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
    }

    /**
    * Établit une connexion à la base de données MySQL.
    * Définit l'état de connexion à true si la connexion est réussie.
    * @throws SQLException si une erreur se produit lors de la connexion
    */
    public void connecter() throws SQLException {
        this.mysql = DriverManager.getConnection("jdbc:mysql://servinfo-maria:3306/DBmoreau", "moreau", "moreau");
        this.connecte = this.mysql != null;
    }

    /**
    * Ferme la connexion à la base de données MySQL.
    * @throws SQLException si une erreur se produit lors de la fermeture de la connexion
    */
    public void close() throws SQLException {
        this.connecte = false;
    }
    /**
    * Vérifie si la connexion à la base de données est établie.
    * @return true si la connexion est active, false sinon
    */
    public boolean isConnecte() {
        return this.connecte;
    }
    /**
    * Crée une déclaration SQL pour exécuter des requêtes.
    * @return un objet Statement pour exécuter des requêtes SQL
    * @throws SQLException si une erreur se produit lors de la création de la déclaration
    */
    public Statement createStatement() throws SQLException {
        return this.mysql.createStatement();
    }
    /**
    * Prépare une requête SQL pour exécution.
    * @param requete la requête SQL à préparer
    * @return un objet PreparedStatement pour exécuter la requête préparée
    * @throws SQLException si une erreur se produit lors de la préparation de la requête
    */
    public PreparedStatement prepareStatement(String requete) throws SQLException {
        return this.mysql.prepareStatement(requete);
    }

    /**
    * Récupère la connexion MySQL.
    * @return l'objet Connection représentant la connexion à la base de données
    */
    public Connection getConnection() {
    return this.mysql;
    }

}
