package tower;
import java.util.ArrayList;

/**
 * HierarchicalCup - Taza que desplaza objetos menores al entrar y se bloquea si llega al fondo.
 */
public class HierarchicalCup extends Cup {

    /**
     * Crea una taza jerarquica.
     * @param number numero de la taza
     * @param towerWidthCm ancho de la torre
     */
    public HierarchicalCup(int number, int towerWidthCm) {
        super(number, towerWidthCm);
        this.color = "gold";
    }

    /**
     * Valida si la taza puede ser removida segun su posicion actual en la torre.
     * @param t Referencia a la torre
     * @return false si la taza esta en la base de la torre
     */
    @Override
    public boolean canBeRemoved(Tower t) {
        return t.getItems().indexOf(this) != 0;
    }

    /**
     * Inserta la taza desplazando hacia arriba todos los elementos de menor numero.
     * @param t Referencia a la torre
     */
    @Override
    public void enterTower(Tower t) {
        ArrayList<StackableItem> items = t.getItems();
        int insertIndex = items.size();

        while (insertIndex > 0 && items.get(insertIndex - 1).getNumber() < this.getNumber()) {
            insertIndex--;
        }
        items.add(insertIndex, this);
    }
}