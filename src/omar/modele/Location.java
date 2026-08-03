package omar.modele;

/**
 * Représente une transaction de location complétée (nombre de jours,
 * montant facturé, kilomètres parcourus) — utilisée pour l'historique
 * et les statistiques de revenu.
 */
public class Location {

    private String immatriculation;
    private int nombreJours;
    private double montant;
    private int kilometresParcourus;

    public Location(String immatriculation,
                    int nombreJours,
                    double montant,
                    int kilometresParcourus) {

        this.immatriculation = immatriculation;
        this.nombreJours = nombreJours;
        this.montant = montant;
        this.kilometresParcourus = kilometresParcourus;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public int getNombreJours() {
        return nombreJours;
    }

    public double getMontant() {
        return montant;
    }

    public int getKilometresParcourus() {
        return kilometresParcourus;
    }

    @Override
    public String toString() {
        return "Location{" +
                "immatriculation='" + immatriculation + '\'' +
                ", nombreJours=" + nombreJours +
                ", montant=" + montant +
                ", kilometresParcourus=" + kilometresParcourus +
                '}';
    }
}
