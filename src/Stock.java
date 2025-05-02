import java.util.Map;
import java.util.HashMap;

public class Stock {
    private Magasin magasin;
    private Map<Livre,int> quantiteLivre;
    private Double valeurStock;

    public Stock(Magasin magasin) {
        this.magasin = magasin;
        this.quantiteLivre = new HashMap<>()
        this.valeurStock = 0.0;
    }

    public void ajouterLivre(Livre livre, int quantite) {
        if (quantiteLivre.containsKey(livre)) {
            System.out.println("Le livre est déjà présent dans le stock.");
        } else {
            quantiteLivre.put(livre, quantite);
        }
    }

    public void majQuantiteLivre(Livre livre, int quantite) {
        if (quantiteLivre.containsKey(livre)) {
            quantiteLivre.put(livre, quantiteLivre.get(livre) + quantite);
        } else {
            quantiteLivre.put(livre, quantite);
        }
    }

    public Double getValeurStock() {
        Double valeur = 0.0;
        for (Map.Entry<Livre, Integer> entry : quantiteLivre.entrySet()) {
            Livre livre = entry.getKey();
            Integer quantite = entry.getValue();
            valeur += livre.getPrix() * quantite;
        }
        return valeur;
    }
}

