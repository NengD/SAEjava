import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuVendeur extends Application {

    private Button btnAjouter;
    private Button btnMajQuantite;
    private Button btnDispo;
    private Button btnTransfert;
    private Button btnCommande;
    private Button boutonMaison;
    private Button btnInfo;
    private Vendeur vendeur;
    private ConnexionMySQL connexion;
    private BorderPane root;    

    public static void main(String[] args) {
        launch(MenuVendeur.class, args);
    }

    public void setContext(ConnexionMySQL connexion, Vendeur vendeur) {
        this.connexion = connexion;
        this.vendeur = vendeur;
    }   

    public BorderPane getRoot() {
        return this.root;
    }

    @Override
    public void init() {

        //Ajout des boutons
        this.btnAjouter = new Button("Ajouter un livre");
        this.btnMajQuantite = new Button("Mise À jour Quantité Livre");
        this.btnDispo = new Button("Disponibilité Livre");
        this.btnTransfert = new Button("Transférer un livre");
        this.btnCommande = new Button("Passer Commande pour un Client");

        //Icone Home et Info
        this.boutonMaison = new Button();
        Image imgMaison = new Image("file:../img/home.png");
        ImageView viewMaison = new ImageView(imgMaison);
        viewMaison.setFitWidth(32);
        viewMaison.setFitHeight(32);
        this.boutonMaison.setGraphic(viewMaison);

        this.btnInfo = new Button();
        Image imgInfo = new Image("file:../img/info.png");
        ImageView viewInfo = new ImageView(imgInfo);
        viewInfo.setFitWidth(32);
        viewInfo.setFitHeight(32);
        this.btnInfo.setGraphic(viewInfo);
        ControleurBoutonVendeur controleur = new ControleurBoutonVendeur(this);
        btnInfo.setOnAction(controleur);
        btnAjouter.setOnAction(controleur);
        this.btnAjouter = setOnAction(controleur);
        this.btnMajQuantite = setOnAction(controleur);
        this.btnDispo = setOnAction(controleur);
        this.btnTransfert = setOnAction(controleur);
        this.btnCommande = setOnAction(controleur);
        btnInfo.setOnAction(new ControleurInfo());
        btnAjouter.setOnAction(e -> root.setCenter(afficherPageAjouterLivre()));
    }

    private Scene laScene() {
        root = new BorderPane();
        root.setTop(titre());
        root.setCenter(fenetreVendeur());
        return new Scene(root, 700, 500);
    }

    public Pane titre() {

        //Banniere Haut de Page
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(0, 10, 0, 10));
        BackgroundFill background = new BackgroundFill(Color.web("#a76726"), null, null);
        Background backgroundTitre = new Background(background);
        banniere.setBackground(backgroundTitre);

        //Titre 
        Text titre = new Text("Menu Vendeur");
        titre.setFont(Font.font("Arial", 50));
        titre.setFill(Color.BLACK);

        HBox boiteTitre = new HBox();
        boiteTitre.setSpacing(10);
        boiteTitre.setAlignment(Pos.CENTER);
        boiteTitre.getChildren().addAll(this.boutonMaison,this.btnInfo);

        banniere.setRight(boiteTitre);
        banniere.setLeft(titre);
        return banniere;
    }

    public BorderPane fenetreVendeur() {
        //Background de la fenetre
        BorderPane fenetre = new BorderPane();
        BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
        Background backgroundFenetre = new Background(background);
        fenetre.setBackground(backgroundFenetre);

        //Boutons disponibles
        VBox boutons = new VBox(20);
        boutons.setPadding(new Insets(40));
        boutons.setAlignment(Pos.CENTER);
        boutons.getChildren().addAll(btnAjouter, btnMajQuantite, btnDispo, btnTransfert, btnCommande);
        fenetre.setCenter(boutons);

        btnAjouter.setMaxWidth(Double.MAX_VALUE);
        btnMajQuantite.setMaxWidth(Double.MAX_VALUE);
        btnDispo.setMaxWidth(Double.MAX_VALUE);
        btnTransfert.setMaxWidth(Double.MAX_VALUE);
        btnCommande.setMaxWidth(Double.MAX_VALUE);
        boutons.setFillWidth(true);
        return fenetre;
    }

    public BorderPane afficherPageAjouterLivre() {
        //Page pour ajouter un livre

        BorderPane page = new BorderPane();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label isbnLabel = new Label("ISBN :");
        TextField isbnField = new TextField();

        Label qteLabel = new Label("Quantité :");
        TextField qteField = new TextField();

        Button ajouterBtn = new Button("Ajouter");

        ajouterBtn.setOnAction(e -> {
            String isbn = isbnField.getText();
            String qteStr = qteField.getText();
            int idmag = vendeur.getIdMagasin();
            int quantite;

            try {
                quantite = Integer.parseInt(qteStr);
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Quantité invalide.");
                return;
            }

            try {
                Connection conn = vendeur.getConnexion().getConnection();
                vendeur.ajouterLivre(isbn, quantite);
                showAlert("Succès", "Livre ajouté !");
                isbnField.clear();
                qteField.clear();
            } catch (Exception ex) {
                showAlert("Erreur", "Erreur lors de l'ajout : " + ex.getMessage());
            }
        });

        grid.add(isbnLabel, 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(qteLabel, 0, 1);
        grid.add(qteField, 1, 1);
        grid.add(ajouterBtn, 0, 2);

        page.setCenter(grid);
        return page;
    }


    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Menu Vendeur");
        stage.setScene(laScene());
        stage.show();
    }
}
