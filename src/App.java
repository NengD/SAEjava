public class App {
    public boolean quitter;

    public App(){
        this.quitter = false;
    }

    public void lancer() {
        while(!this.quitter) {
            menuPrincipal();
        }
        quitter();
    }

    public void menuPrincipal() {
	    boolean commande_faite = false;
	    while(!commande_faite) {
		    System.out.println("╭────────────────╮");
		    System.out.println("│ Menu principal │");
		    System.out.println("╰────────────────╯");
	        System.out.println("Que voulez vous faire?");
            System.out.println("A: Menu Administrateur");
            System.out.println("V: Menu Vendeur");
            System.out.println("C: Menu Client");
	        System.out.println("Q: Quitter");

            /// Majuscule et minuscule prise en compte
	        String commande_brute = System.console().readLine();
	        String commande = commande_brute.strip().toLowerCase();

	        if(commande.equals("q")) {
                this.quitter = true;
                commande_faite = true;}
            else if(commande.equals("a")){
			    menuAdmin();}
		    else if(commande.equals("v")){
			    menuVendeur();}
		    else if(commande.equals("c")){
			    menuClient();}
        }
    }

    public void menuAdmin() {
	    boolean commande_faite = false;
	    while(!commande_faite) {
		    System.out.println("╭─────────────────────╮");
		    System.out.println("│ Menu Administrateur │");
		    System.out.println("╰─────────────────────╯");
	        System.out.println("Que voulez vous faire?");
	        System.out.println("R: Retourner menu principal");

            /// Majuscule et minuscule prise en compte
	        String commande_brute = System.console().readLine();
	        String commande = commande_brute.strip().toLowerCase();

	        if(commande.equals("r")) {
                this.quitter = true;
                commande_faite = true;}
        }
    }
    
    public void menuVendeur() {
	    boolean commande_faite = false;
	    while(!commande_faite) {
		    System.out.println("╭──────────────╮");
		    System.out.println("│ Menu Vendeur │");
		    System.out.println("╰──────────────╯");
	        System.out.println("Que voulez vous faire?");
	        System.out.println("R: Retourner menu principal");

            /// Majuscule et minuscule prise en compte
	        String commande_brute = System.console().readLine();
	        String commande = commande_brute.strip().toLowerCase();

	        if(commande.equals("r")) {
                this.quitter = true;
                commande_faite = true;}
        }
    }

    public void menuClient() {
	    boolean commande_faite = false;
	    while(!commande_faite) {
		    System.out.println("╭─────────────╮");
		    System.out.println("│ Menu Client │");
		    System.out.println("╰─────────────╯");
	        System.out.println("Que voulez vous faire?");
	        System.out.println("R: Retourner menu principal");

            /// Majuscule et minuscule prise en compte
	        String commande_brute = System.console().readLine();
	        String commande = commande_brute.strip().toLowerCase();

	        if(commande.equals("r")) {
                this.quitter = true;
                commande_faite = true;}
        }
    }

    /// Affiche un message d'au revoir
    public void quitter() {
	System.out.println("╭─────────────────────────────────────────╮");
	System.out.println("│ Au revoir, la librairie reste ouverte ! │");
	System.out.println("╰─────────────────────────────────────────╯");
    }
}
