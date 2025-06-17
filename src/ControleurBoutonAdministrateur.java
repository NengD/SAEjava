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
        this.menuAdm.getRoot().setCenter(this.menuAdm.pageCreerVendeur());
    } else if (button.getText().contains("Créer une librairie")) {
        this.menuAdm.getRoot().setCenter(this.menuAdm.pageCreerLibrairie());
    } else if (button.getText().contains("Consulter les statistiques")) {
        this.menuAdm.getRoot().setCenter(this.menuAdm.pageStats());
    } else if (button.getText().contains("Créer un livre")) {
        this.menuAdm.getRoot().setCenter(this.menuAdm.pageCreerLivre());
    } else if (button.getText().contains("Maison")) {
        this.menuAdm.panelCentral.setCenter(this.menuAdm.fenetreAdm());
    }
    else if (button.getText().contains("Info")) {
        this.menuAdm.info().showAndWait();
    } else if (button.getText().contains("Retour")) {
        this.menuAdm.getRoot().setCenter(this.menuAdm.fenetreAdm());
    }
    else if (button.getText().contains("Transférer un livre")) {
        this.menuAdm.getRoot().setCenter(this.menuAdm.pageTransfertLivre());
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
    
