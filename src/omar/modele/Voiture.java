package omar.modele;

/**
 * Tarif : le tarif journalier de base, sans majoration ni rabais.
 */
public class Voiture extends Vehicule {

    public Voiture(String immatriculation, String marque, String modele,
                   int annee, double kilometrage,
                   boolean disponible, double tarifJournalier) {

        super(immatriculation, marque, modele,
                annee, kilometrage,
                disponible, tarifJournalier);
    }

    @Override
    public double calculerTarifLocation(int nombreJours) {
        return getTarifJournalier() * nombreJours;
    }
}