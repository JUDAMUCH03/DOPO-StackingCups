package shapes; 

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose Canvas, specially made for
 * the BlueJ "shapes" example.
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 * @version: 1.6 (shapes)
 */
public class Canvas {

    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", 300, 300, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    //  ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;

    /**
     * Create a Canvas.
     * @param title    title to appear in Canvas Frame
     * @param width    the desired width for the canvas
     * @param height   the desired height for the canvas
     * @param bgColour the desired background colour of the canvas
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible.
     * @param visible boolean value representing the desired visibility
     */
    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Redimensiona el canvas al nuevo ancho y alto indicados.
     * Borra todos los objetos dibujados y recrea la imagen de fondo.
     * @param width  nuevo ancho en pixeles
     * @param height nuevo alto en pixeles
     */
    public void resize(int width, int height) {
        canvas.setPreferredSize(new Dimension(width, height));
        frame.pack();
        canvasImage = canvas.createImage(width, height);
        graphic = (Graphics2D) canvasImage.getGraphics();
        graphic.setColor(backgroundColour);
        graphic.fillRect(0, 0, width, height);
        graphic.setColor(Color.black);
        objects.clear();
        shapes.clear();
        canvas.repaint();
    }

    /**
     * Retorna el tamanio real de la pantalla del sistema operativo.
     * @return Dimension con el ancho y alto de la pantalla en pixeles
     */
    public Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Draw a given shape onto the canvas.
     * @param referenceObject an object to define identity for this shape
     * @param color           the color of the shape
     * @param shape           the shape object to be drawn on the canvas
     */
    public void draw(Object referenceObject, String color, Shape shape) {
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Erase a given shape from the screen.
     * @param referenceObject the shape object to be erased
     */
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Set the foreground colour of the Canvas.
     * @param colorString the new colour name
     */
    private void setForegroundColor(String colorString) {
        if (colorString.equals("red")) {
            graphic.setColor(new java.awt.Color(235, 25, 25));
        } else if (colorString.equals("black")) {
            graphic.setColor(java.awt.Color.black);
        } else if (colorString.equals("blue")) {
            graphic.setColor(new java.awt.Color(30, 75, 220));
        } else if (colorString.equals("yellow")) {
            graphic.setColor(new java.awt.Color(255, 230, 0));
        } else if (colorString.equals("green")) {
            graphic.setColor(new java.awt.Color(80, 160, 50));
        } else if (colorString.equals("magenta")) {
            graphic.setColor(java.awt.Color.magenta);
        } else if (colorString.equals("white")) {
            graphic.setColor(java.awt.Color.white);
        } else if (colorString.equals("gray")) {
            graphic.setColor(java.awt.Color.gray);
        } else if (colorString.equals("pink")) {
            graphic.setColor(java.awt.Color.pink);
        }
        
        else if (colorString.equals("orange")) {
            graphic.setColor(new java.awt.Color(255, 128, 0)); 
        } else if (colorString.equals("cyan")) {
            graphic.setColor(new java.awt.Color(0, 200, 255)); 
        } else if (colorString.equals("purple")) {
            graphic.setColor(new java.awt.Color(128, 0, 128));
        } else if (colorString.equals("gold")) {
            graphic.setColor(new java.awt.Color(212, 175, 55));
        } else if (colorString.equals("crimson")) {
            graphic.setColor(new java.awt.Color(220, 20, 60));
        } 
        
        else {
            graphic.setColor(java.awt.Color.black);
        }
    }

    /**
     * Wait for a specified number of milliseconds before finishing.
     * @param milliseconds the number of milliseconds to wait
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            // ignoring exception at the moment
        }
    }

    /**
     * Retorna las dimensiones actuales del canvas.
     */
    public Dimension getSize() {
        return canvas.getSize();
    }

    /**
     * Redraw all shapes currently on the Canvas.
     */
    private void redraw() {
        erase();
        for (Iterator i = objects.iterator(); i.hasNext();) {
            shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }

    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /************************************************************************
     * Inner class ShapeDescription
     */
    private class ShapeDescription {
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}
