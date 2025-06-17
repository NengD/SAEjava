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
    }
}