import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MenuClient extends Application {
    private StackPane root;

    @Override
    public void start(Stage primaryStage) {
        root = new StackPane();
        root.setStyle("-fx-background-color: #d2d1a2;");
        pageMenu();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Menu Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void pageMenu() {
        Button btnCatalogue = new Button("Consulter Catalogue");
        Button btnCommande = new Button("Passer une Commande");
        Button btnRecommande = new Button("On vous recommande");

        btnCatalogue.setPrefWidth(200);
        btnCommande.setPrefWidth(200);
        btnRecommande.setPrefWidth(200);

        btnCatalogue.setOnAction(e -> pageCatalogue());
        btnCommande.setOnAction(e -> pagePasserCommande());
        btnRecommande.setOnAction(e -> pageRecommande());

        VBox vbox = new VBox(20, btnCatalogue, btnCommande, btnRecommande);
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().setAll(vbox);
    }

    public void pageCatalogue() {
        Label label = new Label("Consulter le catalogue");
        TextField tfParam = new TextField();
        tfParam.setPromptText("Paramètre (ex: filtre, mot-clé...)");
        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> pageMenu());

        VBox vbox = new VBox(15, label, tfParam, btnRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        root.getChildren().setAll(vbox);
    }

    public void pagePasserCommande() {
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
        btnRetour.setOnAction(e -> pageMenu());

        VBox vbox = new VBox(10, label, tfEnLigne, tfTypeLivraison, tfLivres, tfIdMagasin, btnRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        root.getChildren().setAll(vbox);
    }

    public void pageRecommande() {
        Label label = new Label("On vous recommande");
        TextField tfParam = new TextField();
        tfParam.setPromptText("Paramètre (ex: ID client, etc.)");
        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> pageMenu());

        VBox vbox = new VBox(15, label, tfParam, btnRetour);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        root.getChildren().setAll(vbox);
    }

    public static void main(String[] args) {
        launch(args);
    }
}