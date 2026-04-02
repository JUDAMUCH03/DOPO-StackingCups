package tower;

/**
 * NormalLid - Representa una tapa con comportamiento estandar en el simulador.
 * Se ubica siempre al final de la pila actual de la torre.
 * * @author Daniel Felipe Sua y Juan David Munar
 */
public class NormalLid extends Lid {
    
    /**
     * Crea una tapa de tipo normal.
     * @param number numero de la tapa (debe coincidir con el de su taza)
     * @param color color asignado a la tapa
     * @param widthPx ancho de la tapa en pixeles
     */
    public NormalLid(int number, String color, int widthPx) {
        super(number, color, widthPx);
    }

    /**
     * Inserta la tapa en la torre siguiendo el comportamiento de apilamiento estandar.
     * @param t Referencia a la torre donde se adicionara el elemento
     * @return true tras completar la insercion exitosamente
     */
    @Override
    public boolean enterTower(Tower t) {
        t.getItems().add(this);
        return true;
    }
}