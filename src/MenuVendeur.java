import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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

        this.btnAjouter.setOnAction(controleur);
        this.btnMajQuantite.setOnAction(controleur);
        this.btnDispo.setOnAction(controleur);
        this.btnTransfert.setOnAction(controleur);
        this.btnCommande.setOnAction(controleur);
        this.boutonMaison.setOnAction(controleur);
        btnInfo.setOnAction(controleur);

        
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
    Image fond = new Image("file:./img/wp.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
        Background wpp = new Background(backgroundImage);
        page.setBackground(wpp);

    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(20));
    vbox.setAlignment(Pos.CENTER);

    Label titreLabel = new Label("Ajouter un livre au stock");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 15, 0, 0, 0);");

    TextField isbnField = new TextField();
    isbnField.setPromptText("Entrez l'ISBN du livre");
    isbnField.setStyle("-fx-prompt-text-fill: gray;");

    TextField qteField = new TextField();
    qteField.setPromptText("Entrez la quantité");
    qteField.setStyle("-fx-prompt-text-fill: gray;");

    Button ajouterBtn = new Button("Ajouter");
    ajouterBtn.setOnAction(e -> {
        String isbn = isbnField.getText();
        String qteStr = qteField.getText();
        //int idmag = vendeur.getIdMagasin();
        int quantite = 0;

        try {
            vendeur.ajouterLivre(isbn, quantite);
            showAlert("Livre ajouté","Le livre a bien été ajouté au stock.");
            isbnField.clear();
            qteField.clear();
        } catch (Exception ex) {
            showAlert("Erreur","Erreur de l'ajout dans le stock.");
        }

        try {
            quantite = Integer.parseInt(qteStr);
        } catch (NumberFormatException ex) {
            showAlert("Erreur"," la Quantité est invalide, entrez un nombre");
            }
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> {this.getRoot().setCenter(this.fenetreVendeur());});

    HBox boutons = new HBox(10, retourBtn,ajouterBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel,isbnField, qteField, boutons);
    page.setCenter(vbox);

    return page;
    }

public BorderPane afficherPageMajQuantite() {
    BorderPane page = new BorderPane();
    Image fond = new Image("file:./img/wp.jpg");
    BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
    Background wpp = new Background(backgroundImage);
    page.setBackground(wpp);

    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(20));
    vbox.setAlignment(Pos.CENTER);

    Label titreLabel = new Label("Mettre à jour la quantité d'un livre");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 15, 0, 0, 0);");

    TextField isbnField = new TextField();
    isbnField.setPromptText("Entrez l'ISBN du livre");
    isbnField.setStyle("-fx-prompt-text-fill: gray;");

    TextField qteField = new TextField();
    qteField.setPromptText("Entrez la nouvelle quantité");
    qteField.setStyle("-fx-prompt-text-fill: gray;");

    Button majBtn = new Button("Mettre à jour");
    majBtn.setOnAction(e -> {
        String isbn = isbnField.getText();
        String qteStr = qteField.getText();
        int quantite = 0;

        try {
            Livre livre = new Livre(isbn);
            vendeur.majQuantiteLivre(livre, quantite);
            showAlert("Quantité mise a jour","La quantité a bien été mise à jour.");
            isbnField.clear();
            qteField.clear();

        } catch (Exception ex) {
            showAlert("Erreur","Erreur de mise à jour.");
        }

        try {
         quantite = Integer.parseInt(qteStr);
        } catch (NumberFormatException ex) {
            showAlert("Erreur","La quantité est invalide, entrez un nombre");
        }
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> {this.getRoot().setCenter(this.fenetreVendeur());});

    HBox boutons = new HBox(10, retourBtn,majBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel,isbnField, qteField, boutons);
    page.setCenter(vbox);
    return page;
}

