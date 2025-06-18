import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class ControleurInscription implements EventHandler<ActionEvent> {
    public ControleurInscription() {
    }

    @Override
    public void handle(ActionEvent event) {
        // Fermer la fenêtre courante
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Ouvrir la fenêtre d'inscription
        MenuInscription menuInscription = new MenuInscription();
        menuInscription.init();
        Stage stageInscription = new Stage();
        try {
            menuInscription.start(stageInscription);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}