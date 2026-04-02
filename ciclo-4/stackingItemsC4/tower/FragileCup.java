package tower;

/**
 * FragileCup - Taza fragil que se rompe si tiene 3 o mas elementos encima.
 * Entra a la torre de forma normal pero Tower la elimina automaticamente
 * cuando la cantidad de elementos apilados sobre ella alcanza 3 o mas.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class FragileCup extends Cup {

    private static final int MAX_ELEMENTS_ABOVE = 3;

    /**
     * Crea una taza fragil de color crimson.
     * @param number       numero de la taza
     * @param towerWidthCm ancho de la torre en cm
     */
    public FragileCup(int number, int towerWidthCm) {
        super(number, towerWidthCm);
        this.color = "crimson";
    }

    /**
     * Indica que este elemento es una taza fragil.
     * Tower usa este metodo en checkFragile() 
     */
    @Override
    public boolean isFragile() {
        return true;
    }

    /**
     * Entra a la torre de forma normal, al final de la lista.
     * @param t referencia a la torre
     */
    @Override
    public void enterTower(Tower t) {
        t.getItems().add(this);
    }
}