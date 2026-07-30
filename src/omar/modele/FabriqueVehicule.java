package omar.modele;

public class FabriqueVehicule {

    private FabriqueVehicule() {
        // Empêche la création d'un objet FabriqueVehicule
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
    ) {

        return switch (type.toLowerCase()) {
            case "voiture" -> new Voiture(
                    immatriculation,
                    marque,
                    modele,
                    annee,
                    kilometrage,
                    disponible,
                    tarifJournalier
            );

            case "camion" -> new Camion(
                    immatriculation,
                    marque,
                    modele,
                    annee,
                    kilometrage,
                    disponible,
                    tarifJournalier
            );

            case "moto" -> new Moto(
                    immatriculation,
                    marque,
                    modele,
                    annee,
                    kilometrage,
                    disponible,
                    tarifJournalier
            );

            default -> throw new IllegalArgumentException(
                    "Type de véhicule inconnu : " + type
            );
        };
    }
}
