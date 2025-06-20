import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ControleurBoutonClient implements EventHandler<ActionEvent> {
    private MenuClient menuCli;
        
    public ControleurBoutonClient(MenuClient menuCli){
        this.menuCli= menuCli;
    }
    @Override
    public void handle(ActionEvent event) {
        Button button = (Button) (event.getSource());
        if (button.getText().contains("Consulter Catalogue")) {
            this.menuCli.fadeOut(button,() -> this.menuCli.getRoot().setCenter(this.menuCli.pageCatalogue()));
        } else if (button.getText().contains("Passer une Commande")) {
            this.menuCli.fadeOut(button,() ->this.menuCli.getRoot().setCenter(this.menuCli.pagePasserCommande()));
        } else if (button.getText().contains("On vous recommande")) {
            this.menuCli.fadeOut(button,() ->this.menuCli.getRoot().setCenter(this.menuCli.pageRecommande()));
        }
        else if ("info".equals(button.getId())) {
            this.menuCli.infoAlert().showAndWait();
        } else if (button.getText().contains("Retour")) {
            this.menuCli.fadeOut(button,() ->this.menuCli.getRoot().setCenter(this.menuCli.pageMenu()));
        } else if ("maison".equals(button.getId())) {
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
            MenuConnexion menuConnexion = new MenuConnexion();
            menuConnexion.init();
            Stage stageConnexion = new Stage();
            try {
                menuConnexion.start(stageConnexion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
    
