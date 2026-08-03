package omar.interfaces;
import omar.exceptions.KilometrageInvalideException;

/**
 * Contrat pour tout objet pouvant être loué et retourné.
 */
public interface Louable {

    void louer();

    void retourner(double kilometresParcourus)
            throws KilometrageInvalideException;
}