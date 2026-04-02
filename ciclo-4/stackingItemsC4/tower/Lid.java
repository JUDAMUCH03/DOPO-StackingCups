package tower;
import shapes.Rectangle;
import shapes.Canvas;

/**
 * Lid - Representa una tapa en el simulador de apilamiento.
 * La tapa mide siempre 1 cm de alto y conoce su propia posicion.
 * @author Daniel Felipe Sua y Juan David Munar
 */
public abstract class Lid extends StackableItem {
    protected static final int SCALE = 20;
    protected static final int HEIGHT_CM = 1;

    protected int number;
    protected String color;
    protected int cupWidthPx;
    protected boolean visible;
    protected int xPx;
    protected int yPx;

    protected Rectangle body;

    /**
     * Crea una tapa base con su identificador y dimensiones.
     * @param number numero de la tapa (mismo que su taza)
     * @param color color inicial de la tapa
     * @param widthPx ancho en pixeles
     */
    public Lid(int number, String color, int widthPx) {
        this.number = number;
        this.color = color;
        this.cupWidthPx = widthPx;
        this.visible = false;
        this.xPx = 0;
        this.yPx = 0;
        this.body = new Rectangle();
    }

    /**
     * Dibuja la tapa en el lienzo en las coordenadas especificadas.
     * @param xPx posicion x 
     * @param yPx posicion y 
     */
    @Override
    public void drawAt(int xPx, int yPx) {
        makeInvisible();
        this.xPx = xPx;
        this.yPx = yPx;
        body = new Rectangle();
        body.changeSize(HEIGHT_CM * SCALE, cupWidthPx);
        body.moveHorizontal(xPx - 70);
        body.moveVertical(yPx - 15);
        body.changeColor(color);
        body.makeVisible();
        visible = true;
    }

    /**
     * Oculta la representacion grafica de la tapa.
     */
    @Override
    public void makeInvisible() {
        body.makeInvisible();
        visible = false;
    }

    /**
     * Retorna el identificador de tipo "lid".
     * @return String con el tipo
     */
    @Override
    public String getType() {
        return "lid";
    }

    /**
     * Confirma que el objeto es una tapa.
     * @return true para todas las instancias de Lid
     */
    @Override
    public boolean isLid() {
        return true;
    }

    /**
     * Retorna el numero identificador.
     * @return numero de la tapa
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Retorna la altura fija de la tapa.
     * @return 1 cm
     */
    @Override
    public int getHeightCm() {
        return HEIGHT_CM;
    }

    /**
     * Retorna el color actual de la tapa.
     * @return nombre del color
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * Retorna el ancho grafico.
     * @return ancho en pixeles
     */
    public int getWidthPx() {
        return cupWidthPx;
    }

    /**
     * Metodo abstracto para la logica de entrada a la torre.
     * @param t Referencia a la torre
     * @return true si la insercion fue exitosa
     */
    public abstract boolean enterTower(Tower t);

    /**
     * Indica si la tapa puede ser removida segun reglas de negocio.
     * @param t Referencia a la torre
     * @return true por defecto
     */
    public boolean canBeRemoved(Tower t) {
        return true;
    }
}