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
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.plaf.synth.Region;


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

    @Override
    public void init(){
        try {
            this.connexionSQL = new ConnexionMySQL();
            this.connexionSQL.connecter();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion à la base : " + e.getMessage());
        }
        this.boutonMaison = new Button();
        Image imgMaison = new Image("file:./img/home.png");
        ImageView viewMaison = new ImageView(imgMaison);
        viewMaison.setFitWidth(32);
        viewMaison.setFitHeight(32);
        this.boutonMaison.setGraphic(viewMaison);
        this.boutonInfo = new Button();
        Image imgInfo = new Image("file:./img/info.png");
        ImageView viewInfo = new ImageView(imgInfo);
        viewInfo.setFitWidth(32);
        viewInfo.setFitHeight(32);
        this.boutonInfo.setGraphic(viewInfo);
        this.btnCatalogue = new Button("Consulter Catalogue");
        this.btnCommande = new Button("Passer une Commande");
        this.btnRecommande = new Button("On vous recommande");
        this.btnRetour = new Button("Retour");
        this.btnRetour.setOnAction(e -> root.setCenter(pageMenu()));
        this.boutonInfo.setOnAction(e -> {infoAlert().showAndWait();});
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
        BackgroundFill background = new BackgroundFill(Color.web("#a76726"), null, null);
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
        BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
        Background backgroundMenu = new Background(background);
        res.setBackground(backgroundMenu);
        VBox bouton = new VBox();
        bouton.getChildren().addAll(btnCatalogue, btnCommande, btnRecommande);

        btnCatalogue.setPrefWidth(200);
        btnCommande.setPrefWidth(200);
        btnRecommande.setPrefWidth(200);

        btnCatalogue.setOnAction(e -> root.setCenter(pageCatalogue()));
        btnCommande.setOnAction(e -> root.setCenter(pagePasserCommande()));
        btnRecommande.setOnAction(e -> root.setCenter(pageRecommande()));

        VBox vbox = new VBox(20, btnCatalogue, btnCommande, btnRecommande);
        vbox.setAlignment(Pos.CENTER);

        res.setCenter(vbox);
        return res;
    }

    public BorderPane pageCatalogue() {
        BorderPane res = new BorderPane();
        BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
        Background backgroundMenu = new Background(background);
        res.setBackground(backgroundMenu);
        Label label = new Label("Consulter le catalogue");
        VBox vboxCatalogue = new VBox();
        vboxCatalogue.setAlignment(Pos.CENTER);
        String catalogue = "";
        for (Livre l : Client.consulterCatalogue(this.connexionSQL.getConnection())) {
            catalogue+=l.getIsbn() + " - " + l.getTitre(this.connexionSQL.getConnection())+"\n";
        }
        Text textCatalogue = new Text(catalogue);
        vboxCatalogue.getChildren().add(textCatalogue);

        ScrollPane scrollPane = new ScrollPane(vboxCatalogue);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(200);

        VBox vbox = new VBox(15, btnRetour, label, scrollPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        
        res.setCenter(vbox);
        return res;
    }

    public BorderPane pagePasserCommande() {
        BorderPane res = new BorderPane();
        BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
        Background backgroundMenu = new Background(background);
        res.setBackground(backgroundMenu);
        Label label = new Label("Passer une commande");
        TextField tfEnLigne = new TextField();
        tfEnLigne.setPromptText("En ligne ? (true/false)");
        String enLigne = tfEnLigne.getText();
        Boolean isEnLigne = false;
        if(enLigne.equals("true")){ isEnLigne = true;}
        TextField tfTypeLivraison = new TextField();
        tfTypeLivraison.setPromptText("Type de livraison (M/C)");
        String typeLivraison = tfTypeLivraison.getText();
        char typeLivraisonChar = typeLivraison.charAt(0);
        TextField tfLivres = new TextField();
        tfLivres.setPromptText("Liste des titres des livres à commander (séparés par ,)");
        String titres = tfLivres.getText();
        String[] titresArray = titres.split(",");
        List<String> listTitres = Arrays.asList(titresArray);
        TextField tfIdMagasin = new TextField();
        tfIdMagasin.setPromptText("ID magasin");
        int idMagasin = Integer.parseInt(tfIdMagasin.getText());
        this.client.passerCommande(isEnLigne, typeLivraisonChar, listTitres, idMagasin);

        VBox vbox = new VBox(10, label, tfEnLigne, tfTypeLivraison, tfLivres, tfIdMagasin, btnRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        
        res.setCenter(vbox);
        return res;
    }

    public BorderPane pageRecommande() {
        BorderPane rootPane = new BorderPane();
        BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
        Background backgroundMenu = new Background(background);
        rootPane.setBackground(backgroundMenu);
        Label label = new Label("On vous recommande");
        VBox vboxRecommande = new VBox();
        vboxRecommande.setAlignment(Pos.CENTER);
        String recomande = "";
        for (String titre : client.onVousRecommande()) {
            System.out.println("- " + titre);
        }
        Text textRecommande = new Text(recomande);
        vboxRecommande.getChildren().add(textRecommande);

        VBox vbox = new VBox(15, label, vboxRecommande, btnRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
    
        rootPane.setCenter(vbox);
        return rootPane;
   }

   public Alert infoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
