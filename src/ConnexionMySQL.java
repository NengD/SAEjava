import java.sql.*;

public class ConnexionMySQL {

    private Connection mysql = null;
    private boolean connecte = false;

    public ConnexionMySQL() throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
    }

    public void connecter() throws SQLException {
        this.mysql = DriverManager.getConnection("jdbc:mysql://servinfo-maria:3306/DBmoreau", "moreau", "moreau");
        this.connecte = this.mysql != null;
    }

    public void close() throws SQLException {
        this.connecte = false;
    }

    public boolean isConnecte() {
        return this.connecte;
    }

    public Statement createStatement() throws SQLException {
        return this.mysql.createStatement();
    }

    public PreparedStatement prepareStatement(String requete) throws SQLException {
        return this.mysql.prepareStatement(requete);
    }

}
