import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ControleurBoutonAdministrateur implements EventHandler<ActionEvent> {
private MenuAdministrateur menuAdm;
    
public ControleurBoutonAdministrateur(MenuAdministrateur menuAdm){
    this.menuAdm= menuAdm;
}
@Override
public void handle(ActionEvent event) {
    Button button = (Button) (event.getSource());
    if (button.getText().contains("Créer un vendeur")) {
        menuAdm.fadeOut(button, () -> menuAdm.getRoot().setCenter(menuAdm.pageCreerVendeur()));
    
    
    } else if (button.getText().contains("Créer une librairie")) {
        menuAdm.fadeOut(button, () -> menuAdm.getRoot().setCenter(menuAdm.pageCreerLibrairie()));
    } else if (button.getText().contains("Consulter les statistiques")) {
        menuAdm.fadeOut(button, () -> menuAdm.getRoot().setCenter(menuAdm.pageStats()));
    } else if (button.getText().contains("Créer un livre")) {
        menuAdm.fadeOut(button, () -> menuAdm.getRoot().setCenter(menuAdm.pageCreerLivre()));
    } else if (button.getText().contains("Maison")) {
        menuAdm.fadeOut(button, () -> menuAdm.panelCentral.setCenter(menuAdm.fenetreAdm()));
    } else if (button.getText().contains("Info")) {
        menuAdm.fadeOut(button, () -> menuAdm.info().showAndWait());
    } else if (button.getText().contains("Retour")) {
        menuAdm.fadeOut(button, () -> menuAdm.getRoot().setCenter(menuAdm.fenetreAdm()));
    } else if (button.getText().contains("Transférer un livre")) {
        menuAdm.fadeOut(button, () -> menuAdm.getRoot().setCenter(menuAdm.pageTransfertLivre()));
    }
    else if ("maison".equals(button.getId())) {
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
    
