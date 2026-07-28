package omar;

import omar.exceptions.VehiculeIndisponibleException;
import omar.modele.Vehicule;
import omar.services.GestionFlotte;
import omar.modele.Voiture;
import omar.modele.Camion;
import omar.modele.Moto;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        GestionFlotte gestionFlotte = new GestionFlotte();

        // Chargement des fichiers
        System.out.println("===== CHARGEMENT DES DONNÉES =====");

        gestionFlotte.chargerDepuisCSV("vehicules.csv");
        gestionFlotte.chargerLocationsDepuisCSV("locations.csv");

        System.out.println("Chargement terminé.");

        int choix;

        do {
            afficherMenu();

            System.out.print("Votre choix : ");

            while (!scanner.hasNextInt()) {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.nextLine();
                System.out.print("Votre choix : ");
            }

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {

                case 1:
                    System.out.println("\n===== LISTE DE LA FLOTTE =====");
                    gestionFlotte.afficherFlotte();
                    break;

                case 2:
                    rechercherParImmatriculation(
                            scanner,
                            gestionFlotte
                    );
                    break;

                case 3:
                    rechercherParMarque(
                            scanner,
                            gestionFlotte
                    );
                    break;

                case 4:
                    rechercherParType(
                            scanner,
                            gestionFlotte
                    );
                    break;

                case 5:
                    rechercherParAnnee(
                            scanner,
                            gestionFlotte
                    );
                    break;

                case 6:
                    gestionFlotte.afficherVehiculesDisponibles();
                    break;

                case 7:
                    louerVehicule(
                            scanner,
                            gestionFlotte
                    );
                    break;

                case 8:
                    retournerVehicule(
                            scanner,
                            gestionFlotte
                    );
                    break;

                case 9:
                    envoyerEnEntretien(
                            scanner,
                            gestionFlotte
                    );
                    break;

                case 10:
                    afficherStatistiques(
                            gestionFlotte
                    );
                    break;

                case 11:
                    gestionFlotte
                            .afficherVehiculesNecessitantEntretien();
                    break;

                case 12:
                    genererRapport(
                            gestionFlotte
                    );
                    break;
                case 13:
                    gestionFlotte.afficherHistoriqueLocations();
                    break;
                case 0:
                    System.out.println(
                            "\nFermeture de l'application."
                    );
                    break;

                default:
                    System.out.println(
                            "Choix invalide. Veuillez réessayer."
                    );
            }

        } while (choix != 0);

        scanner.close();
    }

    private static void afficherMenu() {

        System.out.println(
                "\n======================================"
        );

        System.out.println(
                "       GESTION DE LA FLOTTE"
        );

        System.out.println(
                "======================================"
        );

        System.out.println("1. Afficher toute la flotte");
        System.out.println(
                "2. Rechercher par immatriculation"
        );
        System.out.println("3. Rechercher par marque");
        System.out.println("4. Rechercher par type");
        System.out.println("5. Rechercher par année");
        System.out.println(
                "6. Afficher les véhicules disponibles"
        );
        System.out.println("7. Louer un véhicule");
        System.out.println("8. Retourner un véhicule");
        System.out.println(
                "9. Envoyer un véhicule en entretien"
        );
        System.out.println(
                "10. Afficher les statistiques"
        );
        System.out.println(
                "11. Afficher les véhicules nécessitant un entretien"
        );
        System.out.println(
                "12. Générer le rapport TXT"
        );System.out.println("13. Afficher l'historique des locations");
        System.out.println("0. Quitter");
        System.out.println(
                "======================================"
        );
    }

    private static void rechercherParImmatriculation(
            Scanner scanner,
            GestionFlotte gestionFlotte
    ) {

        System.out.print(
                "Entrez l'immatriculation : "
        );

        String immatriculation =
                scanner.nextLine().trim();

        Vehicule vehicule =
                gestionFlotte.rechercherVehicule(
                        immatriculation
                );

        if (vehicule != null) {
            System.out.println(
                    "\nVéhicule trouvé :"
            );
            System.out.println(vehicule);
        } else {
            System.out.println(
                    "Aucun véhicule trouvé avec cette immatriculation."
            );
        }
    }

    private static void rechercherParMarque(
            Scanner scanner,
            GestionFlotte gestionFlotte
    ) {

        System.out.print(
                "Entrez la marque : "
        );

        String marque =
                scanner.nextLine().trim();

        gestionFlotte.rechercherParMarque(
                marque
        );
    }

    private static void rechercherParType(
            Scanner scanner,
            GestionFlotte gestionFlotte
    ) {

        System.out.print(
                "Entrez le type (Voiture, Camion ou Moto) : "
        );

        String type =
                scanner.nextLine().trim();

        gestionFlotte.rechercherParType(
                type
        );
    }

    private static void rechercherParAnnee(
            Scanner scanner,
            GestionFlotte gestionFlotte
    ) {

        System.out.print(
                "Entrez l'année : "
        );

        while (!scanner.hasNextInt()) {
            System.out.println(
                    "Veuillez entrer une année valide."
            );
            scanner.nextLine();
            System.out.print(
                    "Entrez l'année : "
            );
        }

        int annee = scanner.nextInt();
        scanner.nextLine();

        gestionFlotte.rechercherParAnnee(
                annee
        );
    }

    private static void louerVehicule(
            Scanner scanner,
            GestionFlotte gestionFlotte
    ) {

        System.out.print(
                "Entrez l'immatriculation du véhicule : "
        );

        String immatriculation =
                scanner.nextLine().trim();

        System.out.print(
                "Entrez le nombre de jours : "
        );

        while (!scanner.hasNextInt()) {
            System.out.println(
                    "Veuillez entrer un nombre entier valide."
            );
            scanner.nextLine();
            System.out.print(
                    "Entrez le nombre de jours : "
            );
        }

        int nombreJours = scanner.nextInt();
        scanner.nextLine();

        if (nombreJours <= 0) {
            System.out.println(
                    "Le nombre de jours doit être supérieur à zéro."
            );
            return;
        }

        try {
            gestionFlotte.louerVehicule(
                    immatriculation
            );

            System.out.println(
                    "Location effectuée avec succès."
            );

        } catch (VehiculeIndisponibleException e) {

            System.out.println(
                    "Erreur de location : "
                            + e.getMessage()
            );

        } catch (Exception e) {

            System.out.println(
                    "Erreur : "
                            + e.getMessage()
            );
        }
    }

    private static void retournerVehicule(
            Scanner scanner,
            GestionFlotte gestionFlotte
    ) {

        System.out.print(
                "Entrez l'immatriculation du véhicule : "
        );

        String immatriculation =
                scanner.nextLine().trim();

        System.out.print(
                "Entrez les kilomètres parcourus : "
        );

        while (!scanner.hasNextDouble()) {
            System.out.println(
                    "Veuillez entrer une valeur numérique valide."
            );
            scanner.nextLine();
            System.out.print(
                    "Entrez les kilomètres parcourus : "
            );
        }

        double kilometres =
                scanner.nextDouble();

        scanner.nextLine();

        if (kilometres < 0) {
            System.out.println(
                    "Le kilométrage ne peut pas être négatif."
            );
            return;
        }

        try {
            gestionFlotte.retournerVehicule(
                    immatriculation
            );

            System.out.println(
                    "Retour du véhicule effectué avec succès."
            );

        } catch (Exception e) {

            System.out.println(
                    "Erreur lors du retour : "
                            + e.getMessage()
            );
        }
    }

    private static void envoyerEnEntretien(
            Scanner scanner,
            GestionFlotte gestionFlotte
    ) {

        System.out.print(
                "Entrez l'immatriculation du véhicule : "
        );

        String immatriculation =
                scanner.nextLine().trim();

        try {
            gestionFlotte.envoyerEnEntretien(
                    immatriculation
            );

            System.out.println(
                    "Le véhicule a été envoyé en entretien."
            );

        } catch (Exception e) {

            System.out.println(
                    "Erreur lors de la mise en entretien : "
                            + e.getMessage()
            );
        }
    }

    private static void afficherStatistiques(
            GestionFlotte gestionFlotte
    ) {

        System.out.println(
                "\n===== STATISTIQUES DE LA FLOTTE ====="
        );

        System.out.println(
                "Nombre total de véhicules : "
                        + gestionFlotte.compterVehicules()
        );

        System.out.println(
                "Nombre de véhicules disponibles : "
                        + gestionFlotte
                        .compterVehiculesDisponibles()
        );

        System.out.println(
                "Nombre de véhicules en entretien : "
                        + gestionFlotte
                        .compterVehiculesEnEntretien()
        );

        System.out.printf(
                "Kilométrage moyen : %.2f km%n",
                gestionFlotte
                        .calculerKilometrageMoyen()
        );

        System.out.printf(
                "Tarif journalier moyen : %.2f $%n",
                gestionFlotte
                        .calculerTarifJournalierMoyen()
        );

        System.out.printf(
                "Revenu total : %.2f $%n",
                gestionFlotte
                        .calculerRevenuTotal()
        );

        Vehicule vehiculeLePlusUtilise =
                gestionFlotte
                        .trouverVehiculeLePlusUtilise();

        if (vehiculeLePlusUtilise != null) {

            System.out.println(
                    "Véhicule le plus utilisé : "
                            + vehiculeLePlusUtilise
            );

            System.out.println(
                    "Nombre de locations : "
                            + gestionFlotte
                            .compterLocationsVehicule(
                                    vehiculeLePlusUtilise
                                            .getImmatriculation()
                            )
            );

        } else {
            System.out.println(
                    "Aucune location enregistrée."
            );
        }
        System.out.println(
                "Taux d'utilisation des voitures : "
                        + gestionFlotte.calculerTauxUtilisationParType(Voiture.class)
                        + " %"
        );

        System.out.println(
                "Taux d'utilisation des camions : "
                        + gestionFlotte.calculerTauxUtilisationParType(Camion.class)
                        + " %"
        );

        System.out.println(
                "Taux d'utilisation des motos : "
                        + gestionFlotte.calculerTauxUtilisationParType(Moto.class)
                        + " %"
        );
    }

    private static void genererRapport(
            GestionFlotte gestionFlotte
    ) {

        try {
            gestionFlotte.genererRapportTXT(
                    "rapport_flotte.txt"
            );

            System.out.println(
                    "Le rapport rapport_flotte.txt a été généré avec succès."
            );

        } catch (Exception e) {

            System.out.println(
                    "Erreur pendant la génération du rapport : "
                            + e.getMessage()
            );
        }
    }
}