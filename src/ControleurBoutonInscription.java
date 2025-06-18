import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ControleurBoutonInscription implements EventHandler<ActionEvent> {
    private MenuClient menuCli;
        
    public ControleurBoutonInscription(MenuClient menuCli){
        this.menuCli= menuCli;
    }
    @Override
    public void handle(ActionEvent event) {
        Button button = (Button) (event.getSource());
        if ("info".equals(button.getId())) {
            this.menuCli.infoAlert().showAndWait();
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