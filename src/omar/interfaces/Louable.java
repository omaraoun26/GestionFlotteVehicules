package omar.interfaces;
import omar.exceptions.KilometrageInvalideException;

public interface Louable {

    void louer();

    void retourner(double kilometresParcourus)
            throws KilometrageInvalideException;
}