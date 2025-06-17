import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Platform;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class MenuConnexion extends Application {
    // Déclaration des composants en attributs de classe
    private ConnexionMySQL connexionSQL;
    private ComboBox<String> typeCompte;
    private TextField idField;
    private PasswordField mdpField;
    private Button connexionBtn;
    private Button quitter;

    @Override
    public void init() {
        try {
            this.connexionSQL = new ConnexionMySQL();
            this.connexionSQL.connecter();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion à la base : " + e.getMessage());
        }
        // Initialisation des composants (sans les ajouter à la scène)
        this.typeCompte = new ComboBox<>();
        this.typeCompte.getItems().addAll("Client", "Administrateur", "Vendeur");
        this.typeCompte.setPrefWidth(200);

        this.idField = new TextField();
        this.idField.setPrefWidth(200);

        this.mdpField = new PasswordField();
        this.mdpField.setPrefWidth(200);

        this.connexionBtn = new Button("Connexion");
        this.connexionBtn.setOnAction(new ControleurBoutonConnexion(this.connexionSQL, this));

        this.quitter = new Button("Quitter");
        this.quitter.setOnAction(e -> {
            javafx.application.Platform.exit();
        });
    }

    @Override
    public void start(Stage stage) {
        // Labels
        Label typeLabel = new Label("Type de compte");
        Label idLabel = new Label("ID");
        Label mdpLabel = new Label("Mot de passe:");

        // Layouts pour organiser les éléments
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        grid.setPadding(new Insets(40, 40, 40, 40));

        // Placement des éléments
        grid.add(typeLabel, 0, 0);
        grid.add(typeCompte, 1, 0);

        grid.add(idLabel, 0, 1);
        grid.add(idField, 1, 1);

        grid.add(mdpLabel, 0, 2);
        grid.add(mdpField, 1, 2);

        grid.add(this.connexionBtn, 1, 3);
        grid.add(this.quitter, 1, 4);

        // Centrage du contenu
        BorderPane root = new BorderPane();
        root.setCenter(grid);

        // Style du fond
        BackgroundFill background = new BackgroundFill(Color.web("d2d1ad"), null, null);
        Background backgroundTitre = new Background(background);
        root.setBackground(backgroundTitre);

        Scene scene = new Scene(root, 600, 350);
        stage.setTitle("Connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void afficheMenuClient() {
        Stage stage = (Stage) this.connexionBtn.getScene().getWindow();
        stage.close();

        String id = this.idField.getText();
        Client client = getClientFromDB(id);
        if (client == null) {
            System.out.println("Client introuvable.");
            return;
        }
        MenuClient menuClient = new MenuClient();
        menuClient.init();
        menuClient.setContext(this.connexionSQL, client);
        Stage stageClient = new Stage();
        try {
            menuClient.start(stageClient);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du menu client.");
            e.printStackTrace();
        }
    }

    public void afficheMenuVendeur() {
        Stage stage = (Stage) this.connexionBtn.getScene().getWindow();
        stage.close();

        String id = this.idField.getText();
        Vendeur vendeur = getVendeurFromDB(id);
        if (vendeur == null) {
            System.out.println("Vendeur introuvable.");
            return;
        }
        MenuVendeur menuVendeur = new MenuVendeur();
        menuVendeur.init();
        menuVendeur.setContext(this.connexionSQL, vendeur);
        Stage stageVendeur = new Stage();
        try {
            menuVendeur.start(stageVendeur);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du menu vendeur.");
            e.printStackTrace();
        }
    }

    public void afficheMenuAdmin() {
        Stage stage = (Stage) this.connexionBtn.getScene().getWindow();
        stage.close();

        String id = this.idField.getText();
        Administrateur admin = getAdminFromDB(id);
        if (admin == null) {
            System.out.println("Administrateur introuvable.");
            return;
        }
        MenuAdministrateur menuAdmin = new MenuAdministrateur();
        menuAdmin.init();
        menuAdmin.setContexte(this.connexionSQL, admin);
        Stage stageAdmin = new Stage();
        try {
            menuAdmin.start(stageAdmin);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du menu administrateur.");
            e.printStackTrace();
        }
    }

    public String getTypeCompte() {
        return this.typeCompte.getValue();
    }

    public Client getClientFromDB(String idClient) {
        try {
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                    "SELECT idcli FROM CLIENT WHERE idcli = ?");
            ps.setInt(1, Integer.parseInt(idClient));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idcli = rs.getInt("idcli");
                rs.close();
                ps.close();
                return new Client(idcli, this.connexionSQL);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
        }
        return null;
    }

    public Vendeur getVendeurFromDB(String idVendeur) {
        try {
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                    "SELECT idven, idmag FROM VENDEUR WHERE idven = ?");
            ps.setInt(1, Integer.parseInt(idVendeur));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idven = rs.getInt("idven");
                int idmag = rs.getInt("idmag");
                rs.close();
                ps.close();
                return new Vendeur(idven, this.connexionSQL, idmag);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du vendeur : " + e.getMessage());
        }
        return null;
    }

    public Administrateur getAdminFromDB(String idAdmin) {
        try {
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                    "SELECT idadm FROM ADMINISTRATEUR WHERE idadm = ?");
            ps.setInt(1, Integer.parseInt(idAdmin));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idadm = rs.getInt("idadm");
                rs.close();
                ps.close();
                return new Administrateur(idadm, this.connexionSQL);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de l'administrateur : " + e.getMessage());
        }
        return null;
    }
}
