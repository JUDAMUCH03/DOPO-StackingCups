package shapes;

/**
 * Rectangle - Figura rectangular que se puede manipular y dibujar en el canvas.
 *
 * @author Michael Kolling and David J. Barnes (Modified)
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class Rectangle extends Figure {

    public static int EDGES = 4;

    private int height;
    private int width;

    /**
     * Crea un rectangulo en la posicion y color por defecto.
     */
    public Rectangle() {
        super(70, 15, "magenta");
        this.height = 30;
        this.width = 40;
    }

    /**
     * Cambia las dimensiones del rectangulo.
     * @param newHeight nuevo alto en pixeles
     * @param newWidth  nuevo ancho en pixeles
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    /**
     * Dibuja el rectangulo en el canvas con sus dimensiones y color actuales.
     */
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }
}