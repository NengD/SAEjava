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

        //Connexion BD
        try {
            this.connexion = new ConnexionMySQL();
            this.connexion.connecter();
        } catch (Exception e) {
            System.out.println("Erreur de connexion à la base : ");
        }


        //Ajout des boutons
        this.btnAjouter = new Button("Ajouter un livre");
        this.btnMajQuantite = new Button("Mise À jour Quantité Livre");
        this.btnDispo = new Button("Disponibilité Livre");
        this.btnTransfert = new Button("Transférer un livre");
        this.btnCommande = new Button("Passer Commande pour un Client");

        //Bouton Home et Info
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

        //Mise en marche des boutons
        this.btnAjouter.setOnAction(controleur);
        this.btnMajQuantite.setOnAction(controleur);
        this.btnDispo.setOnAction(controleur);
        this.btnTransfert.setOnAction(controleur);
        this.btnCommande.setOnAction(controleur);
        
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

/////////////////////////////////////////////////////////

// Page pour ajouter un livre
public BorderPane afficherPageAjouterLivre() {

    //Background de la page
    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);

    //Mise en page
    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(20));
    vbox.setAlignment(Pos.CENTER);
    Label isbnLabel = new Label("ISBN :");
    TextField isbnField = new TextField();
    Label qteLabel = new Label("Quantité :");
    TextField qteField = new TextField();

    //Bouton "Ajouter"
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

    //Bouton "Retour"
    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> {this.getRoot().setCenter(this.fenetreVendeur());});

    //Placement des boutons
    HBox boutons = new HBox(10, retourBtn,ajouterBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(isbnLabel, isbnField, qteLabel, qteField, boutons);
    page.setCenter(vbox);

    return page;
    }

/////////////////////////////////////////////////////////////////////////////////////

//Page pour mettre à jour la quantité d'un livre
public BorderPane afficherPageMajQuantite() {

    //Background de la page
    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);

    //Mise en page
    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(20));
    vbox.setAlignment(Pos.CENTER);
    Label isbnLabel = new Label("ISBN :");
    TextField isbnField = new TextField();
    Label qteLabel = new Label("Quantité à ajouter/retirer :");
    TextField qteField = new TextField();

    //Bouton "Mettre à jour"
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

    //Bouton Retour
    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> {this.getRoot().setCenter(this.fenetreVendeur());});

    //Placement des boutons
    HBox boutons = new HBox(10, retourBtn,majBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(isbnLabel, isbnField, qteLabel, qteField, boutons);
    page.setCenter(vbox);
    return page;
}

/////////////////////////////////////////////////////////////////////////////////////

//Page pour vérifier la disponibilité d'un livre
public BorderPane afficherPageDispoLivres() {  

    //Background de la page
    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);

    // Mise en page
    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);
    Label titreLabel = new Label("Vérifier la disponibilité d'un livre");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    Label livreLabel = new Label("Titre du livre :");
    TextField livreField = new TextField();
    Button verifierBtn = new Button("Vérifier");
    Label resultatLabel = new Label();

    //Bouton "Verifier"
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

    // Bouton Retour
    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    // Placement des boutons
    HBox boutons = new HBox(10, retourBtn, verifierBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel, livreLabel, livreField, boutons, resultatLabel);
    page.setCenter(vbox);
    return page;
}

/////////////////////////////////////////////////////////////////////////////////////


// Page pour transférer un livre vers un autre magasin
public BorderPane afficherPageTransfertLivre() {

    //Background de la page
    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);

    // Mise en page
    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);
    Label titreLabel = new Label("Transférer un livre vers un autre magasin");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    Label isbnLabel = new Label("ISBN :");
    TextField isbnField = new TextField();
    Label qteLabel = new Label("Quantité à transférer :");
    TextField qteField = new TextField();
    Label idMagLabel = new Label("ID du magasin de destination :");
    TextField idMagField = new TextField();

    // Bouton pour transférer le livre
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

    //Bouton Retour
    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    // Placement des boutons
    HBox boutons = new HBox(10, retourBtn, transfererBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel,isbnLabel, isbnField,qteLabel, qteField,idMagLabel, idMagField,boutons,resultatLabel);
    page.setCenter(vbox);
    return page;
}

/////////////////////////////////////////////////////////////////////////////////////

// Page pour passer une commande pour un client
public BorderPane afficherPageCommandeClient() {

    //Background de la page
    BorderPane page = new BorderPane();
    BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
    Background backgroundPage = new Background(background);
    page.setBackground(backgroundPage);

    //Mise en page
    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(30));
    vbox.setAlignment(Pos.CENTER);
    Label titreLabel = new Label("Passer une commande pour un client");
    titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    Label idClientLabel = new Label("ID du client :");
    TextField idClientField = new TextField();
    Label enLigneLabel = new Label("Commande en ligne ?");
    TextField enLigneField = new TextField();
    Label typeLivraisonLabel = new Label("Type de livraison (M/C)");
    TextField typeLivraisonField = new TextField();
    Label titresLabel = new Label("Livres a commander:");
    TextField titresField = new TextField();
    Button commanderBtn = new Button("Commander");
    Label resultatLabel = new Label();
    
    //Bouton "Commander"
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

    //Bouton Retour
    Button retourBtn = new Button("Retour");
    retourBtn.setOnAction(e -> this.root.setCenter(this.fenetreVendeur()));

    // Placement des boutons
    HBox boutons = new HBox(10, retourBtn, commanderBtn);
    boutons.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(titreLabel,idClientLabel, idClientField,enLigneLabel, enLigneField,typeLivraisonLabel, typeLivraisonField,titresLabel, titresField,boutons,resultatLabel);
    page.setCenter(vbox);
    return page;
}

//Afficher Alerte 
private void showAlert(String titre, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titre);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();    
    }

// demarrer application
@Override
public void start(Stage stage) {
    stage.setTitle("Menu Vendeur");
    stage.setScene(laScene());
    stage.show();
    }
}