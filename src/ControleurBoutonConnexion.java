import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ControleurBoutonConnexion implements EventHandler<ActionEvent> {

    private MenuConnexion appli;
    private ConnexionMySQL connexionSQL;

    public ControleurBoutonConnexion(ConnexionMySQL connexionSQL, MenuConnexion appli) {
        this.appli = appli;
        this.connexionSQL = connexionSQL;
    }

    @Override
    public void handle(ActionEvent event) {
        String type = this.appli.getTypeCompte();
        if (type == null)
            return; // rien sélectionné

        if (type.equals("Client")) {
            this.appli.afficheMenuClient();
        } else if (type.equals("Vendeur")) {
            this.appli.afficheMenuVendeur();
        } else if (type.equals("Administrateur")) {
            this.appli.afficheMenuAdmin();
        }
    }
}