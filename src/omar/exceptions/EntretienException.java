package omar.exceptions;

/**
 * Levée lors d'une opération d'entretien invalide (ex. véhicule déjà en
 * entretien, ou actuellement loué).
 */
public class EntretienException extends Exception {

    public EntretienException(String message) {
        super(message);
    }
}
