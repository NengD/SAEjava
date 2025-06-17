import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


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

    }
}
