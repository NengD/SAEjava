import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.scene.text.*;
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
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuConnexion extends Application {
    // Déclaration des composants en attributs de classe
    private ConnexionMySQL connexionSQL;
    private ComboBox<String> typeCompte;
    private TextField idField;
    private PasswordField mdpField;
    private Button connexionBtn;
    private Button quitter;
    private Button inscription;

    @Override
    public void init() {
        try {
            this.connexionSQL = new ConnexionMySQL();
            this.connexionSQL.connecter();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setPrefWidth(400);
            alert.getDialogPane().setPrefHeight(400);
            alert.setTitle("Information");
            alert.setHeaderText("Erreur de connexion à la base de données");
            alert.showAndWait();
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
            Platform.exit();
        });

        this.inscription = new Button("Inscription");
        this.inscription.setOnAction(new ControleurInscription(this));
    }

    public Pane titre() {
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(0, 10, 0, 10));
        BackgroundFill background = new BackgroundFill(Color.web("#bec3b9"), null, null);
        Background backgroundTitre = new Background(background);
        banniere.setBackground(backgroundTitre);
        Text titre = new Text("Menu Connexion");
        titre.setFont(Font.font("Arial", 50));
        HBox boiteTitre = new HBox();
        boiteTitre.setSpacing(10);
        boiteTitre.setAlignment(Pos.CENTER);
        banniere.setRight(boiteTitre);
        banniere.setLeft(titre);
        return banniere;
    }

    @Override
    public void start(Stage stage) {

        typeCompte.setPromptText("Type de compte");
        idField.setPromptText("Identifiant");
        mdpField.setPromptText("Mot de passe");

        // Layouts pour organiser les éléments
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        grid.setPadding(new Insets(40, 40, 40, 40));

        // Placement des éléments
        grid.add(typeCompte, 1, 0);
        grid.add(idField, 1, 1);
        grid.add(mdpField, 1, 2);

        grid.add(this.connexionBtn, 1, 3);
        grid.add(this.quitter, 1, 4);
        grid.add(this.inscription, 2, 0);

        // Centrage du contenu
        BorderPane root = new BorderPane();
        root.setCenter(grid);

        root.setTop(titre());

        // Style du fond
        Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        Background wpp = new Background(backgroundImage);
        root.setBackground(wpp);
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
        String id = this.idField.getText();
        Client client = getClientFromDB(id);
        if (client == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setPrefWidth(400);
            alert.getDialogPane().setPrefHeight(400);
            alert.setTitle("Erreur de Connexion");
            alert.setHeaderText("Impossible de se connecter au menu client");
            alert.setContentText("Le client demandé n'existe pas ou le mot de passe est incorrect.");
            alert.showAndWait();
        }

        else {
            stage.close();
            MenuClient menuClient = new MenuClient();
            menuClient.init();
            menuClient.setContext(this.connexionSQL, client);
            Stage stageClient = new Stage();
            try {
                menuClient.start(stageClient);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setPrefHeight(400);
                alert.setTitle("Erreur de Connexion");
                alert.setHeaderText("Impossible d'ouvrir le menu client");
                alert.showAndWait();
            }
        }
    }

    public void afficheMenuVendeur() {
        Stage stage = (Stage) this.connexionBtn.getScene().getWindow();

        String id = this.idField.getText();
        Vendeur vendeur = getVendeurFromDB(id);
        if (vendeur == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setPrefWidth(400);
            alert.getDialogPane().setPrefHeight(400);
            alert.setTitle("Erreur de Connexion");
            alert.setHeaderText("Impossible de se connecter au menu vendeur");
            alert.setContentText("Le vendeur demandé n'existe pas ou le mot de passe est incorrect.");
            alert.showAndWait();
        } else {
            stage.close();
            MenuVendeur menuVendeur = new MenuVendeur();
            menuVendeur.init();
            menuVendeur.setContext(this.connexionSQL, vendeur);
            Stage stageVendeur = new Stage();
            try {
                menuVendeur.start(stageVendeur);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setPrefHeight(400);
                alert.setTitle("Erreur de Connexion");
                alert.setHeaderText("Impossible d'ouvrir le menu vendeur");
                alert.showAndWait();
            }
        }
    }

    public void afficheMenuAdmin() {
        Stage stage = (Stage) this.connexionBtn.getScene().getWindow();

        String id = this.idField.getText();
        Administrateur admin = getAdminFromDB(id);
        if (admin == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setPrefWidth(400);
            alert.getDialogPane().setPrefHeight(400);
            alert.setTitle("Erreur de Connexion");
            alert.setHeaderText("Impossible de se connecter au menu administrateur");
            alert.setContentText("L'administrateur demandé n'existe pas ou le mot de passe est incorrect.");
            alert.showAndWait();
        } else {
            stage.close();
            MenuAdministrateur menuAdmin = new MenuAdministrateur();
            menuAdmin.init();
            menuAdmin.setContexte(this.connexionSQL, admin);
            Stage stageAdmin = new Stage();
            try {
                menuAdmin.start(stageAdmin);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setPrefHeight(400);
                alert.setTitle("Erreur de Connexion");
                alert.setHeaderText("Impossible d'ouvrir le menu administrateur");
                alert.showAndWait();
            }
        }
    }

    public String getTypeCompte() {
        return this.typeCompte.getValue();
    }

    public Client getClientFromDB(String idClient) {
        try {
            String mdp = this.mdpField.getText();
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                    "SELECT idcli FROM CLIENT WHERE idcli = ? AND mdp = ?");
            ps.setInt(1, Integer.parseInt(idClient));
            ps.setString(2, mdp);
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
        }
        return null;
    }

    public Vendeur getVendeurFromDB(String idVendeur) {
        try {
            String mdp = this.mdpField.getText();
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                    "SELECT idven, idmag FROM VENDEUR WHERE idven = ? AND mdp = ?");
            ps.setInt(1, Integer.parseInt(idVendeur));
            ps.setString(2, mdp);
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
        }
        return null;
    }

    public Administrateur getAdminFromDB(String idAdmin) {
        try {
            String mdp = this.mdpField.getText();
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                    "SELECT idadm FROM ADMINISTRATEUR WHERE idadm = ? AND mdp = ?");
            ps.setInt(1, Integer.parseInt(idAdmin));
            ps.setString(2, mdp);
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
        }
        return null;
    }
}
