package tower;
import shapes.Rectangle;
import shapes.Canvas;

/**
 * Cup - Representa una taza en el simulador.
 * La taza numero i tiene altura 2i-1 cm y se dibuja como una forma de U.
 * * @author Daniel Felipe Sua y Juan David Munar
 */
public abstract class Cup extends StackableItem {
    protected static final int SCALE = 20;
    protected static final int WALL_THICK = 8;
    protected static final String[] COLORS = {"red", "blue", "green", "yellow", "magenta", "white", "gray", "pink"};

    protected int number;
    protected int heightCm;
    protected int cupWidthPx;
    protected String color;
    protected boolean visible;
    protected boolean covered;
    protected int xPx;
    protected int yPx;

    protected Rectangle leftWall;
    protected Rectangle rightWall;
    protected Rectangle base;

    /**
     * Crea una taza con su numero y el ancho de la torre.
     * @param number numero de la taza (i)
     * @param towerWidthCm ancho de la torre en cm
     */
    public Cup(int number, int towerWidthCm) {
        this.number = number;
        this.heightCm = 2 * number - 1;
        this.color = COLORS[(number - 1) % COLORS.length];
        this.cupWidthPx = calculateWidth(number, towerWidthCm);
        this.visible = false;
        this.covered = false;
        this.xPx = 0;
        this.yPx = 0;
        leftWall = new Rectangle();
        rightWall = new Rectangle();
        base = new Rectangle();
    }

    /**
     * Calcula el ancho de la taza en pixeles sin exceder el ancho de la torre.
     * @param number numero de la taza
     * @param towerWidthCm ancho de la torre
     * @return ancho calculado en pixeles
     */
    private int calculateWidth(int number, int towerWidthCm) {
        int desired = number * SCALE * 2;
        int maxAllowed = towerWidthCm * SCALE - WALL_THICK * 2;
        return Math.min(desired, maxAllowed);
    }

    /**
     * Dibuja la taza en la posicion indicada y almacena su posicion.
     * @param xPx posicion x absoluta en pixeles (esquina superior izquierda)
     * @param yPx posicion y absoluta en pixeles (esquina superior izquierda)
     */
    @Override
    public void drawAt(int xPx, int yPx) {
        makeInvisible();
        this.xPx = xPx;
        this.yPx = yPx;
        leftWall = new Rectangle();
        rightWall = new Rectangle();
        base = new Rectangle();

        int wallHeight = (heightCm - 1) * SCALE;

        leftWall.changeSize(wallHeight, WALL_THICK);
        leftWall.moveHorizontal(xPx - 70);
        leftWall.moveVertical(yPx - 15);
        leftWall.changeColor(color);
        leftWall.makeVisible();

        rightWall.changeSize(wallHeight, WALL_THICK);
        rightWall.moveHorizontal(xPx + cupWidthPx - WALL_THICK - 70);
        rightWall.moveVertical(yPx - 15);
        rightWall.changeColor(color);
        rightWall.makeVisible();

        base.changeSize(SCALE, cupWidthPx);
        base.moveHorizontal(xPx - 70);
        base.moveVertical(yPx + wallHeight - 15);
        base.changeColor(color);
        base.makeVisible();

        visible = true;
    }

    /**
     * Hace invisible la taza ocultando sus componentes graficos.
     */
    @Override
    public void makeInvisible() {
        leftWall.makeInvisible();
        rightWall.makeInvisible();
        base.makeInvisible();
        visible = false;
    }

    /**
     * Retorna el identificador de tipo de la taza.
     * @return String "cup"
     */
    @Override
    public String getType() {
        return "cup";
    }

    /**
     * Verifica si el elemento es una taza.
     * @return true siempre para esta clase
     */
    @Override
    public boolean isCup() {
        return true;
    }

    /**
     * Marca la taza como tapada o destapada.
     * @param covered true si la taza esta tapada
     */
    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    /**
     * Retorna si la taza esta actualmente tapada.
     * @return estado de cobertura de la taza
     */
    public boolean isCovered() {
        return covered;
    }

    /**
     * Retorna el numero identificador de la taza.
     * @return numero de la taza
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Retorna la altura de la taza en centimetros.
     * @return altura en cm
     */
    @Override
    public int getHeightCm() {
        return heightCm;
    }

    /**
     * Retorna el ancho grafico de la taza.
     * @return ancho en pixeles
     */
    public int getCupWidthPx() {
        return cupWidthPx;
    }

    /**
     * Retorna el color asignado a la taza.
     * @return nombre del color
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * Retorna la coordenada X actual.
     * @return posicion x en pixeles
     */
    public int getXPx() {
        return xPx;
    }

    /**
     * Retorna la coordenada Y actual.
     * @return posicion y en pixeles
     */
    public int getYPx() {
        return yPx;
    }

    /**
     * Indica si la taza es mas ancha que la torre.
     * @param towerWidthCm ancho de la torre en cm
     * @return true si excede el ancho permitido
     */
    public boolean isTooBig(int towerWidthCm) {
        return number * SCALE * 2 > towerWidthCm * SCALE - WALL_THICK * 2;
    }

    /**
     * Define si la taza puede ser retirada de la torre.
     * @param t Referencia a la torre
     * @return true si puede ser removida
     */
    public boolean canBeRemoved(Tower t) {
        return true;
    }

    /**
     * Metodo abstracto para definir el comportamiento de insercion en la torre.
     * @param t Referencia a la torre donde se insertara
     */
    public abstract void enterTower(Tower t);
}