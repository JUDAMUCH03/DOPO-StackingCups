package shapes;

/**
 * Figure - Clase abstracta que representa una figura geometrica en el canvas.
 * Centraliza la logica comun de posicion, color y movimiento para todas las figuras.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public abstract class Figure {

    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;

    /**
     * Crea una figura en la posicion y color indicados.
     * @param x     posicion horizontal inicial en pixeles
     * @param y     posicion vertical inicial en pixeles
     * @param color color inicial de la figura
     */
    public Figure(int x, int y, String color) {
        this.xPosition = x;
        this.yPosition = y;
        this.color = color;
        this.isVisible = false;
    }

    /**
     * Hace visible la figura en el canvas.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible la figura en el canvas.
     */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /**
     * Mueve la figura un paso a la derecha.
     */
    public void moveRight() {
        moveHorizontal(20);
    }

    /**
     * Mueve la figura un paso a la izquierda.
     */
    public void moveLeft() {
        moveHorizontal(-20);
    }

    /**
     * Mueve la figura un paso hacia arriba.
     */
    public void moveUp() {
        moveVertical(-20);
    }

    /**
     * Mueve la figura un paso hacia abajo.
     */
    public void moveDown() {
        moveVertical(20);
    }

    /**
     * Mueve la figura horizontalmente la distancia indicada.
     * @param distance distancia en pixeles (negativa = izquierda)
     */
    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Mueve la figura verticalmente la distancia indicada.
     * @param distance distancia en pixeles (negativa = arriba)
     */
    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Mueve la figura horizontalmente de forma gradual.
     * @param distance distancia total en pixeles
     */
    public void slowMoveHorizontal(int distance) {
        int delta = (distance < 0) ? -1 : 1;
        int steps = Math.abs(distance);
        for (int i = 0; i < steps; i++) {
            xPosition += delta;
            draw();
        }
    }

    /**
     * Mueve la figura verticalmente de forma gradual.
     * @param distance distancia total en pixeles
     */
    public void slowMoveVertical(int distance) {
        int delta = (distance < 0) ? -1 : 1;
        int steps = Math.abs(distance);
        for (int i = 0; i < steps; i++) {
            yPosition += delta;
            draw();
        }
    }

    /**
     * Cambia el color de la figura.
     * @param newColor nuevo color de la figura
     */
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    /**
     * Dibuja la figura en el canvas con su forma especifica.
     * Cada subclase implementa como se ve visualmente.
     */
    protected abstract void draw();

    /**
     * Borra la figura del canvas.
     */
    protected void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}