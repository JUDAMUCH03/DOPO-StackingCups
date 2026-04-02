package tower;

/**
 * FearfulLid - Tapa que requiere la presencia de su taza para entrar y no sale si la esta cubriendo.
 */
public class FearfulLid extends Lid {
    /**
     * Crea una tapa Fearful de color morado.
     * @param number numero de la tapa
     * @param color color heredado
     * @param widthPx ancho en pixeles
     */
    public FearfulLid(int number, String color, int widthPx) {
        super(number, color, widthPx);
        this.color = "purple";
    }

    /**
     * Valida la existencia de la taza compañera antes de entrar.
     * @param t Referencia a la torre
     * @return true si la taza existe, false de lo contrario
     */
    @Override
    public boolean enterTower(Tower t) {
        if (!t.cupExists(this.getNumber())) {
            return false;
        }
        t.getItems().add(this);
        return true;
    }

    /**
     * Impide la salida de la tapa si su taza sigue en la torre.
     * @param t Referencia a la torre
     * @return true si la taza no esta, false si la esta cubriendo
     */
    @Override
    public boolean canBeRemoved(Tower t) {
        return !t.cupExists(this.getNumber());
    }
}