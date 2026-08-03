package omar.services;

import omar.modele.Vehicule;
import omar.exceptions.VehiculeIndisponibleException;
import java.util.ArrayList;
import omar.exceptions.DonneesVehiculeInvalidesException;
import omar.exceptions.KilometrageInvalideException;
import omar.exceptions.EntretienException;
import omar.modele.Voiture;
import omar.modele.Camion;
import omar.modele.Moto;
import omar.modele.FabriqueVehicule;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import omar.modele.Location;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

/**
 * Point central de la logique métier : chargement des données, gestion
 * des locations/retours/entretiens, calcul des statistiques et génération
 * du rapport.
 */
public class GestionFlotte {

    private ArrayList<Vehicule> flotte;
    private ArrayList<Location> historiqueLocations;

    // Mémorise le nombre de jours demandés à la location (clé = immatriculation),
    // pour pouvoir calculer le montant final au moment du retour, une fois le
    // kilométrage parcouru connu.
    private Map<String, Integer> joursLocationEnCours = new HashMap<>();

    public GestionFlotte() {
        flotte = new ArrayList<>();
        historiqueLocations = new ArrayList<>();
    }

    public void ajouterVehicule(Vehicule vehicule) {
        flotte.add(vehicule);
    }

    public void supprimerVehicule(Vehicule vehicule) {
        flotte.remove(vehicule);
    }

    public Vehicule rechercherVehicule(String immatriculation) {

        for (Vehicule v : flotte) {

            if (v.getImmatriculation().equals(immatriculation)) {
                return v;
            }

        }

        return null;
    }

    public void louerVehicule(String immatriculation, int nombreJours)
            throws VehiculeIndisponibleException {

        Vehicule vehicule = rechercherVehicule(immatriculation);

        if (vehicule == null) {
            throw new VehiculeIndisponibleException(
                    "Véhicule introuvable : " + immatriculation
            );
        }

        if (!vehicule.isDisponible()) {
            throw new VehiculeIndisponibleException(
                    "Le véhicule " + immatriculation + " est indisponible."
            );
        }

        vehicule.louer();
        joursLocationEnCours.put(immatriculation, nombreJours);
    }

    // Calcule le montant à partir du nombre de jours mémorisé à la
    // location, puis enregistre la transaction complète dans l'historique.
    public void retournerVehicule(
            String immatriculation,
            double kilometresParcourus
    ) throws KilometrageInvalideException {
        Vehicule vehicule = rechercherVehicule(immatriculation);

        if (vehicule != null) {
            vehicule.retourner(kilometresParcourus);

            int nombreJours = joursLocationEnCours.getOrDefault(immatriculation, 1);
            double montant = vehicule.calculerTarifLocation(nombreJours);

            Location location = new Location(
                    immatriculation,
                    nombreJours,
                    montant,
                    (int) kilometresParcourus
            );
            historiqueLocations.add(location);

            joursLocationEnCours.remove(immatriculation);
        }
    }

    public void afficherFlotte() {

        for (Vehicule v : flotte) {
            System.out.println(v);
        }

    }

