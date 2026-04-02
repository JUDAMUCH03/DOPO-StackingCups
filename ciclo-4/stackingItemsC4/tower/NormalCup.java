package tower;

/**
 * NormalCup - Representa el comportamiento estandar de una taza en el simulador.
 */
public class NormalCup extends Cup {
    /**
     * Crea una taza normal.
     * @param number numero de la taza
     * @param towerWidthCm ancho de la torre
     */
    public NormalCup(int number, int towerWidthCm) {
        super(number, towerWidthCm);
    }

    /**
     * Agrega la taza al final de la pila actual de la torre.
     * @param t Referencia a la torre
     */
    @Override
    public void enterTower(Tower t) {
        t.getItems().add(this);
    }
}