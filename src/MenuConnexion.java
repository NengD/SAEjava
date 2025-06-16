import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class MenuConnexion extends Application {
    // Déclaration des composants en attributs de classe
    private ConnexionMySQL connexionSQL;
    private ComboBox<String> typeCompte;
    private TextField idField;
    private PasswordField mdpField;
    private Button connexionBtn;

    @Override
    public void init() {
        try {
            this.connexionSQL = new ConnexionMySQL();
            this.connexionSQL.connecter();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion à la base : " + e.getMessage());
        }
        // Initialisation des composants (sans les ajouter à la scène)
        this.typeCompte = new ComboBox<>();
        this.typeCompte.getItems().addAll("Client", "Administrateur", "Vendeur");
        this.typeCompte.setPrefWidth(200);

        this.idField = new TextField();
        this.idField.setPrefWidth(200);

        this.mdpField = new PasswordField();
        this.mdpField.setPrefWidth(200);

        this.connexionBtn = new Button("Connexion");
        this.connexionBtn.setOnAction(new ControleurBoutonConnexion(this.connexionSQL, this));
    }

    @Override
    public void start(Stage stage) {
        // Labels
        Label typeLabel = new Label("Type de compte");
        Label idLabel = new Label("ID");
        Label mdpLabel = new Label("Mot de passe:");

        // Layouts pour organiser les éléments
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        grid.setPadding(new Insets(40, 40, 40, 40));

        // Placement des éléments
        grid.add(typeLabel, 0, 0);
        grid.add(typeCompte, 1, 0);

        grid.add(idLabel, 0, 1);
        grid.add(idField, 1, 1);

        grid.add(mdpLabel, 0, 2);
        grid.add(mdpField, 1, 2);

        grid.add(connexionBtn, 2, 2);

        // Centrage du contenu
        BorderPane root = new BorderPane();
        root.setCenter(grid);

        // Style du fond
        BackgroundFill background = new BackgroundFill(Color.web("d2d1ad"), null, null);
        Background backgroundTitre = new Background(background);
        root.setBackground(backgroundTitre);

        Scene scene = new Scene(root, 600, 350);
        stage.setTitle("Connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void afficheMenuClient(ConnexionMySQL connexionSQL) {
        // Logique pour afficher le menu client
    }

    public void afficheMenuVendeur(ConnexionMySQL connexionSQL) {
        // Logique pour afficher le menu vendeur
    }

    public void afficheMenuAdmin(ConnexionMySQL connexionSQL) {
        // Logique pour afficher le menu administrateur
    }
}