    // Charge la flotte depuis un CSV (type,immatriculation,marque,modele,
    // annee,kilometrage,disponible,tarifJournalier). Une ligne invalide est
    // signalée puis ignorée, sans interrompre le chargement des autres.
    public void chargerDepuisCSV(String nomFichier) {

        try (BufferedReader lecteur =
                     new BufferedReader(new FileReader(nomFichier))) {

            String ligne;
            int numeroLigne = 0;

            while ((ligne = lecteur.readLine()) != null) {

                numeroLigne++;

                if (numeroLigne == 1 || ligne.trim().isEmpty()) {
                    continue;
                }

                try {

                    String[] colonnes = ligne.split(",");

                    if (colonnes.length != 8) {
                        throw new DonneesVehiculeInvalidesException(
                                "Nombre de colonnes invalide"
                        );
                    }

                    String type = colonnes[0].trim();
                    String immatriculation = colonnes[1].trim();
                    String marque = colonnes[2].trim();
                    String modele = colonnes[3].trim();

                    int annee = Integer.parseInt(colonnes[4].trim());
                    double kilometrage =
                            Double.parseDouble(colonnes[5].trim());

                    String disponibiliteTexte = colonnes[6].trim();

                    if (!disponibiliteTexte.equalsIgnoreCase("true")
                            && !disponibiliteTexte.equalsIgnoreCase("false")) {

                        throw new DonneesVehiculeInvalidesException(
                                "Disponibilité invalide"
                        );
                    }

                    boolean disponible =
                            Boolean.parseBoolean(disponibiliteTexte);

                    double tarifJournalier =
                            Double.parseDouble(colonnes[7].trim());

                    if (annee < 1900 || annee > 2026) {
                        throw new DonneesVehiculeInvalidesException(
                                "Année invalide"
                        );
                    }

                    if (kilometrage < 0) {
                        throw new DonneesVehiculeInvalidesException(
                                "Kilométrage invalide"
                        );
                    }

                    if (tarifJournalier < 0) {
                        throw new DonneesVehiculeInvalidesException(
                                "Tarif journalier invalide"
                        );
                    }

                    Vehicule vehicule;

                    vehicule = FabriqueVehicule.creerVehicule(
                            type,
                            immatriculation,
                            marque,
                            modele,
                            annee,
                            kilometrage,
                            disponible,
                            tarifJournalier
                    );

                    ajouterVehicule(vehicule);

                } catch (DonneesVehiculeInvalidesException
                         | NumberFormatException e) {

                    System.out.println(
                            "Erreur à la ligne " + numeroLigne
                                    + " : " + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Erreur de lecture du fichier : " + e.getMessage()
            );
        }
    }

    // Signale un véhicule en entretien : rejette un véhicule déjà en
    // entretien ou actuellement loué (voir EntretienException).
    public void envoyerEnEntretien(String immatriculation)
            throws EntretienException {

        Vehicule vehicule = rechercherVehicule(immatriculation);
        if (vehicule != null && vehicule.isEntretienRequis()) {
            throw new EntretienException(
                    "Le véhicule est déjà en entretien."
            );
        }
        if (vehicule != null && !vehicule.isDisponible()) {
            throw new EntretienException(
                    "Impossible d'envoyer un véhicule loué en entretien."
            );
        }
        if (vehicule != null) {
            vehicule.setEntretienRequis(true);
            vehicule.setEtat("En entretien");
            vehicule.setDisponible(false);
        }
    }

    public int compterVehicules() {
        return flotte.size();
    }

    public int compterVehiculesDisponibles() {

        int compteur = 0;

        for (Vehicule vehicule : flotte) {
            if (vehicule.isDisponible()) {
                compteur++;
            }
        }

        return compteur;
    }

    public int compterVehiculesEnEntretien() {

        int compteur = 0;

        for (Vehicule vehicule : flotte) {
            if (vehicule.isEntretienRequis()) {
                compteur++;
            }
        }

        return compteur;
    }

    public double calculerKilometrageMoyen() {

        if (flotte.isEmpty()) {
            return 0;
        }

        double total = 0;

        for (Vehicule vehicule : flotte) {
            total += vehicule.getKilometrage();
        }

        return total / flotte.size();
    }

    public double calculerTarifJournalierMoyen() {

        if (flotte.isEmpty()) {
            return 0;
        }

        double total = 0;

        for (Vehicule vehicule : flotte) {
            total += vehicule.getTarifJournalier();
        }

        return total / flotte.size();
    }

    // Charge l'historique des locations depuis un CSV (utilisé pour les
    // statistiques de revenu et d'utilisation), avec la même logique de
    // validation ligne par ligne que chargerDepuisCSV.
    public void chargerLocationsDepuisCSV(String nomFichier) {

        try (BufferedReader lecteur = new BufferedReader(new FileReader(nomFichier))) {

            String ligne;
            int numeroLigne = 0;

            while ((ligne = lecteur.readLine()) != null) {

                numeroLigne++;

                if (numeroLigne == 1) {
                    continue;
                }

                try {

                    String[] donnees = ligne.split(",");

                    if (donnees.length != 4) {
                        throw new DonneesVehiculeInvalidesException(
                                "Nombre de colonnes invalide"
                        );
                    }

                    String immatriculation = donnees[0].trim();
                    int nombreJours = Integer.parseInt(donnees[1].trim());
                    double montant = Double.parseDouble(donnees[2].trim());
                    int kilometresParcourus = Integer.parseInt(donnees[3].trim());

                    if (immatriculation.isEmpty()) {
                        throw new DonneesVehiculeInvalidesException(
                                "Immatriculation manquante"
                        );
                    }

                    if (nombreJours <= 0) {
                        throw new DonneesVehiculeInvalidesException(
                                "Nombre de jours invalide"
                        );
                    }

                    if (montant < 0) {
                        throw new DonneesVehiculeInvalidesException(
                                "Montant invalide"
                        );
                    }

                    if (kilometresParcourus < 0) {
                        throw new DonneesVehiculeInvalidesException(
                                "Kilométrage parcouru invalide"
                        );
                    }

                    Vehicule vehicule = rechercherVehicule(immatriculation);

                    if (vehicule == null) {
                        throw new DonneesVehiculeInvalidesException(
                                "Véhicule introuvable : " + immatriculation
                        );
                    }

                    Location location = new Location(
                            immatriculation,
                            nombreJours,
                            montant,
                            kilometresParcourus
                    );

                    historiqueLocations.add(location);

                } catch (DonneesVehiculeInvalidesException |
                         NumberFormatException e) {

                    System.out.println(
                            "Erreur location à la ligne "
                                    + numeroLigne
                                    + " : "
                                    + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Erreur lors de la lecture du fichier des locations : "
                            + e.getMessage()
            );
        }
    }

    public double calculerRevenuTotal() {

        double revenuTotal = 0;

        for (Location location : historiqueLocations) {
            revenuTotal += location.getMontant();
        }

        return revenuTotal;
    }

    public Vehicule trouverVehiculeLePlusUtilise() {

        Vehicule vehiculeLePlusUtilise = null;
        int maximumLocations = 0;

        for (Vehicule vehicule : flotte) {

            int nombreLocations = 0;

            for (Location location : historiqueLocations) {

                if (location.getImmatriculation()
                        .equalsIgnoreCase(vehicule.getImmatriculation())) {

                    nombreLocations++;
                }
            }

            if (nombreLocations > maximumLocations) {
                maximumLocations = nombreLocations;
                vehiculeLePlusUtilise = vehicule;
            }
        }

        return vehiculeLePlusUtilise;
    }

    public int compterLocationsVehicule(String immatriculation) {

        int compteur = 0;

        for (Location location : historiqueLocations) {

            if (location.getImmatriculation()
                    .equalsIgnoreCase(immatriculation)) {

                compteur++;
            }
        }

        return compteur;
    }
    // typeVehicule (ex. Voiture.class) permet de filtrer la flotte par
    // sous-classe via isInstance(), sans avoir à écrire une méthode
    // distincte pour chaque type de véhicule.
    public double calculerTauxUtilisationParType(Class<?> typeVehicule) {

        int totalVehiculesType = 0;
        int vehiculesUtilises = 0;

        for (Vehicule vehicule : flotte) {

            if (typeVehicule.isInstance(vehicule)) {

                totalVehiculesType++;

                if (compterLocationsVehicule(
                        vehicule.getImmatriculation()) > 0) {

                    vehiculesUtilises++;
                }
            }
        }

        if (totalVehiculesType == 0) {
            return 0;
        }

        return (double) vehiculesUtilises
                / totalVehiculesType
                * 100;
    }
    public void afficherVehiculesNecessitantEntretien() {

        boolean trouve = false;

        for (Vehicule vehicule : flotte) {

            if (vehicule.isEntretienRequis()) {
                System.out.println(vehicule);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun véhicule ne nécessite un entretien.");
        }
    }
    // Écrit un résumé complet (statistiques + véhicules à entretenir)
    // dans un fichier texte, pour la remise du projet.
    public void genererRapportTXT(String nomFichier) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(nomFichier))) {

            writer.write("===== RAPPORT DE LA FLOTTE =====");
            writer.newLine();
            writer.newLine();
            DateTimeFormatter format =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            writer.write("Date de génération : "
                    + LocalDateTime.now().format(format));

            writer.newLine();
            writer.newLine();
            writer.write("Nombre total de véhicules : "
                    + compterVehicules());
            writer.newLine();

            writer.write("Véhicules disponibles : "
                    + compterVehiculesDisponibles());
            writer.newLine();

            writer.write("Véhicules en entretien : "
                    + compterVehiculesEnEntretien());
            writer.newLine();

            writer.newLine();

            writer.write(String.format(
                    "Revenu total généré : %.2f $",
                    calculerRevenuTotal()
            ));
            writer.newLine();

            writer.write(String.format(
                    "Kilométrage moyen : %.2f km",
                    calculerKilometrageMoyen()
            ));
            writer.newLine();

            writer.write(String.format(
                    "Tarif journalier moyen : %.2f $",
                    calculerTarifJournalierMoyen()
            ));
            writer.newLine();

            writer.newLine();

            Vehicule vehiculeLePlusUtilise =
                    trouverVehiculeLePlusUtilise();

            if (vehiculeLePlusUtilise != null) {

                writer.write(
                        "Véhicule le plus utilisé : "
                                + vehiculeLePlusUtilise.getImmatriculation()
                                + " avec "
                                + compterLocationsVehicule(
                                vehiculeLePlusUtilise.getImmatriculation()
                        )
                                + " locations"
                );

            } else {
                writer.write("Aucune location enregistrée.");
            }

            writer.newLine();
            writer.newLine();

            writer.write(String.format(
                    "Taux d'utilisation des voitures : %.2f %%",
                    calculerTauxUtilisationParType(Voiture.class)
            ));
            writer.newLine();

            writer.write(String.format(
                    "Taux d'utilisation des camions : %.2f %%",
                    calculerTauxUtilisationParType(Camion.class)
            ));
            writer.newLine();

            writer.write(String.format(
                    "Taux d'utilisation des motos : %.2f %%",
                    calculerTauxUtilisationParType(Moto.class)
            ));
            writer.newLine();

            writer.newLine();
            writer.write("===== VÉHICULES NÉCESSITANT UN ENTRETIEN =====");
            writer.newLine();

            boolean trouve = false;

            for (Vehicule vehicule : flotte) {

                if (vehicule.isEntretienRequis()) {
                    writer.write(
                            "Immatriculation : " + vehicule.getImmatriculation()
                                    + " | Marque : " + vehicule.getMarque()
                                    + " | Modèle : " + vehicule.getModele()
                                    + " | Année : " + vehicule.getAnnee()
                                    + " | Kilométrage : " + vehicule.getKilometrage() + " km"
                                    + " | Tarif : " + vehicule.getTarifJournalier() + " $/jour"
                    );
                    writer.newLine();
                    trouve = true;
                }
            }

            if (!trouve) {
                writer.write(
                        "Aucun véhicule ne nécessite un entretien."
                );
                writer.newLine();
            }

            System.out.println(
                    "Rapport généré avec succès : " + nomFichier
            );

        } catch (IOException e) {

            System.out.println(
                    "Erreur lors de la génération du rapport : "
                            + e.getMessage()
            );
        }
    }
    public void rechercherParMarque(String marque) {
        boolean trouve = false;

        System.out.println("\nRésultats pour la marque : " + marque);

        for (Vehicule vehicule : flotte) {
            if (vehicule.getMarque().equalsIgnoreCase(marque)) {
                System.out.println(vehicule);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun véhicule trouvé pour cette marque.");
        }
    }
    public void rechercherParType(String type) {
        boolean trouve = false;

        System.out.println("\nRésultats pour le type : " + type);

        for (Vehicule vehicule : flotte) {
            if (vehicule.getClass().getSimpleName().equalsIgnoreCase(type)) {
                System.out.println(vehicule);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun véhicule trouvé pour ce type.");
        }
    }
    public void afficherVehiculesDisponibles() {
        boolean trouve = false;

        System.out.println("\n===== VÉHICULES DISPONIBLES =====");

        for (Vehicule vehicule : flotte) {
            if (vehicule.isDisponible()) {
                System.out.println(vehicule);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun véhicule disponible.");
        }
    }
    public void rechercherParAnnee(int annee) {
        boolean trouve = false;

        System.out.println("\nRésultats pour l'année : " + annee);

        for (Vehicule vehicule : flotte) {
            if (vehicule.getAnnee() == annee) {
                System.out.println(vehicule);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun véhicule trouvé pour cette année.");
        }
    }
    public void afficherHistoriqueLocations() {

        System.out.println("\n===== HISTORIQUE DES LOCATIONS =====");

        if (historiqueLocations.isEmpty()) {
            System.out.println("Aucune location enregistrée.");
            return;
        }

        for (Location location : historiqueLocations) {
            System.out.println(
                    "Immatriculation : " + location.getImmatriculation()
                            + " | Nombre de jours : " + location.getNombreJours()
                            + " | Montant : " + location.getMontant() + " $"
                            + " | Kilomètres parcourus : "
                            + location.getKilometresParcourus() + " km"
            );
        }
    }
}