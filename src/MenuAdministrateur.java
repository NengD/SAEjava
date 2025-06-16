
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


 
public class MenuAdministrateur extends Application {
    
    private Button btnCreerVendeur;
    private Button btnCreerLibrairie;
    private Button btnConsulterStat;
    private Button btnCreerLivre;
    public BorderPane panelCentral;
    private Button boutonMaison;
    private Button boutonInfo;
 
    /** 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(MenuAdministrateur.class, args);
    }
    
    @Override
    public void init(){
        this.btnConsulterStat = new Button("Consulter les statistiques");
        this.btnCreerVendeur = new Button("Créer un vendeur");
        this.btnCreerLibrairie = new Button("Créer une librairie");
        this.btnCreerLivre = new Button("Créer un livre");
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
    }
    

    public Pane titre(){
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(0, 10, 0, 10));
        BackgroundFill background = new BackgroundFill(Color.web("#582900"), null, null);
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
        VBox boutons = new VBox();
        boutons.getChildren().addAll(btnConsulterStat, btnCreerVendeur, btnCreerLibrairie, btnCreerLivre);
        res.setCenter(boutons);
        return res;
    }

    
    private Scene lascene(){
        BorderPane page = new BorderPane();
        page.setTop(titre());
        page.setCenter(fenetreAdm());
        return new Scene(page, 600, 400);  
    }

    public void start(Stage stage) {
        this.panelCentral = fenetreAdm();
        stage.setTitle("Livre Express - Menu Administrateur");
        stage.setScene(lascene());
        stage.show();
    }
    

    
    

}
