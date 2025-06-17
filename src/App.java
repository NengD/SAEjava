import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Classe principale de l'application de gestion de librairie.
 * Gère la connexion à la base de données et les menus utilisateurs.
 */
public class App {

    private ConnexionMySQL connexionSQL;
    public boolean quitter;

    /**
     * Constructeur de l'application.
     * Initialise la connexion à la base de données.
     */
    public App() {
        this.quitter = false;
        try {
            this.connexionSQL = new ConnexionMySQL();
            this.connexionSQL.connecter();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion à la base : " + e.getMessage());
        }
    }

    /**
     * Lance la boucle principale de l'application.
     */
    public void lancer() {
        while (!this.quitter) {
            menuConnexion();
        }
        quitter();
    }

    /**
     * Affiche le menu de sélection utilisateur (Admin, Vendeur, Client, Quitter).
     */
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

            switch (commande) {
                case "q":
                    this.quitter = true;
                    commande_faite = true;
                    break;
                case "a":
                    System.out.println("Entrez l'ID de l'administrateur :");
                    String idAdmin = System.console().readLine();
                    Administrateur admin = getAdminFromDB(idAdmin);
                    if (admin != null) {
                        menuAdmin(admin);
                    } else {
                        System.out.println("Administrateur introuvable.");
                    }
                    break;
                case "v":
                    System.out.println("Entrez l'ID du vendeur :");
                    String idVendeur = System.console().readLine();
                    Vendeur vendeur = getVendeurFromDB(idVendeur);
                    if (vendeur != null) {
                        menuVendeur(vendeur);
                    } else {
                        System.out.println("Vendeur introuvable.");
                    }
                    break;
                case "c":
                    System.out.println("Entrez l'ID du client :");
                    String idClient = System.console().readLine();
                    Client client = getClientFromDB(idClient);
                    if (client != null) {
                        menuClient(client);
                    } else {
                        System.out.println("Client introuvable.");
                    }
                    break;
                default:
                    System.out.println("Commande inconnue.");
            }
        }
    }

    /**
     * Récupère un client depuis la base de données.
     * @param idClient ID du client
     * @return Client ou null si non trouvé
     */
    public Client getClientFromDB(String idClient) {
        try {
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                "SELECT idcli FROM CLIENT WHERE idcli = ?");
            ps.setInt(1, Integer.parseInt(idClient));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idcli = rs.getInt("idcli");
                rs.close();
                ps.close();
                return new Client(idcli, this.connexionSQL);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère un vendeur depuis la base de données.
     * @param idVendeur ID du vendeur
     * @return Vendeur ou null si non trouvé
     */
    public Vendeur getVendeurFromDB(String idVendeur) {
        try {
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                "SELECT idven, idmag FROM VENDEUR WHERE idven = ?");
            ps.setInt(1, Integer.parseInt(idVendeur));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idven = rs.getInt("idven");
                int idmag = rs.getInt("idmag");
                rs.close();
                ps.close();
                return new Vendeur(idven, this.connexionSQL, idmag);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du vendeur : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère un administrateur depuis la base de données.
     * @param idAdmin ID de l'administrateur
     * @return Administrateur ou null si non trouvé
     */
    public Administrateur getAdminFromDB(String idAdmin) {
        try {
            PreparedStatement ps = this.connexionSQL.prepareStatement(
                "SELECT idadm, prenomadm, nomadm FROM ADMINISTRATEUR WHERE idadm = ?");
            ps.setInt(1, Integer.parseInt(idAdmin));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idadm = rs.getInt("idadm");
                rs.close();
                ps.close();
                return new Administrateur(idadm, this.connexionSQL);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de l'administrateur : " + e.getMessage());
        }
        return null;
    }

    /**
     * Affiche le menu administrateur et gère ses actions.
     * @param admin Administrateur connecté
     */
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
            System.out.println("S: Consulter les statistiques de vente");
            System.out.println("T: Transférer un livre d'un magasin à un autre");
            System.out.println("L: Créer un livre");

            String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            switch (commande) {
                case "r":
                    commande_faite = true;
                    break;
                case "c":
                    System.out.println("Nom du vendeur :");
                    String nom = System.console().readLine();
                    System.out.println("Prénom du vendeur :");
                    String prenom = System.console().readLine();
                    System.out.println("ID du magasin :");
                    int idmag = Integer.parseInt(System.console().readLine());
                    admin.creerVendeur(nom, prenom, idmag);
                    break;
                case "a":
                    System.out.println("Nom de la librairie :");
                    String nomLib = System.console().readLine();
                    System.out.println("Ville :");
                    String ville = System.console().readLine();
                    admin.ajouterLibrairie(nomLib, ville);
                    break;
                case "s":
                    admin.consulterStatisques();
                    break;
                case "t":
                    System.out.println("ISBN du livre à transférer :");
                    String isbn = System.console().readLine();
                    System.out.println("Quantité à transférer :");
                    int quantite = Integer.parseInt(System.console().readLine());
                    System.out.println("ID du magasin source :");
                    int idMagSource = Integer.parseInt(System.console().readLine());
                    System.out.println("ID du magasin de destination :");
                    int idMagDest = Integer.parseInt(System.console().readLine());
                    admin.transfertLivreEntreMagasins(isbn, quantite, idMagSource, idMagDest);
                    break;
                case "l":
                    System.out.println("ISBN du livre :");
                    String isbnLivre = System.console().readLine();
                    System.out.println("Titre du livre :");
                    String titre = System.console().readLine();
                    System.out.println("Nombre de pages :");
                    int nbpages = Integer.parseInt(System.console().readLine());
                    System.out.println("Année de publication :");
                    int datepubli = Integer.parseInt(System.console().readLine());
                    System.out.println("Prix :");
                    double prix = Double.parseDouble(System.console().readLine());
                    System.out.println("ID Dewey (classification, laisser vide si aucune) :");
                    String iddewey = System.console().readLine().trim();
                    if (iddewey.isEmpty()) iddewey = null;
                    admin.creerLivre(isbnLivre, titre, nbpages, datepubli, prix, iddewey);
                    break;
                default:
                    System.out.println("Commande inconnue.");
            }
        }
    }

    /**
     * Affiche le menu vendeur et gère ses actions.
     * @param vendeur Vendeur connecté
     */
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

            switch (commande) {
                case "r":
                    commande_faite = true;
                    break;
                case "a":
                    System.out.println("ISBN du livre :");
                    String isbn = System.console().readLine();
                    System.out.println("Quantité :");
                    int quantite = Integer.parseInt(System.console().readLine());
                    vendeur.ajouterLivre(isbn, quantite);
                    break;
                case "m":
                    System.out.println("ISBN du livre :");
                    String isbnMaj = System.console().readLine();
                    System.out.println("Quantité à ajouter :");
                    int quantiteMaj = Integer.parseInt(System.console().readLine());
                    Livre livre = new Livre(isbnMaj);
                    vendeur.majQuantiteLivre(livre, quantiteMaj);
                    break;
                case "d":
                    System.out.println("Nom du livre :");
                    String nomLivre = System.console().readLine();
                    boolean dispo = vendeur.livreDisponible(nomLivre);
                    System.out.println("Disponible : " + dispo);
                    break;
                case "p":
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
                        String isbnLivreCmd = System.console().readLine();
                        livres.add(new Livre(isbnLivreCmd));
                    }
                    System.out.println("Commande en ligne ? (o/n) :");
                    boolean enLigne = System.console().readLine().trim().equalsIgnoreCase("o");
                    System.out.println("Type de livraison (M pour retrait en magasin, C pour livraison à domicile) :");
                    char typeLivraison = System.console().readLine().trim().toUpperCase().charAt(0);

                    break;
                case "t":
                    System.out.println("ISBN du livre à transférer :");
                    String isbnTransfert = System.console().readLine();
                    System.out.println("Quantité à transférer :");
                    int quantiteTransfert = Integer.parseInt(System.console().readLine());
                    System.out.println("ID du magasin de destination :");
                    int idMagasin = Integer.parseInt(System.console().readLine());
                    vendeur.transfertLivre(isbnTransfert, quantiteTransfert, idMagasin);
                    break;
                default:
                    System.out.println("Commande inconnue.");
            }
        }
    }

    /**
     * Affiche le menu client et gère ses actions.
     * @param client Client connecté
     */
    public void menuClient(Client client) {
        boolean commande_faite = false;
        while (!commande_faite) {
            System.out.println("╭─────────────╮");
            System.out.println("│ Menu Client │");
            System.out.println("╰─────────────╯");
            System.out.println("Que voulez vous faire?");
            System.out.println("R: Retourner menu principal");
            System.out.println("P: Passer une commande");
            System.out.println("C: Consulter le catalogue");
            System.out.println("O: On vous recommande");

            String commande_brute = System.console().readLine();
            String commande = commande_brute.strip().toLowerCase();

            switch (commande) {
                case "r":
                    commande_faite = true;
                    break;
                case "p":
                    System.out.println("ISBN du livre à commander :");
                    String isbn = System.console().readLine();
                    Livre livreTrouve = null;
                    for (Livre l : Client.consulterCatalogue(this.connexionSQL.getConnection())) {
                        if (l.getIsbn().equalsIgnoreCase(isbn)) {
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
                    System.out.println("ID du magasin pour la commande :");
                    int idMagasin = Integer.parseInt(System.console().readLine());
                    System.out.println("Commande en ligne ? (o/n) :");
                    boolean enLigne = System.console().readLine().trim().equalsIgnoreCase("o");
                    char typeLivraison;
                    if (enLigne) {
                        typeLivraison = 'L';
                    } else {
                        System.out.println("Type de livraison (M pour retrait en magasin, C pour livraison à domicile) :");
                        typeLivraison = System.console().readLine().trim().toUpperCase().charAt(0);
                    }
                    ArrayList<Livre> livres = new ArrayList<>();
                    for (int i = 0; i < quantite; i++) {
                        livres.add(livreTrouve);
                    }
                    System.out.println("Commande passée !");
                    break;
                case "c":
                    System.out.println("Catalogue :");
                    for (Livre l : Client.consulterCatalogue(this.connexionSQL.getConnection())) {
                        System.out.println(l.getIsbn() + " - " + l.getTitre(this.connexionSQL.getConnection()));
                    }
                    break;
                case "o":
                    System.out.println("Livres recommandés pour vous :");
                    for (String titre : client.onVousRecommande()) {
                        System.out.println("- " + titre);
                    }
                    break;
                default:
                    System.out.println("Commande inconnue.");
            }
        }
    }

    /**
     * Affiche un message de fermeture de l'application.
     */
    public void quitter() {
        System.out.println("╭───────────────────────────────────────────────────────╮");
        System.out.println("│ Au revoir, la librairie reste ouverte et accessible ! │");
        System.out.println("╰───────────────────────────────────────────────────────╯");
    }
}