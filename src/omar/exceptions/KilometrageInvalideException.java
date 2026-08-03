package omar.exceptions;

/**
 * Levée lorsqu'un kilométrage fourni est invalide (ex. négatif au retour
 * d'un véhicule).
 */
public class KilometrageInvalideException extends Exception {

    public KilometrageInvalideException(String message) {
        super(message);
    }
}
