package omar.modele;

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