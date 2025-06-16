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


public class MenuClient extends Application {
    private Button btnCatalogue;
    private Button btnRecommande;
    private Button btnCommande;
    private Button boutonMaison;
    private BorderPane root;

    @Override
    public void init(){
        this.boutonMaison = new Button();
        Image imgMaison = new Image("file:./img/home.png");
        ImageView viewMaison = new ImageView(imgMaison);
        viewMaison.setFitWidth(32);
        viewMaison.setFitHeight(32);
        this.boutonMaison.setGraphic(viewMaison);
        this.btnCatalogue = new Button("Consulter Catalogue");
        this.btnCommande = new Button("Passer une Commande");
        this.btnRecommande = new Button("On vous recommande");
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
        boiteTitre.getChildren().add(this.boutonMaison);
        banniere.setRight(boiteTitre);
        banniere.setLeft(titre);
        return banniere;
    }

    public BorderPane pageMenu() {
        BorderPane res = new BorderPane();
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
        Label label = new Label("Consulter le catalogue");
        TextField tfParam = new TextField();
        tfParam.setPromptText("Paramètre (ex: filtre, mot-clé...)");
        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> root.setCenter(pageMenu()));
        VBox vbox = new VBox(15, label, tfParam, btnRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        
        res.setCenter(vbox);
        return res;
    }

    public BorderPane pagePasserCommande() {
        BorderPane res = new BorderPane();
        Label label = new Label("Passer une commande");
        TextField tfEnLigne = new TextField();
        tfEnLigne.setPromptText("En ligne ? (true/false)");
        TextField tfTypeLivraison = new TextField();
        tfTypeLivraison.setPromptText("Type de livraison (M/C)");
        TextField tfLivres = new TextField();
        tfLivres.setPromptText("Liste ISBN (séparés par ,)");
        TextField tfIdMagasin = new TextField();
        tfIdMagasin.setPromptText("ID magasin");

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> root.setCenter(pageMenu()));

        VBox vbox = new VBox(10, label, tfEnLigne, tfTypeLivraison, tfLivres, tfIdMagasin, btnRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        
        res.setCenter(vbox);
        return res;
    }

    public BorderPane pageRecommande() {
        BorderPane rootPane = new BorderPane();
        Label label = new Label("On vous recommande");
        TextField tfParam = new TextField();
        tfParam.setPromptText("Paramètre (ex: ID client, etc.)");
        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> root.setCenter(pageMenu()));


        VBox vbox = new VBox(15, label, tfParam, btnRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
    
        rootPane.setCenter(vbox);
        return rootPane;
   }


    public static void main(String[] args) {
        launch(args);
    }
}
