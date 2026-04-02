package tower;

/**
 * CrazyLid - Tapa que se ubica en la base de la torre en lugar de cubrir la taza.
 */
public class CrazyLid extends Lid {
    /**
     * Crea una tapa Crazy de color naranja.
     * @param number numero de la tapa
     * @param color color heredado
     * @param widthPx ancho en pixeles
     */
    public CrazyLid(int number, String color, int widthPx) {
        super(number, color, widthPx);
        this.color = "orange";
    }

    /**
     * Inserta la tapa en la posicion mas baja de la torre (indice 0).
     * @param t Referencia a la torre
     * @return true siempre
     */
    @Override
    public boolean enterTower(Tower t) {
        t.getItems().add(0, this);
        return true;
    }
}