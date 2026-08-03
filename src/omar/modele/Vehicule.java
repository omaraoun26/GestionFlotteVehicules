package omar.modele;
import omar.interfaces.Louable;
import omar.interfaces.Entretenable;
import omar.exceptions.KilometrageInvalideException;

/**
 * Classe abstraite regroupant les attributs et comportements communs à
 * tout véhicule de la flotte. Chaque sous-classe (Voiture, Camion, Moto)
 * définit sa propre règle de calcul de tarif.
 */
public abstract class Vehicule implements Louable, Entretenable {

    private String immatriculation;
    private String marque;
    private String modele;
    private int annee;
    private double kilometrage;
    private boolean disponible;
    private double tarifJournalier;
    private boolean entretienRequis;
    private String etat;

    public Vehicule(String immatriculation, String marque, String modele,
                    int annee, double kilometrage,
                    boolean disponible, double tarifJournalier) {

        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.kilometrage = kilometrage;
        this.disponible = disponible;
        this.tarifJournalier = tarifJournalier;
        this.entretienRequis = false;
        this.etat = "Excellent";
    }
    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public double getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(double kilometrage) {
        this.kilometrage = kilometrage;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public double getTarifJournalier() {
        return tarifJournalier;
    }

    public void setTarifJournalier(double tarifJournalier) {
        this.tarifJournalier = tarifJournalier;
    }
    @Override
    public String toString() {
        return "Vehicule{" +
                "immatriculation='" + immatriculation + '\'' +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", annee=" + annee +
                ", kilometrage=" + kilometrage +
                ", disponible=" + disponible +
                ", tarifJournalier=" + tarifJournalier +
                ", entretienRequis=" + entretienRequis +
                ", etat='" + etat + '\'' +
                '}';

    }
    // Chaque sous-classe applique sa propre formule de tarification.
    public abstract double calculerTarifLocation(int nombreJours);
    @Override
    public void louer() {
        disponible = false;
    }

    // Valide le kilométrage, puis l'ajoute au total du véhicule.
    @Override
    public void retourner(double kilometresParcourus)
            throws KilometrageInvalideException {

        if (kilometresParcourus < 0) {
            throw new KilometrageInvalideException(
                    "Le kilométrage parcouru ne peut pas être négatif."
            );
        }

        this.kilometrage += kilometresParcourus;
        disponible = true;
    }

    // Remet le véhicule en bon état et le rend de nouveau disponible.
    @Override
    public void effectuerEntretien() {
        entretienRequis = false;
        etat = "Excellent";
        disponible = true;
    }
    public boolean isEntretienRequis() {
        return entretienRequis;
    }

    public void setEntretienRequis(boolean entretienRequis) {
        this.entretienRequis = entretienRequis;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
