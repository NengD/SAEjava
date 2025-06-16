import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControleurInfo implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Ceci est la page reservé aux vendeurs :\n"
                            +" - Vous pouvez y ajouter des livres.\n"
                            +" - Mettre à jour les quantités.\n"
                            +" - Vérifier la disponibilité des livres.\n"
                            +" - Transférer des livres entre magasins.\n"
                            +" - Passer des commandes pour les clients.");
        alert.showAndWait();
    }
}