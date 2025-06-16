import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class ControleurAjouterLivre implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un livre");
        dialog.setHeaderText("Ajouter un nouveau livre");
        dialog.setContentText("Entrez le titre du livre :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(titre -> {
            //base de données
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Livre ajouté");
            alert.setHeaderText(null);
            alert.setContentText("Livre avec le titre \"" + titre + "\" ajouté.");
            alert.showAndWait();
        });
    }
}