package tower;
import java.util.ArrayList;

/**
 * OpenerCup - Taza que elimina todas las tapas que le impiden el paso al entrar.
 */
public class OpenerCup extends Cup {
    /**
     * Crea una taza de tipo Opener.
     * @param number numero de la taza
     * @param towerWidthCm ancho de la torre
     */
    public OpenerCup(int number, int towerWidthCm) {
        super(number, towerWidthCm);
        this.color = "cyan";
    }

    /**
     * Implementa la logica de entrada eliminando visual y logicamente todas las tapas.
     * @param t Referencia a la torre
     */
    @Override
    public void enterTower(Tower t) {
        ArrayList<StackableItem> items = t.getItems();
        for (StackableItem item : items) {
            if (item.isLid()) {
                item.makeInvisible();
            }
        }
        items.removeIf(item -> item.isLid());
        items.add(this);
    }
}