public BorderPane afficherPageDispoLivres() {  

    BorderPane page = new BorderPane();
    Image fond = new Image("file:./img/wp.jpg");
    BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
    Background wpp = new Background(backgroundImage);
    page.setBackground(wpp);

    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);

    Label titreLabel = new Label("Vérifier la disponibilité d'un livre");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 15, 0, 0, 0);");

    TextField livreField = new TextField();
    livreField.setPromptText("Entrez le titre du livre");
    livreField.setStyle("-fx-prompt-text-fill: gray;");

    Button verifierBtn = new Button("Vérifier");


    verifierBtn.setOnAction(e -> {
        String titreLivre = livreField.getText();
        if (titreLivre.isEmpty()) {
            showAlert("Erreur","Veuillez entrez un titre");
        }
        boolean dispo = vendeur.livreDisponible(titreLivre);
        if (dispo == true) {
            showAlert("Livre disponible","Le livre \"" + titreLivre + "\" est disponible en stock.");
        } else {
            showAlert("Livre non disponible","Le livre \"" + titreLivre + "\" n'est pas disponible en stock.");
        }
        livreField.clear();
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    HBox boutons = new HBox(10, retourBtn, verifierBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel, livreField, boutons);
    page.setCenter(vbox);
    return page;
}

public BorderPane afficherPageTransfertLivre() {

    BorderPane page = new BorderPane();
    Image fond = new Image("file:./img/wp.jpg");
    BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
    Background wpp = new Background(backgroundImage);
    page.setBackground(wpp);

    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);

    Label titreLabel = new Label("Transférer un livre vers un autre magasin");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold ;-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 15, 0, 0, 0);");


    TextField isbnField = new TextField();
    isbnField.setPromptText("Entrez l'ISBN du livre");
    isbnField.setStyle("-fx-prompt-text-fill: gray;");

    TextField qteField = new TextField();
    qteField.setPromptText("Entrez la quantité à transférer");
    qteField.setStyle("-fx-prompt-text-fill: gray;");

    TextField idMagField = new TextField();
    idMagField.setPromptText("Entrez l'ID du magasin destinataire");
    idMagField.setStyle("-fx-prompt-text-fill: gray;");

    Button transfererBtn = new Button("Transférer");
    transfererBtn.setOnAction(e -> {
        String isbn = isbnField.getText();
        String qteStr = qteField.getText();
        String idMagStr = idMagField.getText();
        int quantite, idMagasinDest;

        try {
            quantite = Integer.parseInt(qteStr);
            idMagasinDest = Integer.parseInt(idMagStr);
        } catch (NumberFormatException ex) {
            showAlert("Erreur","La quantité est invalide, entrez un nombre");
            return;
        }

        try {
            vendeur.transfertLivre(isbn, quantite, idMagasinDest);
            showAlert("Livre Transféré","Le livre a bien été transféré vers"+idMagasinDest);
            isbnField.clear();
            qteField.clear();
            idMagField.clear();   
        }   catch (Exception ex){
            showAlert("Erreur","Une erreur est survenue lors du transfert");
        }
        
        
    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    HBox boutons = new HBox(10, retourBtn, transfererBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel, isbnField, qteField, idMagField,boutons);
    page.setCenter(vbox);
    return page;
}

public BorderPane afficherPageCommandeClient() {
    BorderPane page = new BorderPane();
    Image fond = new Image("file:./img/wp.jpg");
    BackgroundImage backgroundImage = new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));   
    Background wpp = new Background(backgroundImage);
    page.setBackground(wpp);

    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);

    Label titreLabel = new Label("Passer une commande pour un client");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 15, 0, 0, 0);");

    TextField idClientField = new TextField();
    idClientField.setPromptText("Entrez l'ID du client");
    idClientField.setStyle("-fx-prompt-text-fill: gray;");

    TextField enLigneField = new TextField();
    enLigneField.setPromptText("En ligne (true/false)");
    enLigneField.setStyle("-fx-prompt-text-fill: gray;");

    TextField typeLivraisonField = new TextField();
    typeLivraisonField.setPromptText("Type de livraison (M/C)");
    typeLivraisonField.setStyle("-fx-prompt-text-fill: gray;");

    TextField titresField = new TextField();
    titresField.setPromptText("Titres des livres (séparés par des virgules)");
    titresField.setStyle("-fx-prompt-text-fill: gray;");

    Button commanderBtn = new Button("Commander");
    
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
            showAlert("Erreur"," Une erreur est survenue lors de botre commande reessayez.");

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
            showAlert("Erreur"," Une erreur est survenue lors de botre commande reessayez.");

        }

        List<String> titres = new ArrayList<>();
        for (String titre : titresStr.split(",")) {
            titres.add(titre);
        }

        try {
            vendeur.passerCommandePourClient(enLigne, typeLivraison, titres, client);
            showAlert("Succès"," la commande a bien été enregistré");
            idClientField.clear();
            enLigneField.clear();
            typeLivraisonField.clear();
            titresField.clear();
        } catch (Exception ex){
            showAlert("Erreur"," Une erreur est survenue lors de botre commande reessayez.");
        }

    });

    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    HBox boutons = new HBox(10, retourBtn, commanderBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel, idClientField, enLigneField, typeLivraisonField, titresField,boutons);
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
            + "2. Mise à jour quantité livre : Modifiez la quantité d'un livre en stock.\n"
            + "3. Disponibilité livre : Vérifiez si un livre est disponible dans le magasin.\n"
            + "4. Transférer un livre : Transférez un livre vers un autre magasin.\n"
            + "5. Passer commande pour un client : Passez une commande pour un client en magasin.");
        return alert;
    }

@Override
public void start(Stage stage) {
    stage.setTitle("Menu Vendeur");
    stage.setScene(laScene());
    stage.show();
    }
}