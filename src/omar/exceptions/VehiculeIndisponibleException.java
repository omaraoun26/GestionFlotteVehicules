package omar.exceptions;

/**
 * Levée lorsqu'on tente de louer un véhicule introuvable ou déjà indisponible.
 */
public class VehiculeIndisponibleException extends Exception {

    public VehiculeIndisponibleException(String message) {
        super(message);
    }
}
