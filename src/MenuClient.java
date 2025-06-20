import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.animation.FadeTransition;
import javafx.util.Duration;

import javafx.scene.control.ComboBox;




public class MenuClient extends Application {
    private ConnexionMySQL connexionSQL;
    private Client client;
    private Button btnCatalogue;
    private Button btnRecommande;
    private Button btnCommande;
    private Button btnRetour;
    private Button boutonMaison;
    private Button boutonInfo;
    private BorderPane root;

    public void setContext(ConnexionMySQL connexionSQL, Client client) {
        this.connexionSQL = connexionSQL;
        this.client = client;
    }

    public BorderPane getRoot() {
        return this.root;
    }

    public void fadeOut(Button boutton, Runnable action) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), boutton);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> {
            boutton.setOpacity(1.0);
            action.run();
        });
        ft.play();
    }
    
    @Override
    public void init(){
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
        this.btnCatalogue = new Button("Consulter Catalogue");
        this.btnCommande = new Button("Passer une Commande");
        this.btnRecommande = new Button("On vous recommande");
        this.btnRetour = new Button("Retour");
        ControleurBoutonClient controleur = new ControleurBoutonClient(this);
        this.btnRetour.setOnAction(controleur);
        this.boutonInfo.setOnAction(controleur);
        this.btnCatalogue.setOnAction(controleur);
        this.btnCommande.setOnAction(controleur);
        this.btnRecommande.setOnAction(controleur);
        this.boutonMaison.setOnAction(controleur);
    }

    private Scene lascene(){
        root = new BorderPane();
        root.setTop(titre());
        root.setCenter(pageMenu());
        return new Scene(root, 600, 400);  
    }

    
    public void start(Stage stage) {
        stage.setTitle("Livre Express - Menu Client");
        stage.setScene(lascene());
        stage.show();
    }

    public Pane titre(){
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(0, 10, 0, 10));
        BackgroundFill background = new BackgroundFill(Color.web("#bec3b9"), null, null);
        Background backgroundTitre = new Background(background);
        banniere.setBackground(backgroundTitre);
        Text titre = new Text("Menu Client");
        titre.setFont(Font.font("Arial", 50));
        HBox boiteTitre = new HBox();
        boiteTitre.setSpacing(10);
        boiteTitre.setAlignment(Pos.CENTER);
        boiteTitre.getChildren().addAll(this.boutonMaison,this.boutonInfo);
        banniere.setRight(boiteTitre);
        banniere.setLeft(titre);
        return banniere;
    }

    public BorderPane pageMenu() {
        BorderPane res = new BorderPane();

        Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
        Background wpp = new Background(backgroundImage);
        res.setBackground(wpp);

        this.btnCatalogue.setPrefWidth(200);
        this.btnCommande.setPrefWidth(200);
        this.btnRecommande.setPrefWidth(200);

        VBox vbox = new VBox(20, this.btnCatalogue, this.btnCommande, this.btnRecommande);
        vbox.setAlignment(Pos.CENTER);

        res.setCenter(vbox);
        return res;
    }

    public BorderPane pageCatalogue() {
        BorderPane res = new BorderPane();

        Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
        Background wpp = new Background(backgroundImage);
        res.setBackground(wpp);
        
        VBox vboxCatalogue = new VBox();
        vboxCatalogue.setAlignment(Pos.CENTER);

        StringBuilder catalogue = new StringBuilder();
        for (Livre l : Client.consulterCatalogue(this.connexionSQL.getConnection())) {
            String isbn = l.getIsbn();
            String titre = l.getTitre(this.connexionSQL.getConnection());
            double prix = l.getPrix(this.connexionSQL.getConnection());
            String ligne = String.format("%-15s  %-100s  %8.2f €\n", isbn, titre, prix);
            catalogue.append(ligne);
        }
        TextArea textCatalogue = new TextArea(catalogue.toString());
        textCatalogue.setEditable(false);
        textCatalogue.setWrapText(false);
        textCatalogue.setStyle("-fx-font-family: 'monospaced'; -fx-control-inner-background: #bec3b9; -fx-background-color: #bec3b9; -fx-text-fill: black;");
        textCatalogue.setMaxWidth(Double.MAX_VALUE);
        textCatalogue.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(textCatalogue, Priority.ALWAYS);
        vboxCatalogue.getChildren().add(textCatalogue);

        textCatalogue.setPrefColumnCount(40);
        textCatalogue.setPrefRowCount(catalogue.toString().split("\n").length);

        VBox vbox = new VBox(15, btnRetour, textCatalogue);

        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        
        res.setCenter(vbox);
        return res;
    }


    public BorderPane pagePasserCommande() {
        BorderPane res = new BorderPane();

        Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
        Background wpp = new Background(backgroundImage);
        res.setBackground(wpp);

        ComboBox<String> CbEnLigne = new ComboBox<>();
        CbEnLigne.getItems().addAll("true", "false");
        CbEnLigne.setPrefWidth(500);
        CbEnLigne.setPromptText("Êtes-vous en ligne ? (true/false)");
        ComboBox<String> cbTypeLivraison = new ComboBox<>();
        cbTypeLivraison.getItems().addAll("M", "C");
        cbTypeLivraison.setPrefWidth(500);
        cbTypeLivraison.setPromptText("Type de livraison (M pour magasin, C pour colis)");
        TextField tfLivres = new TextField();
        tfLivres.setPromptText("Liste des titres des livres à commander (séparés par ,)");
        tfLivres.setPrefWidth(500);
        TextField tfIdMagasin = new TextField();
        tfIdMagasin.setPromptText("ID magasin");
        tfIdMagasin.setPrefWidth(500);
        Button btnValider = new Button("Valider la commande");
        btnValider.setOnAction(e -> {
            try {
                String enLigne = CbEnLigne.getValue();
                if (!enLigne.equalsIgnoreCase("true") && !enLigne.equalsIgnoreCase("false")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.getDialogPane().setPrefWidth(400);
                    alert.getDialogPane().setPrefHeight(400);
                    alert.setTitle("Information");
                    alert.setContentText("Veuillez sélectionnez 'true' ou 'false' pour l'état en ligne.");
                    alert.showAndWait();
                    return;
                }
                boolean isEnLigne = "true".equals(CbEnLigne.getValue());
                String typeLivraison = cbTypeLivraison.getValue();
                if (typeLivraison.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.getDialogPane().setPrefWidth(400);
                    alert.getDialogPane().setPrefHeight(400);
                    alert.setTitle("Information");
                    alert.setContentText("Veuillez sélectionnez le type de livraison (M pour magasin, C pour colis).");
                    alert.showAndWait();
                    return;
                }
                char typeLivraisonChar = typeLivraison.charAt(0);
                String titres = tfLivres.getText().trim();
                if (titres.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.getDialogPane().setPrefWidth(400);
                    alert.getDialogPane().setPrefHeight(400);
                    alert.setTitle("Information");
                    alert.setContentText("Veuillez entrer les titres des livres à commander, séparés par des virgules.");
                    alert.showAndWait();
                    return;
                }
                List<String> listTitres = Arrays.asList(titres.split(","));
                for (String titre : listTitres) {
                    String isbn = null;
                    PreparedStatement psLivre = connexionSQL.prepareStatement(
                        "SELECT isbn FROM LIVRE WHERE titre = ?");
                    psLivre.setString(1, titre);
                    ResultSet rsLivre = psLivre.executeQuery();
                    if (rsLivre.next()) {
                        isbn = rsLivre.getString("isbn");
                    }
                    rsLivre.close();
                    psLivre.close();
                    if (isbn == null) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.getDialogPane().setPrefWidth(400);
                        alert.getDialogPane().setPrefHeight(400);
                        alert.setTitle("Information");
                        alert.setContentText("Le titre '" + titre + "' n'existe pas dans le catalogue.");
                        alert.showAndWait();
                        return;
                    }
                }



                String idMagasinStr = tfIdMagasin.getText().trim();
                int idMagasin = Integer.parseInt(idMagasinStr);
                this.client.passerCommande(isEnLigne, typeLivraisonChar, listTitres, idMagasin);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Commande Passée");
                alert.setHeaderText(null);
                alert.setContentText("Votre commande a été passée avec succès !");
                alert.showAndWait();
                tfLivres.clear();
                tfIdMagasin.clear();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setPrefHeight(400);
                alert.setTitle("Information");
                alert.setContentText("Veuillez entrer un ID magasin valide (un nombre entier).");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().setPrefWidth(400);
                alert.getDialogPane().setPrefHeight(400);
                alert.setTitle("Erreur");
                alert.setContentText("Une erreur est survenue lors de la commande : " + ex.getMessage());
                alert.showAndWait();
            }
    });

        VBox vbox = new VBox(10, btnRetour, CbEnLigne, cbTypeLivraison, tfLivres, tfIdMagasin, btnValider);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        res.setCenter(vbox);
        return res;
    }


    public BorderPane pageRecommande() {
        BorderPane res = new BorderPane();
        Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(
            fond,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        Background wpp = new Background(backgroundImage);
        res.setBackground(wpp);

        StringBuilder recommandations = new StringBuilder();
        for (String titre : client.onVousRecommande()) {
            recommandations.append("- ").append(titre).append("\n");
        }
        TextArea textRecommande = new TextArea(recommandations.toString());
        textRecommande.setEditable(false);
        textRecommande.setWrapText(true);

        textRecommande.setStyle("-fx-font-family: 'monospaced'; -fx-control-inner-background: #bec3b9; -fx-text-fill: black;");
        textRecommande.setPrefColumnCount(40);
        textRecommande.setPrefRowCount(recommandations.toString().split("\n").length);


        VBox vbox = new VBox(15, btnRetour, textRecommande);

        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        res.setCenter(vbox);
        return res;
    }

   public Alert infoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setPrefWidth(400);
        alert.getDialogPane().setPrefHeight(400);
        alert.setTitle("Information");
        alert.setHeaderText("Aide");
        alert.setContentText("Bienvenue dans le menu client de Livre Express !\n\n"
                + "1. Consulter le catalogue : Affiche la liste des livres disponibles.\n"
                + "2. Passer une commande : Permet de passer une commande en ligne ou en magasin.\n"
                + "3. On vous recommande : Affiche les livres recommandés pour vous.\n\n"
                + "Pour toute assistance, veuillez contacter le support.");
        
        return alert;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
