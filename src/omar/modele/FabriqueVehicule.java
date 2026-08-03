package omar.modele;

import omar.exceptions.DonneesVehiculeInvalidesException;

import java.util.HashMap;
import java.util.Map;

/**
 * Fabrique responsable de la création des véhicules selon leur type.
 *
 * Démontre le principe OCP (Open/Closed Principle) : pour ajouter un
 * nouveau type de véhicule (par ex. Fourgonnette), il suffit de :
 *   1) créer la classe Fourgonnette qui hérite de Vehicule,
 *   2) ajouter UNE ligne dans le bloc static ci-dessous :
 *      enregistrerType("fourgonnette", Fourgonnette::new);
 *
 * GestionFlotte n'a alors jamais besoin d'être modifié pour supporter
 * ce nouveau type.
 */
public class FabriqueVehicule {

    @FunctionalInterface
    private interface VehiculeCreator {
        Vehicule creer(String immatriculation, String marque, String modele,
                        int annee, double kilometrage,
                        boolean disponible, double tarifJournalier);
    }

    private static final Map<String, VehiculeCreator> CREATEURS = new HashMap<>();

    static {
        enregistrerType("voiture", Voiture::new);
        enregistrerType("camion", Camion::new);
        enregistrerType("moto", Moto::new);
        // Pour ajouter un type futur, par exemple :
        // enregistrerType("fourgonnette", Fourgonnette::new);
    }

    private FabriqueVehicule() {
        // Empêche la création d'un objet FabriqueVehicule
    }

    public static void enregistrerType(String type, VehiculeCreator createur) {
        CREATEURS.put(type.toLowerCase(), createur);
    }

    public static Vehicule creerVehicule(
            String type,
            String immatriculation,
            String marque,
            String modele,
            int annee,
            double kilometrage,
            boolean disponible,
            double tarifJournalier
    ) throws DonneesVehiculeInvalidesException {

        String cle = (type == null) ? "" : type.trim().toLowerCase();
        VehiculeCreator createur = CREATEURS.get(cle);

        if (createur == null) {
            throw new DonneesVehiculeInvalidesException("Type de véhicule inconnu : " + type);
        }

        return createur.creer(immatriculation, marque, modele, annee, kilometrage, disponible, tarifJournalier);
    }
}
