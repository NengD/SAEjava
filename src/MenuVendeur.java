import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

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

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;

public class MenuVendeur extends Application {

    private Button btnAjouter;
    private Button btnMajQuantite;
    private Button btnDispo;
    private Button btnTransfert;
    private Button btnCommande;
    private Button boutonMaison;
    private Button btnInfo;
    private Vendeur vendeur;
    private Stock stock;
    private Magasin magasin;
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
        this.btnAjouter = new Button("Ajouter un livre");
        this.btnMajQuantite = new Button("Mise À jour Quantité Livre");
        this.btnDispo = new Button("Disponibilité Livre");
        this.btnTransfert = new Button("Transférer un livre");
        this.btnCommande = new Button("Passer Commande pour un Client");

        this.boutonMaison = new Button();
        this.boutonMaison.setId("maison");
        Image imgMaison = new Image("file:./img/home.png");
        ImageView viewMaison = new ImageView(imgMaison);
        viewMaison.setFitWidth(32);
        viewMaison.setFitHeight(32);
        this.boutonMaison.setGraphic(viewMaison);

        this.btnInfo = new Button();
        this.btnInfo.setId("info");
        Image imgInfo = new Image("file:./img/info.png");
        ImageView viewInfo = new ImageView(imgInfo);
        viewInfo.setFitWidth(32);
        viewInfo.setFitHeight(32);
        this.btnInfo.setGraphic(viewInfo);
        ControleurBoutonVendeur controleur = new ControleurBoutonVendeur(this);
        btnInfo.setOnAction(controleur);
        btnAjouter.setOnAction(controleur);

        this.btnAjouter.setOnAction(controleur);
        this.btnMajQuantite.setOnAction(controleur);
        this.btnDispo.setOnAction(controleur);
        this.btnTransfert.setOnAction(controleur);
        this.btnCommande.setOnAction(controleur);
        this.boutonMaison.setOnAction(controleur);

        
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
        BackgroundFill background = new BackgroundFill(Color.web("#bec3b9"), null, null);
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
        Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
        Background wpp = new Background(backgroundImage);
        fenetre.setBackground(wpp);

