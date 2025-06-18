import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ControleurBoutonVendeur implements EventHandler<ActionEvent> {

private MenuVendeur MenuVendeur;

public ControleurBoutonVendeur(MenuVendeur MenuVendeur){
    this.MenuVendeur = MenuVendeur;
    }

@Override
public void handle(ActionEvent event) {

Button button = (Button) (event.getSource());

    if (button.getText().contains("Ajouter un livre")) {
        this.MenuVendeur.getRoot().setCenter(this.MenuVendeur.afficherPageAjouterLivre());
        }

    if (button.getText().contains("Mise À jour Quantité Livre")) {
        this.MenuVendeur.getRoot().setCenter(this.MenuVendeur.afficherPageMajQuantite());
        }

    if (button.getText().contains("Disponibilité Livre")) {
        this.MenuVendeur.getRoot().setCenter(this.MenuVendeur.afficherPageDispoLivres());
        }
    
    if (button.getText().contains("Transférer un livre")) {
        this.MenuVendeur.getRoot().setCenter(this.MenuVendeur.afficherPageTransfertLivre());
        }
    if (button.getText().contains("Passer Commande pour un Client")) {
        this.MenuVendeur.getRoot().setCenter(this.MenuVendeur.afficherPageCommandeClient());
        }

    if ("maison".equals(button.getId())) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        MenuConnexion menuConnexion = new MenuConnexion();
        menuConnexion.init();
        Stage stageConnexion = new Stage();
        menuConnexion.start(stageConnexion);
            
    }

    if ("info".equals(button.getId())) {
        this.MenuVendeur.infoAlert().showAndWait();
    }
}
}