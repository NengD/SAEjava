import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    private Vendeur monVendeur;

    public static void main(String[] args) {
        launch(MenuVendeur.class, args);
    }

    @Override
    public void init() {
        this.btnAjouter = new Button("Ajouter un livre");
        this.btnMajQuantite = new Button("Mise À jour Quantité Livre");
        this.btnDispo = new Button("Disponibilité Livre");
        this.btnTransfert = new Button("Transférer un livre");
        this.btnCommande = new Button("Passer Commande pour un Client");

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

        btnInfo.setOnAction(new ControleurInfo());
        btnAjouter.setOnAction(new ControleurAjouterLivre());
    }

    private Scene laScene() {
        BorderPane page = new BorderPane();
        page.setTop(titre());
        page.setCenter(fenetreVendeur());
        return new Scene(page, 700, 500);
    }

    public Pane titre() {
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(0, 10, 0, 10));
        BackgroundFill background = new BackgroundFill(Color.web("#a76726"), null, null);
        Background backgroundTitre = new Background(background);
        banniere.setBackground(backgroundTitre);

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
        BorderPane fenetre = new BorderPane();
        BackgroundFill background = new BackgroundFill(Color.web("#d2d1ad"), null, null);
        Background backgroundFenetre = new Background(background);
        fenetre.setBackground(backgroundFenetre);

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


    @Override
    public void start(Stage stage) {
        stage.setTitle("Menu Vendeur");
        stage.setScene(laScene());
        stage.show();
    }
}