import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuVendeur extends Application {

    private BorderPane root;

    @Override
    public void start(Stage stage) {
        init();
        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Menu Vendeur");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initialise l'interface graphique du menu vendeur.
     */
    public void init() {
        root = new BorderPane();
        root.setTop(top());
        root.setCenter(centre());
        root.setPadding(new Insets(20));
    }

    private BorderPane top() {
        MenuBar menuBar = new MenuBar();
        ImageView homeIcon = new ImageView(new Image("file:../img/home.png"));
        Button homeButton = new Button("", homeIcon);
        homeButton.setPrefSize(40, 40);

        // Menu à gauche
        HBox leftBox = new HBox(menuBar);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        // Bouton accueil à droite
        HBox rightBox = new HBox(homeButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        // Utilisation d'un BorderPane pour placer à gauche et à droite
        BorderPane topPane = new BorderPane();
        topPane.setLeft(leftBox);
        topPane.setRight(rightBox);
        topPane.setPadding(new Insets(5, 10, 5, 10));
        return topPane;
    }

    private VBox centre() {
        Button btnAjouter = new Button("Ajouter un livre");
        Button btnMajQuantite = new Button("Mise À jour Quantité Livre");
        Button btnDispo = new Button("Disponibilité Livre");
        Button btnTransfert = new Button("Transférer un livre");
        Button btnCommande = new Button("Passer Commande pour un Client");

        btnAjouter.setMaxWidth(Double.MAX_VALUE);
        btnMajQuantite.setMaxWidth(Double.MAX_VALUE);
        btnDispo.setMaxWidth(Double.MAX_VALUE);
        btnTransfert.setMaxWidth(Double.MAX_VALUE);
        btnCommande.setMaxWidth(Double.MAX_VALUE);

        VBox centerBox = new VBox(20, btnAjouter, btnMajQuantite, btnDispo, btnTransfert, btnCommande);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));
        return centerBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}