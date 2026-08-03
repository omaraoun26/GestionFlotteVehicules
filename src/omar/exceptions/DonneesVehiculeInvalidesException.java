package omar.exceptions;

/**
 * Levée lorsqu'une ligne de fichier CSV contient une donnée manquante,
 * mal formatée ou incohérente (type inconnu, valeur négative, etc.).
 */
public class DonneesVehiculeInvalidesException extends Exception {

    public DonneesVehiculeInvalidesException(String message) {
        super(message);
    }

}