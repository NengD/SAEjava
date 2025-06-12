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
                    "SELECT idcli FROM CLIENT WHERE idcli = ?");
            ps.setInt(1, Integer.parseInt(idClient));
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idcli = rs.getInt("idcli");
                rs.close();
                ps.close();
                return new Client(idcli, this.connexionSQL);
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
                // Ici, seul l'ISBN doit être utilisé pour créer l'objet Livre
                Livre livre = new Livre(isbn);
                System.out.println("Quantité :");
                int quantite = Integer.parseInt(System.console().readLine());
                vendeur.ajouterLivre(livre, quantite);
            } else if (commande.equals("m")) {
                System.out.println("ISBN du livre :");
                String isbn = System.console().readLine();
                System.out.println("Quantité à ajouter :");
                int quantite = Integer.parseInt(System.console().readLine());
                Livre livre = new Livre(isbn);
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
                    Livre livre = new Livre(isbn);
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
                Livre livre = new Livre(isbn);
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
            System.out.println("O: On vous recommande");

            String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            if (commande.equals("r")) {
                commande_faite = true;
            } else if (commande.equals("p")) {
                System.out.println("Nom du livre à commander :");
                String nomLivre = System.console().readLine();
                Livre livreTrouve = null;
                for (Livre l : Client.consulterCatalogue(this.connexionSQL.getConnection())) {
                    if (l.getTitre(this.connexionSQL.getConnection()).equalsIgnoreCase(nomLivre)) {
                        livreTrouve = l;
                        break;
                    }
                }
                if (livreTrouve == null) {
                    System.out.println("Livre non trouvé dans le catalogue.");
                    return;
                }
                System.out.println("Quantité à commander :");
                int quantite = Integer.parseInt(System.console().readLine());
                System.out.println("Nom du magasin pour la commande :");
                String nomMagasin = System.console().readLine();
                Magasin magasin = new Magasin(nomMagasin);
                System.out.println("Commande en ligne ? (o/n) :");
                boolean enLigne = System.console().readLine().trim().equalsIgnoreCase("o");
                char typeLivraison;
                if (enLigne) {
                    typeLivraison = 'L';
                } else {
                    System.out.println("Type de livraison (M pour magasin, R pour relais) :");
                    typeLivraison = System.console().readLine().trim().toUpperCase().charAt(0);
                }
                ArrayList<Livre> livres = new ArrayList<>();
                for (int i = 0; i < quantite; i++) {
                    livres.add(livreTrouve);
                }
                client.passerCommande(enLigne, typeLivraison, livres, magasin);
                System.out.println("Commande passée !");
            } else if (commande.equals("m")) {
                System.out.println("Nouveau mode de réception (L/M/R) :");
                System.out.println("Mode de réception mis à jour !");
            } else if (commande.equals("c")) {
                System.out.println("Catalogue :");
                for (Livre l : Client.consulterCatalogue(this.connexionSQL.getConnection())) {
                    System.out.println(l.getIsbn() + " - " + l.getTitre(this.connexionSQL.getConnection()));
                }
            } else if (commande.equals("o")) {
                System.out.println("Livres recommandés pour vous :");
                for (String titre : client.onVousRecommande()) {
                    System.out.println("- " + titre);
                }
            } else {
                System.out.println("Commande inconnue.");
            }
        }
    }

    public void quitter() {
        System.out.println("╭───────────────────────────────────────────────────────╮");
        System.out.println("│ Au revoir, la librairie reste ouverte et accessible ! │");
        System.out.println("╰───────────────────────────────────────────────────────╯");
    }
}