        //Ajustement des boutons
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

    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);
    Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
        Background wpp = new Background(backgroundImage);
        page.setBackground(wpp);

    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(20));
    vbox.setAlignment(Pos.CENTER);
    Label isbnLabel = new Label("ISBN :");
    isbnLabel.setStyle("-fx-text-fill: white;");
    TextField isbnField = new TextField();
    Label qteLabel = new Label("Quantité :");
    qteLabel.setStyle("-fx-text-fill: white;");
    TextField qteField = new TextField();

    Button ajouterBtn = new Button("Ajouter");
    ajouterBtn.setOnAction(e -> {
        String isbn = isbnField.getText();
        String qteStr = qteField.getText();
        int idmag = vendeur.getIdMagasin();
        int quantite = 0;

        try {
            quantite = Integer.parseInt(qteStr);
        } catch (NumberFormatException ex) {
            showAlert("Erreur","Quantité invalide.");
            }

        try {
            vendeur.ajouterLivre(isbn, quantite);
            showAlert("","Livre ajouté");
            isbnField.clear();
            qteField.clear();
        } catch (Exception ex) {
            showAlert("Erreur","Erreur de l'ajout.");
        }
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> {this.getRoot().setCenter(this.fenetreVendeur());});

    HBox boutons = new HBox(10, retourBtn,ajouterBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(isbnLabel, isbnField, qteLabel, qteField, boutons);
    page.setCenter(vbox);

    return page;
    }

public BorderPane afficherPageMajQuantite() {
    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);
    Image fond = new Image("file:./img/wp.jpg");
    BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
    Background wpp = new Background(backgroundImage);
    page.setBackground(wpp);

    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(20));
    vbox.setAlignment(Pos.CENTER);
    Label isbnLabel = new Label("ISBN :");
    isbnLabel.setStyle("-fx-text-fill: white;");
    TextField isbnField = new TextField();
    Label qteLabel = new Label("Quantité à ajouter/retirer :");
    qteLabel.setStyle("-fx-text-fill: white;");
    TextField qteField = new TextField();

    Button majBtn = new Button("Mettre à jour");
    majBtn.setOnAction(e -> {
        String isbn = isbnField.getText();
        String qteStr = qteField.getText();
        int quantite = 0;

        try {
         quantite = Integer.parseInt(qteStr);
        } catch (NumberFormatException ex) {
            showAlert("Erreur","Quantité invalide.");
        }

        try {
            Livre livre = new Livre(isbn);
            vendeur.majQuantiteLivre(livre, quantite);
            showAlert("","Quantité mise à jour.");
            isbnField.clear();
            qteField.clear();

        } catch (Exception ex) {
            showAlert("Erreur","Erreur de mise à jour.");
        }
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> {this.getRoot().setCenter(this.fenetreVendeur());});

    HBox boutons = new HBox(10, retourBtn,majBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(isbnLabel, isbnField, qteLabel, qteField, boutons);
    page.setCenter(vbox);
    return page;
}

public BorderPane afficherPageDispoLivres() {  

    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);
    Image fond = new Image("file:./img/wp.jpg");
    BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
    Background wpp = new Background(backgroundImage);
    page.setBackground(wpp);

    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);
    Label titreLabel = new Label("Vérifier la disponibilité d'un livre");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
    Label livreLabel = new Label("Titre du livre :");
    livreLabel.setStyle("-fx-text-fill: white;");
    TextField livreField = new TextField();
    Button verifierBtn = new Button("Vérifier");
    Label resultatLabel = new Label();

    verifierBtn.setOnAction(e -> {
        String titreLivre = livreField.getText();
        if (titreLivre.isEmpty()) {
            resultatLabel.setText("Veuillez entrer un titre.");
            
        }
        boolean dispo = vendeur.livreDisponible(titreLivre);
        if (dispo == true) {
            resultatLabel.setText("Le livre \"" + titreLivre + "\" est disponible en stock.");
        } else {
            resultatLabel.setText("Le livre \"" + titreLivre + "\" n'est pas disponible en stock.");
        }
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    HBox boutons = new HBox(10, retourBtn, verifierBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel, livreLabel, livreField, boutons, resultatLabel);
    page.setCenter(vbox);
    return page;
}

public BorderPane afficherPageTransfertLivre() {

    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);
    Image fond = new Image("file:./img/wp.jpg");
    BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
    Background wpp = new Background(backgroundImage);
    page.setBackground(wpp);

    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);
    Label titreLabel = new Label("Transférer un livre vers un autre magasin");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold ;-fx-text-fill: white;");
    Label isbnLabel = new Label("ISBN :");
    isbnLabel.setStyle("-fx-text-fill: white;");
    TextField isbnField = new TextField();
    Label qteLabel = new Label("Quantité à transférer :");
    qteLabel.setStyle("-fx-text-fill: white;");
    TextField qteField = new TextField();
    Label idMagLabel = new Label("ID du magasin de destination :");
    idMagLabel.setStyle("-fx-text-fill: white;");
    TextField idMagField = new TextField();

    Button transfererBtn = new Button("Transférer");
    Label resultatLabel = new Label();
    transfererBtn.setOnAction(e -> {
        String isbn = isbnField.getText();
        String qteStr = qteField.getText();
        String idMagStr = idMagField.getText();
        int quantite, idMagasinDest;

        try {
            quantite = Integer.parseInt(qteStr);
            idMagasinDest = Integer.parseInt(idMagStr);
        } catch (NumberFormatException ex) {
            resultatLabel.setText("Invalide.");
            return;
        }

        vendeur.transfertLivre(isbn, quantite, idMagasinDest);
        resultatLabel.setText("Transfert effectué.");
        isbnField.clear();
        qteField.clear();
        idMagField.clear();
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    HBox boutons = new HBox(10, retourBtn, transfererBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel,isbnLabel, isbnField,qteLabel, qteField,idMagLabel, idMagField,boutons,resultatLabel);
    page.setCenter(vbox);
    return page;
}

public BorderPane afficherPageCommandeClient() {
    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);
    Image fond = new Image("file:./img/wp.jpg");
    BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
    Background wpp = new Background(backgroundImage);
    page.setBackground(wpp);

    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);
    Label titreLabel = new Label("Passer une commande pour un client");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
    Label idClientLabel = new Label("ID du client :");
    idClientLabel.setStyle("-fx-text-fill: white;");
    TextField idClientField = new TextField();
    Label enLigneLabel = new Label("Commande en ligne ?");
    enLigneLabel.setStyle("-fx-text-fill: white;");
    TextField enLigneField = new TextField();
    Label typeLivraisonLabel = new Label("Type de livraison (M/C)");
    typeLivraisonLabel.setStyle("-fx-text-fill: white;");
    TextField typeLivraisonField = new TextField();
    Label titresLabel = new Label("Livres a commander:");
    titresLabel.setStyle("-fx-text-fill: white;");
    TextField titresField = new TextField();
    Button commanderBtn = new Button("Commander");
    Label resultatLabel = new Label();
    
    commanderBtn.setOnAction(e -> {
        String idClientStr = idClientField.getText();
        String enLigneStr = enLigneField.getText();
        String typeLivraison = typeLivraisonField.getText();
        String titresStr = titresField.getText();
        int idClient = -1;
        boolean enLigne = false;

        try {
            idClient = Integer.parseInt(idClientStr);
            enLigne = Boolean.parseBoolean(enLigneStr);
        } catch (Exception ex) {
            resultatLabel.setText("Invalide.");
        }

        Client client = null;
        try {
            PreparedStatement ps = this.connexion.prepareStatement("SELECT idcli FROM CLIENT WHERE idcli = ?");
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                client = new Client(idClient, this.connexion);
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            resultatLabel.setText("Erreur");
        }

        List<String> titres = new ArrayList<>();
        for (String titre : titresStr.split(",")) {
            titres.add(titre);
        }

        vendeur.passerCommandePourClient(enLigne, typeLivraison, titres, client);
        resultatLabel.setText("Commande passée.");
        idClientField.clear();
        enLigneField.clear();
        typeLivraisonField.clear();
        titresField.clear();
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    HBox boutons = new HBox(10, retourBtn, commanderBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel,idClientLabel, idClientField,enLigneLabel, enLigneField,typeLivraisonLabel, typeLivraisonField,titresLabel, titresField,boutons,resultatLabel);
    page.setCenter(vbox);
    return page;
}
 
private void showAlert(String titre, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titre);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();    
    }

public Alert infoAlert() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.getDialogPane().setPrefWidth(400);
    alert.getDialogPane().setPrefHeight(400);
    alert.setTitle("Information");
    alert.setHeaderText("Aide");
    alert.setContentText("Bienvenue dans le menu vendeur de Livre Express !\n\n"
            + "1. Ajouter un livre : Ajoutez livre au stock du magasin.\n\n"
            + "2. Mise à jour quantité livre : Modifiez la quantité d'un livre en stock.\n\n"
            + "3. Disponibilité livre : Vérifiez si un livre est disponible dans le magasin.\n\n"
            + "4. Transférer un livre : Transférez un livre vers un autre magasin.\n\n"
            + "5. Passer commande pour un client : Passez une commande pour un client en magasin.\n\n"
            + "Pour toute assistance, veuillez contacter le support.");
        return alert;
    }

@Override
public void start(Stage stage) {
    stage.setTitle("Menu Vendeur");
    stage.setScene(laScene());
    stage.show();
    }
}