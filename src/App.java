
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {

    private ConnexionMySQL connexionSQL;
    public boolean quitter;

    public App() {
        this.quitter = false;
    }

    public void lancer() {
        while (!this.quitter) {
            menuConnexion();
        }
        quitter();
    }

    public void menuConnexion() {
        boolean commande_faite = false;
        while (!commande_faite) {
            System.out.println("╭────────────────╮");
            System.out.println("│ Menu Connexion │");
            System.out.println("╰────────────────╯");
            System.out.println("C: Se connecter");
            System.out.println("Q: Quitter");

            String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("q")) {
                this.quitter = true;
                commande_faite = true;
            } else if (commande.equals("c")) {
                try {
                    boolean connexion_faite = false;
                    String login = "";
                    String mdp = "";
                    while (!connexion_faite) {
                        if (login.equals("")) {
                            System.out.println("Quel est votre login");
                            login = System.console().readLine();
                        } else if (mdp.equals("")) {
                            System.out.println("Quel est votre mot de passe");
                            mdp = System.console().readLine();
                        } else {
                            connexion_faite = true;
                        }
                    }
                    // Connexion à la base avec les identifiants saisis
                    this.connexionSQL = new ConnexionMySQL();
                    this.connexionSQL.connecter();

                    // Vérification du login/mdp dans la table Utilisateur
                    String role = "";
                    String sql = "SELECT role FROM Utilisateur WHERE login = ? AND mdp = ?";
                    try (PreparedStatement ps = this.connexionSQL.prepareStatement(sql)) {
                        ps.setString(1, login);
                        ps.setString(2, mdp);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            role = rs.getString("role");
                        }
                    }

                    if (role.equals("admin")) {
                        menuAdmin();
                    } else if (role.equals("vendeur")) {
                        menuVendeur();
                    } else if (role.equals("client")) {
                        menuClient();
                    } else {
                        System.out.println("Login ou mot de passe incorrect !");
                    }
                } catch (ClassNotFoundException ex) {
                    System.out.println("Driver MySQL non trouvé !");
                    System.exit(1);
                } catch (SQLException ex) {
                    System.out.println("Login ou mot de passe incorrect !");
                    System.exit(1);
                }
            }
        }
    }

    public void menuAdmin() {
        boolean commande_faite = false;
        while (!commande_faite) {
            System.out.println("╭─────────────────────╮");
            System.out.println("│ Menu Administrateur │");
            System.out.println("╰─────────────────────╯");
            System.out.println("Que voulez vous faire?");
            System.out.println("R: Retourner menu principal");
            System.out.println("C: Crée un compte vendeur");
            System.out.println("A: Ajouter une nouvelle librairie");
            System.out.println("G: Gérer les stocks de toutes les librairies");
            System.out.println("S: Consulter les statistiques de vente");

            /// Majuscule et minuscule prise en compte
	        String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("r")) {
                this.quitter = true;
                commande_faite = true;
            }
        }
    }

    public void menuVendeur() {
        boolean commande_faite = false;
        while (!commande_faite) {
            System.out.println("╭──────────────╮");
            System.out.println("│ Menu Vendeur │");
            System.out.println("╰──────────────╯");
            System.out.println("Que voulez vous faire?");
            System.out.println("R: Retourner menu principal");
            System.out.println("A: Ajouter un livre au stock de sa librairie");
            System.out.println("M: Mettre à jour la quantité disponible d’un livre");
            System.out.println("D: Vérifier la disponibilité d’un livre dans une librairie");
            System.out.println("P: Passer une commande pour un client en magasin");
            System.out.println("T: Transférer un livre d’une autre librairie pour satisfaire une commande client");

            /// Majuscule et minuscule prise en compte
	        String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("r")) {
                this.quitter = true;
                commande_faite = true;
            }
        }
    }

    public void menuClient() {
        boolean commande_faite = false;
        while (!commande_faite) {
            System.out.println("╭─────────────╮");
            System.out.println("│ Menu Client │");
            System.out.println("╰─────────────╯");
            System.out.println("Que voulez vous faire?");
            System.out.println("R: Retourner menu principal");
            System.out.println("P: Passer une commande");
            System.out.println("M: Choisir le mode de réception");
            System.out.println("C: Consulter le catalogue");

            /// Majuscule et minuscule prise en compte
	        String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("r")) {
                this.quitter = true;
                commande_faite = true;
            }
        }
    }

    /// Affiche un message d'au revoir
    public void quitter() {
        System.out.println("╭──────────────────────────────────────────────────────╮");
        System.out.println("│ Au revoir, la librairie reste ouverte et accessible! │");
        System.out.println("╰──────────────────────────────────────────────────────╯");
    }
}
