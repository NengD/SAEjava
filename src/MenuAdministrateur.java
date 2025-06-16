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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;

 
public class MenuAdministrateur extends Application {
    
    private Button btnCreerVendeur;
    private Button btnCreerLibrairie;
    private Button btnConsulterStat;
    private Button btnCreerLivre;
    public BorderPane panelCentral;
    private Button boutonMaison;
    private Button boutonInfo;
    private BorderPane root;
    private Button retour;
    
 
    /** 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(MenuAdministrateur.class, args);
    }
    
    @Override
    public void init(){
        this.root = new BorderPane();
        ControleurBoutonAdministrateur controleur = new ControleurBoutonAdministrateur(this);


        this.btnConsulterStat = new Button("Consulter les statistiques");
        this.btnConsulterStat.setOnAction(controleur);
        
        this.btnCreerVendeur = new Button("Créer un vendeur");
        this.btnCreerVendeur.setOnAction(controleur);
        
        this.btnCreerLibrairie = new Button("Créer une librairie");
        this.btnCreerLibrairie.setOnAction(controleur);
        
        this.btnCreerLivre = new Button("Créer un livre");
        this.btnCreerLivre.setOnAction(controleur);
        
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
        this.boutonInfo.setOnAction(controleur);

        this.retour = new Button("Retour");
        this.retour.setOnAction(controleur);
    }
    
    public BorderPane getRoot() {
    return this.root;
    }
    

    public Pane titre(){
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(0, 10, 0, 10));
        BackgroundFill background = new BackgroundFill(Color.web("#a76726"), null, null);
        Background backgroundTitre = new Background(background);
        banniere.setBackground(backgroundTitre);
        Text titre = new Text("Menu Administrateur");
        titre.setFont(Font.font("Arial", 50));
        HBox boiteTitre = new HBox();
        boiteTitre.setSpacing(10);
        boiteTitre.setAlignment(Pos.CENTER);
        boiteTitre.getChildren().addAll(this.boutonMaison, this.boutonInfo);
        banniere.setRight(boiteTitre);
        banniere.setLeft(titre);
        return banniere;
    }
    
    

    public BorderPane fenetreAdm(){
        BorderPane res = new BorderPane();
        BackgroundFill background1 = new BackgroundFill(Color.web("d2d1ad"), null, null);
        Background backgroundAdm = new Background(background1);
        res.setBackground(backgroundAdm);
        
        VBox boutons = new VBox();
        boutons.getChildren().addAll(btnConsulterStat, btnCreerVendeur, btnCreerLibrairie, btnCreerLivre);
        boutons.setSpacing(10);
        boutons.setAlignment(Pos.CENTER);
        res.setCenter(boutons);
        return res;
    }

    public BorderPane pageStats(){
        BorderPane res = new BorderPane();
        BackgroundFill background2 = new BackgroundFill(Color.web("d2d1ad"), null, null);
        Background backgroundStat = new Background(background2);
        res.setBackground(backgroundStat);
        VBox centre = new VBox();
        centre.getChildren().add(this.retour);
        res.setCenter(centre);
        
        return res;


    }

    public BorderPane pageCreerLivre(){
        BorderPane res = new BorderPane();
        BackgroundFill background3 = new BackgroundFill(Color.web("d2d1ad"), null, null);
        Background backgroundLi = new Background(background3);
        res.setBackground(backgroundLi);
        VBox centre = new VBox();
        centre.getChildren().add(this.retour);
        res.setCenter(centre);
        
        return res;
    }
    public BorderPane pageCreerVendeur(){
        BorderPane res = new BorderPane();
        BackgroundFill background4 = new BackgroundFill(Color.web("d2d1ad"), null, null);
        Background backgroundVe = new Background(background4);
        res.setBackground(backgroundVe);
        VBox centre = new VBox();
        centre.getChildren().add(this.retour);
        res.setCenter(centre);
        
        return res;
    }

    public BorderPane pageCreerLibrairie(){
        BorderPane res = new BorderPane();
        BackgroundFill background5 = new BackgroundFill(Color.web("d2d1ad"), null, null);
        Background backgroundLib = new Background(background5);
        res.setBackground(backgroundLib);
        VBox centre = new VBox();
        centre.getChildren().add(this.retour);
        res.setCenter(centre);
        
        return res;
    }



    
    private Scene lascene(){
        this.root.setTop(titre());
        this.root.setCenter(fenetreAdm());
        return new Scene(this.root, 600, 400);  
    }

    public void start(Stage stage) {
        this.panelCentral = fenetreAdm();
        stage.setTitle("Livre Express - Menu Administrateur");
        stage.setScene(lascene());
        stage.show();
    }
    
    public Alert info(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Vous ếtes sur la page Administrateur, Vous pouvez gérer la base de donnèes et l'ensemble des magasins. Les différents boutons vous permette de réaliser plusieurs actions.");
        alert.setTitle("Information");
        alert.getDialogPane().setPrefSize(500, 200);
        alert.setHeaderText("Aide");
        return alert;
    }
    
    

}
