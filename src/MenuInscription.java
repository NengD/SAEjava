import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuInscription extends Application {

    private ConnexionMySQL connexionSQL;
    private BorderPane root;
    private Button boutonMaison;
    private Button boutonInfo;

    public BorderPane getRoot() {
        return this.root;
    }

    @Override
    public void init() {
        try {
            this.connexionSQL = new ConnexionMySQL();
            this.connexionSQL.connecter();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion à la base : " + e.getMessage());
        }
        this.boutonMaison = new Button();
        this.boutonMaison.setId("maison");
        Image imgMaison = new Image("file:./img/home.png");
        ImageView viewMaison = new ImageView(imgMaison);
        viewMaison.setFitWidth(32);
        viewMaison.setFitHeight(32);
        this.boutonMaison.setGraphic(viewMaison);

        this.boutonInfo = new Button();
        this.boutonInfo.setId("info");
        Image imgInfo = new Image("file:./img/info.png");
        ImageView viewInfo = new ImageView(imgInfo);
        viewInfo.setFitWidth(32);
        viewInfo.setFitHeight(32);
        this.boutonInfo.setGraphic(viewInfo);

        ControleurBoutonInscription controleur = new ControleurBoutonInscription(this);
        this.boutonMaison.setOnAction(controleur);
        this.boutonInfo.setOnAction(controleur);
    }

    private Scene lascene() {
        root = new BorderPane();
        root.setTop(titre());
        root.setCenter(pageInscription());
        return new Scene(root, 600, 400);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Livre Express - Inscription");
        stage.setScene(lascene());
        stage.show();
    }

    public Pane titre() {
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(0, 10, 0, 10));
        BackgroundFill bgFill = new BackgroundFill(Color.web("#bec3b9"), null, null);
        Background bgTitre = new Background(bgFill);
        banniere.setBackground(bgTitre);
        Text txtTitre = new Text("Inscription Client");
        txtTitre.setFont(Font.font("Arial", 50));
        HBox boiteTitre = new HBox();
        boiteTitre.setSpacing(10);
        boiteTitre.setAlignment(Pos.CENTER);
        boiteTitre.getChildren().addAll(this.boutonMaison, this.boutonInfo);
        banniere.setRight(boiteTitre);
        banniere.setLeft(txtTitre);
        return banniere;
    }

    public BorderPane pageInscription() {
        BorderPane res = new BorderPane();

        Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(
            fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        Background wpp = new Background(backgroundImage);
        res.setBackground(wpp);

        TextField nomC = new TextField();
        nomC.setPromptText("Nom de famille");
        TextField prenomC = new TextField();
        prenomC.setPromptText("Prénom");
        TextField adress = new TextField();
        adress.setPromptText("Adresse");
        TextField codePostal = new TextField();
        codePostal.setPromptText("Code postal");
        TextField ville = new TextField();
        ville.setPromptText("Ville");
        PasswordField mdp = new PasswordField();
        mdp.setPromptText("Mot de passe");

        VBox centre = new VBox();
        centre.setPadding(new Insets(10));
        centre.setAlignment(Pos.CENTER);
        centre.setSpacing(10);
        centre.getChildren().addAll(nomC, prenomC, adress, codePostal, ville, mdp);

        Button btnSInscrire = new Button("S'inscrire");
        btnSInscrire.setOnAction(event -> {
            String nom = nomC.getText();
            String prenom = prenomC.getText();
            String adresse = adress.getText();
            String cp = codePostal.getText();
            String villeStr = ville.getText();
            String motDePasse = mdp.getText();
            if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || cp.isEmpty() || villeStr.isEmpty() || motDePasse.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setPrefHeight(400);
                alert.setTitle("Information");
                alert.setContentText("Veuillez remplir tous les champs avant de vous inscrire.");
                alert.showAndWait();
                return;
            }
            String sql = "INSERT INTO CLIENT (idcli, nomcli, prenomcli, adressecli, codepostal, villecli, mdp) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connexionSQL.prepareStatement(sql)) {
                int id = -1;
                PreparedStatement psId = connexionSQL.prepareStatement(
                    "SELECT COALESCE(MAX(idcli), 0) + 1 AS nextId FROM CLIENT");
                ResultSet rsId = psId.executeQuery();
                if (rsId.next()) {
                    id = rsId.getInt("nextId");
                }
                rsId.close();
                psId.close();
                stmt.setInt(1, id);
                stmt.setString(2, nom);
                stmt.setString(3, prenom);
                stmt.setString(4, adresse);
                stmt.setString(5, cp);
                stmt.setString(6, villeStr);
                stmt.setString(7, motDePasse);
                stmt.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setPrefHeight(400);
                alert.setTitle("Information");
                alert.setContentText("Inscription réussie !\n\n" +
                        "Vous pouvez maintenant vous connecter avec votre compte.\n" +
                        "Veuillez vous souvenir de vos identifiants et de votre ID qui est " + id + "\n\n" +
                        "Si vous avez des questions, n'hésitez pas à nous contacter.\n\n" +
                        "Merci de votre confiance !");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setPrefHeight(400);
                alert.setTitle("Information");
                alert.setContentText("Erreur lors de l'inscription : " + e.getMessage() + "\n\n" +
                        "Veuillez vérifier vos informations et réessayer.\n\n" +
                        "Si le problème persiste, veuillez contacter notre support.");
                alert.showAndWait();
            }
        });
        centre.getChildren().add(btnSInscrire);
        res.setCenter(centre);
        return res;
    }

    public Alert infoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setPrefWidth(400);
        alert.getDialogPane().setPrefHeight(400);
        alert.setTitle("Information");
        alert.setHeaderText("Aide");
        alert.setContentText("Bienvenue dans le menu inscription de Livre Express !\n\n" +
                "Vous pouvez créer votre compte client.\n\n" +
                "Pour toute assistance, veuillez contacter notre support.");
        return alert;
    }

    public static void main(String[] args) {
        launch(args);
    }
}