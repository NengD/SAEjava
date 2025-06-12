
import java.util.ArrayList;

public class App {

    private ConnexionMySQL connexionSQL;
    public boolean quitter;

    public App() {
        this.quitter = false;
        try {
            this.connexionSQL = new ConnexionMySQL();
            this.connexionSQL.connecter();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion à la base : " + e.getMessage());
        }
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
            System.out.println("╭───────────────────────────────╮");
            System.out.println("│ Menu de sélection utilisateur │");
            System.out.println("╰───────────────────────────────╯");
            System.out.println("A: Administrateur");
            System.out.println("V: Vendeur");
            System.out.println("C: Client");
            System.out.println("Q: Quitter");

            String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("q")) {
                this.quitter = true;
                commande_faite = true;
            } else if (commande.equals("a")) {
                System.out.println("Entrez l'ID de l'administrateur :");
                String idAdmin = System.console().readLine();
                Administrateur admin = new Administrateur("Admin", "Admin", this.connexionSQL);
                menuAdmin(admin);
            } else if (commande.equals("v")) {
                System.out.println("Entrez l'ID du vendeur :");
                String idVendeur = System.console().readLine();
                Vendeur vendeur = new Vendeur("Vendeur", "Vendeur", this.connexionSQL);
                menuVendeur(vendeur);
            } else if (commande.equals("c")) {
                System.out.println("Entrez l'ID du client :");
                String idClient = System.console().readLine();
                Client client = getClientFromDB(idClient);
                if (client != null) {
                    menuClient(client);
                } else {
                    System.out.println("Client introuvable.");
                }
            } else {
                System.out.println("Commande inconnue.");
            }
        }
    }

    public Client getClientFromDB(String idClient) {
        try {
            java.sql.PreparedStatement ps = this.connexionSQL.prepareStatement(
                    "SELECT nomcli, prenomcli, adressecli FROM CLIENT WHERE idcli = ?"
            );
            ps.setInt(1, Integer.parseInt(idClient));
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nomcli");
                String prenom = rs.getString("prenomcli");
                String adresse = rs.getString("adressecli");
                rs.close();
                ps.close();
                return new Client(nom, prenom, adresse, this.connexionSQL);
            } else {
                rs.close();
                ps.close();
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
            return null;
        }
    }

    public void menuAdmin(Administrateur admin) {
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

            String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("r")) {
                this.quitter = true;
                commande_faite = true;
            } else if (commande.equals("c")) {
                System.out.println("Nom du vendeur :");
                String nom = System.console().readLine();
                System.out.println("Prénom du vendeur :");
                String prenom = System.console().readLine();
                System.out.println("Nom du magasin :");
                String nomMagasin = System.console().readLine();
                Magasin magasin = new Magasin(nomMagasin);
                admin.creerVendeur(nom, prenom, magasin);
                System.out.println("Vendeur créé !");
            } else if (commande.equals("a")) {
                System.out.println("Nom de la librairie :");
                String nom = System.console().readLine();
                System.out.println("Ville :");
                String ville = System.console().readLine();
                admin.ajouterLibrairie(nom, ville);
                System.out.println("Librairie ajoutée !");
            } else if (commande.equals("g")) {
                System.out.println("Gestion des stocks (fonctionnalité à implémenter)");
            } else if (commande.equals("s")) {
                System.out.println("Statistiques de vente (fonctionnalité à implémenter)");
            } else {
                System.out.println("Commande inconnue.");
            }
        }
    }

    public void menuVendeur(Vendeur vendeur) {
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

            String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("r")) {
                commande_faite = true;
            } else if (commande.equals("a")) {
                System.out.println("ISBN du livre :");
                String isbn = System.console().readLine();
                System.out.println("Titre :");
                String titre = System.console().readLine();
                System.out.println("Classification :");
                String classification = System.console().readLine();
                System.out.println("Prix :");
                double prix = Double.parseDouble(System.console().readLine());
                Livre livre = new Livre(isbn, titre, classification, prix, new ArrayList<>(), null);
                System.out.println("Quantité :");
                int quantite = Integer.parseInt(System.console().readLine());
                vendeur.ajouterLivre(livre, quantite);
            } else if (commande.equals("m")) {
                System.out.println("ISBN du livre :");
                String isbn = System.console().readLine();
                System.out.println("Quantité à ajouter :");
                int quantite = Integer.parseInt(System.console().readLine());
                Livre livre = new Livre(isbn, "Titre", "Classification", 0.0, new ArrayList<>(), null);
                vendeur.majQuantiteLivre(livre, quantite);
            } else if (commande.equals("d")) {
                System.out.println("Nom du livre :");
                String nomLivre = System.console().readLine();
                boolean dispo = vendeur.livreDisponible(nomLivre);
                System.out.println("Disponible : " + dispo);
            } else if (commande.equals("p")) {
                System.out.println("ID du client :");
                String idClient = System.console().readLine();
                Client client = getClientFromDB(idClient);
                if (client == null) {
                    System.out.println("Client introuvable.");
                    continue;
                }
                System.out.println("Combien de livres à commander ?");
                int nbLivres = Integer.parseInt(System.console().readLine());
                ArrayList<Livre> livres = new ArrayList<>();
                for (int i = 0; i < nbLivres; i++) {
                    System.out.println("ISBN du livre " + (i + 1) + " :");
                    String isbn = System.console().readLine();
                    Livre livre = new Livre(isbn, "Titre", "Classification", 0.0, new ArrayList<>(), null);
                    livres.add(livre);
                }
                System.out.println("Commande en ligne ? (o/n) :");
                boolean enLigne = System.console().readLine().trim().equalsIgnoreCase("o");
                System.out.println("Type de livraison (L/M/R) :");
                char typeLivraison = System.console().readLine().trim().toUpperCase().charAt(0);
                vendeur.passerCommandePourClient(enLigne, String.valueOf(typeLivraison), livres, client);
            } else if (commande.equals("t")) {
                System.out.println("ISBN du livre à transférer :");
                String isbn = System.console().readLine();
                Livre livre = new Livre(isbn, "Titre", "Classification", 0.0, new ArrayList<>(), null);
                System.out.println("Quantité à transférer :");
                int quantite = Integer.parseInt(System.console().readLine());
                System.out.println("ID du magasin de destination :");
                String idMagasin = System.console().readLine();
                Magasin magasinDest = new Magasin(idMagasin);
                vendeur.transfertLivre(livre, quantite, magasinDest);
            } else {
                System.out.println("Commande inconnue.");
            }
        }
    }

    public void menuClient(Client client) {
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

            String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("r")) {
                commande_faite = true;
            } else if (commande.equals("p")) {
                System.out.println("Combien de livres voulez-vous commander ?");
                int nbLivres = Integer.parseInt(System.console().readLine());
                ArrayList<Livre> livres = new ArrayList<>();
                for (int i = 0; i < nbLivres; i++) {
                    System.out.println("ISBN du livre " + (i + 1) + " :");
                    String isbn = System.console().readLine();
                    Livre livre = new Livre(isbn, "Titre", "Classification", 0.0, new ArrayList<>(), null);
                    livres.add(livre);
                }
                System.out.println("Commande en ligne ? (o/n) :");
                boolean enLigne = System.console().readLine().trim().equalsIgnoreCase("o");
                System.out.println("Type de livraison (L/M/R) :");
                char typeLivraison = System.console().readLine().trim().toUpperCase().charAt(0);
                System.out.println("Nom du magasin pour la commande :");
                String nomMagasin = System.console().readLine();
                Magasin magasin = new Magasin(nomMagasin);
                client.passerCommande(enLigne, typeLivraison, livres, magasin);
                System.out.println("Commande passée !");
            } else if (commande.equals("m")) {
                // Choisir le mode de réception
                System.out.println("Nouveau mode de réception (L/M/R) :");
                System.out.println("Mode de réception mis à jour !");
            } else if (commande.equals("c")) {
                System.out.println("Catalogue :");
                System.out.println(client.consulterCatalogue(this.connexionSQL.getConnection()));
            } else {
                System.out.println("Commande inconnue.");
            }
        }
    }

    /// Affiche un message d'au revoir
    public void quitter() {
        System.out.println("╭───────────────────────────────────────────────────────╮");
        System.out.println("│ Au revoir, la librairie reste ouverte et accessible ! │");
        System.out.println("╰───────────────────────────────────────────────────────╯");
    }
}
