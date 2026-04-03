package tower;

import shapes.Rectangle;
import shapes.Canvas;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.awt.Dimension;

/**
 * Tower - Simulador de apilamiento de tazas y tapas.
 * Basado en el Problem J - Stacking Cups de ICPC 2025.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class Tower {

    private static final int SCALE = 20;
    private static final int WALL = 5;
    private static final int TOWER_X = 50;
    private static final int TOWER_Y = 20;

    private int width;
    private int maxHeight;
    private ArrayList<StackableItem> items;
    private boolean ok;
    private boolean visible;

    private Rectangle leftWall;
    private Rectangle rightWall;
    private Rectangle floor;
    private ArrayList<Rectangle> marks;

    /**
     * Crea una nueva torre con las dimensiones especificadas.
     * @param width     ancho de la torre en cm
     * @param maxHeight altura maxima de la torre en cm
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.items = new ArrayList<>();
        this.ok = true;
        this.visible = false;
        this.marks = new ArrayList<>();
    }

    /**
     * Crea una torre con tazas numeradas del 1 al numero indicado.
     * El ancho y la altura maxima se calculan automaticamente para que
     * todas las tazas quepan en el peor caso (sin nesting).
     * No incluye tapas. Ej. cups=4: crea las tazas 1, 2, 3 y 4 de tamano 1, 3, 5 y 7.
     * @param cups cantidad de tazas a crear (de 1 a cups)
     */
    public Tower(int cups) {
        this.width = cups * 2 + 2;
        this.maxHeight = cups * cups;
        this.items = new ArrayList<>();
        this.ok = true;
        this.visible = false;
        this.marks = new ArrayList<>();
        for (int i = 1; i <= cups; i++) {
            items.add(new NormalCup(i, this.width));
        }
    }

    /**
     * Agrega una taza de tipo normal a la torre.
     * @param i numero de la taza
     */
    public void pushCup(int i) {
        pushCup("cup", i);
    }

    /**
     * Agrega una taza del tipo especificado a la torre.
     * Tipos validos: "cup". Preparado para nuevos tipos en ciclos futuros.
     * @param type tipo de la taza
     * @param i    numero de la taza
     */
    public void pushCup(String type, int i) {
        ok = false;
        if (cupExists(i)) {
            if (visible) JOptionPane.showMessageDialog(null, "Ya existe una taza con el numero " + i);
            return;
        }

        Cup cup;
        switch(type.toLowerCase()) {
            case "opener": cup = new OpenerCup(i, width); break;
            case "hierarchical": cup = new HierarchicalCup(i, width); break;
            case "fragile": cup = new FragileCup(i, width); break;
            default: cup = new NormalCup(i, width); break;
        }

        if (cup.isTooBig(width)) {
            if (visible) JOptionPane.showMessageDialog(null, "La taza " + i + " es mas ancha que la torre");
            return;
        }

        cup.enterTower(this);

        if (this.height() > this.maxHeight) {
            items.remove(cup);
            if (visible) JOptionPane.showMessageDialog(null, "La taza " + i + " no cabe (excede la altura)");
            return;
        }

        checkFragile();
        ok = true;
        if (visible) redraw();
    }

    /**
     * Elimina la ultima taza agregada a la torre.
     * Si tiene su tapa en la torre, la elimina tambien.
     */
    public void popCup() {
        ok = false;
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).isCup()) {
                Cup cup = (Cup) items.get(i);
                cup.makeInvisible();
                if (!cup.canBeRemoved(this)) {
                    if (visible) JOptionPane.showMessageDialog(null, "Este elemento no se deja quitar.");
                    return;
                }
                items.remove(i);
                removeLidIfExists(cup.getNumber());
                ok = true;
                if (visible) redraw();
                return;
            }
        }
        if (visible) JOptionPane.showMessageDialog(null, "No hay tazas en la torre");
    }

    /**
     * Elimina la taza con el numero especificado.
     * Si tiene su tapa en la torre, la elimina tambien.
     * @param i numero de la taza a eliminar
     */
    public void removeCup(int i) {
        ok = false;
        for (int j = 0; j < items.size(); j++) {
            if (items.get(j).isCup() && items.get(j).getNumber() == i) {
                Cup cup = (Cup) items.get(j);
                
                if (!cup.canBeRemoved(this)) {
                    if (visible) JOptionPane.showMessageDialog(null, "Esta taza no se deja quitar.");
                    return;
                }
                
                cup.makeInvisible();
                items.remove(j);
                removeLidIfExists(i);
                ok = true;
                if (visible) redraw();
                return;
            }
        }
        if (visible) JOptionPane.showMessageDialog(null, "No existe una taza con el numero " + i);
    }

    /**
     * Agrega una tapa de tipo normal a la torre.
     * @param i numero de la tapa
     */
    public void pushLid(int i) {
        pushLid("lid", i);
    }

    /**
     * Agrega una tapa del tipo especificado a la torre.
     * Tipos validos: "lid". Preparado para nuevos tipos en ciclos futuros.
     * @param type tipo de la tapa
     * @param i    numero de la tapa
     */
    public void pushLid(String type, int i) {
        ok = false;
        if (lidExists(i)) return;

        Cup tempCup = new NormalCup(i, width);
        Cup cup = findCup(i);
        String color = (cup != null) ? cup.getColor() : tempCup.getColor();
        int widthPx  = (cup != null) ? cup.getCupWidthPx() : tempCup.getCupWidthPx();

        Lid lid;
        switch(type.toLowerCase()) {
            case "fearful": lid = new FearfulLid(i, color, widthPx); break;
            case "crazy": lid = new CrazyLid(i, color, widthPx); break;
            default: lid = new NormalLid(i, color, widthPx); break;
        }

        boolean entered = lid.enterTower(this);
        
        if (!entered) {
            return; 
        }

        if (this.height() > this.maxHeight) {
            items.remove(lid);
            if (visible) JOptionPane.showMessageDialog(null, "La tapa " + i + " no cabe");
            return;
        }

        checkFragile();
        ok = true;
        if (visible) redraw();
    }

    /**
     * Elimina la tapa que esta en el tope de la torre.
     */
    public void popLid() {
        ok = false;
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).isLid()) {
                items.get(i).makeInvisible();
                Lid lid = (Lid) items.get(i);
                if (!lid.canBeRemoved(this)) {
                    if (visible) JOptionPane.showMessageDialog(null, "Esta tapa no se deja quitar.");
                    return;
                }
                items.remove(i);
                ok = true;
                if (visible) redraw();
                return;
            }
        }
        if (visible) JOptionPane.showMessageDialog(null, "No hay tapas en la torre");
    }

    /**
     * Elimina la tapa con el numero especificado.
     * @param i numero de la tapa a eliminar
     */
    public void removeLid(int i) {
        ok = false;
        for (int j = 0; j < items.size(); j++) {
            if (items.get(j).isLid() && items.get(j).getNumber() == i) {
                Lid lid = (Lid) items.get(j);
                
                if (!lid.canBeRemoved(this)) {
                    if (visible) JOptionPane.showMessageDialog(null, "Esta tapa no se deja quitar.");
                    return;
                }
                
                lid.makeInvisible();
                items.remove(j);
                ok = true;
                if (visible) redraw();
                return;
            }
        }
        if (visible) JOptionPane.showMessageDialog(null, "No existe una tapa con el numero " + i);
    }

    /**
     * Mueve cada tapa inmediatamente encima de su taza correspondiente.
     * Solo afecta las tapas cuya taza este presente en la torre.
     * Las tapas sin taza permanecen en su posicion actual.
     */
    public void cover() {
        ok = false;
        ArrayList<StackableItem> newItems = new ArrayList<>();
        ArrayList<Lid> placedLids = new ArrayList<>();

        for (StackableItem item : items) {
            if (item.isCup()) {
                Cup cup = (Cup) item;
                newItems.add(cup);
                Lid matchingLid = findLid(cup.getNumber());
                if (matchingLid != null) {
                    newItems.add(matchingLid);
                    placedLids.add(matchingLid);
                    cup.setCovered(true);
                } else {
                    cup.setCovered(false);
                }
            }
        }
        for (StackableItem item : items) {
            if (item.isLid() && !placedLids.contains(item)) {
                newItems.add(item);
            }
        }

        items = filterByHeight(newItems);
        checkFragile();
        ok = true;
        if (visible) redraw();
    }

    /**
     * Intercambia la posicion de dos objetos en la torre.
     * Los objetos se identifican por tipo y numero. Ej: {"cup","4"} o {"lid","4"}.
     * @param o1 descriptor del primer objeto {tipo, numero}
     * @param o2 descriptor del segundo objeto {tipo, numero}
     */
    public void swap(String[] o1, String[] o2) {
        ok = false;
        int idx1 = findIndex(o1);
        int idx2 = findIndex(o2);
        
        if (idx1 == -1) {
            if (visible) JOptionPane.showMessageDialog(null, "No se encontro: " + o1[0] + " " + o1[1]);
            return;
        }
        if (idx2 == -1) {
            if (visible) JOptionPane.showMessageDialog(null, "No se encontro: " + o2[0] + " " + o2[1]);
            return;
        }

        backupItems(); 
        doSwap(idx1, idx2); 

        if (height() > maxHeight) {
            restoreItems();
            if (visible) JOptionPane.showMessageDialog(null, "Ese intercambio excede la altura maxima de la torre");
            return;
        }

        checkFragile(); 

        ok = true;
        if (visible) redraw();
    }

    /**
     * Busca un intercambio de dos elementos que reduzca la altura de la torre.
     * Retorna los descriptores de los dos elementos a intercambiar.
     * Si no existe ningun intercambio que reduzca la altura, retorna arreglo vacio.
     * Ej: {{"cup","4"},{"lid","4"}}
     * @return par de descriptores {tipo, numero} o arreglo vacio
     */
    public String[][] swapToReduce() {
        int currentHeight = height();
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                doSwap(i, j);
                int newHeight = height();
                doSwap(i, j);
                if (newHeight < currentHeight) {
                    return new String[][] {
                        {items.get(i).getType(), String.valueOf(items.get(i).getNumber())},
                        {items.get(j).getType(), String.valueOf(items.get(j).getNumber())}
                    };
                }
            }
        }
        return new String[0][0];
    }

    /**
     * Ordena los elementos de la torre de mayor a menor numero.
     * Solo incluye los elementos que quepan en la torre.
     */
    public void orderTower() {
        ok = false;
        ArrayList<StackableItem> cups = new ArrayList<>();
        ArrayList<StackableItem> lids = new ArrayList<>();

        for (StackableItem item : items) {
            if (item.isCup()) cups.add(item);
            else if (item.isLid()) lids.add(item);
        }

        cups.sort((a, b) -> b.getNumber() - a.getNumber());
        lids.sort((a, b) -> b.getNumber() - a.getNumber());

        ArrayList<StackableItem> ordered = new ArrayList<>();
        for (StackableItem cup : cups) {
            ordered.add(cup);
            int number = cup.getNumber();
            for (StackableItem lid : lids) {
                if (lid.getNumber() == number) { ordered.add(lid); break; }
            }
        }
        for (StackableItem lid : lids) {
            boolean cupFound = cups.stream().anyMatch(c -> c.getNumber() == lid.getNumber());
            if (!cupFound) ordered.add(lid);
        }

        items = filterByHeight(ordered);
        checkFragile();
        ok = true;
        if (visible) redraw();
    }

    /**
     * Coloca los elementos de la torre en orden inverso al actual.
     * Solo incluye los elementos que quepan en la torre.
     */
    public void reverseTower() {
        ok = false;
        ArrayList<StackableItem> reversed = new ArrayList<>();
        for (int i = items.size() - 1; i >= 0; i--) {
            reversed.add(items.get(i));
        }
        items = filterByHeight(reversed);
        checkFragile();
        ok = true;
        if (visible) redraw();
    }

    /**
     * Retorna la altura actual de los elementos apilados en cm.
     * Usa la misma logica de posicionamiento que redraw() para garantizar consistencia.
     */
    public int height() {
        int currentTopY = maxHeight;
        int prevItemTopY = maxHeight;
        int prevCupOuterBottomY = maxHeight;
        int prevCupNumber = 0;
        boolean prevWasCup = false;
        java.util.HashMap<Integer, Integer> cupTopYMap = new java.util.HashMap<>();

        for (StackableItem item : items) {
            if (item.isCup()) {
                Cup cup = (Cup) item;
                boolean nests = prevWasCup && cup.getNumber() < prevCupNumber;
                int outerBottomY = nests ? prevCupOuterBottomY - 1 : prevItemTopY;
                int cupTopY = outerBottomY - cup.getHeightCm();
                cupTopYMap.put(cup.getNumber(), cupTopY);
                if (cupTopY < currentTopY) currentTopY = cupTopY;
                prevCupOuterBottomY = outerBottomY;
                prevItemTopY = cupTopY;
                prevCupNumber = cup.getNumber();
                prevWasCup = true;
            } else if (item.isLid()) {
                boolean nests = prevWasCup && item.getNumber() < prevCupNumber;
                int lidBottomY = nests ? prevCupOuterBottomY - 1 : prevItemTopY;
                
                if (!nests && cupTopYMap.containsKey(item.getNumber())) {
                    lidBottomY = Math.min(lidBottomY, cupTopYMap.get(item.getNumber()));
                }
                
                int lidTopY = lidBottomY - item.getHeightCm();
                if (lidTopY < currentTopY) currentTopY = lidTopY;
                prevItemTopY = lidTopY;
                prevWasCup = false;
            }
        }
        return maxHeight - currentTopY;
    }

    /**
     * Retorna los numeros de las tazas tapadas por sus tapas, ordenados de menor a mayor.
     */
    public int[] lidedCups() {
        ArrayList<Integer> result = new ArrayList<>();
        for (StackableItem item : items) {
            if (item.isCup() && lidExists(item.getNumber())) {
                result.add(item.getNumber());
            }
        }
        result.sort((a, b) -> a - b);
        int[] array = new int[result.size()];
        for (int i = 0; i < result.size(); i++) array[i] = result.get(i);
        return array;
    }

    /**
     * Retorna los elementos apilados ordenados de base a cima.
     * Cada elemento es un par {tipo, numero} en minusculas.
     */
    public String[][] stackingItems() {
        String[][] result = new String[items.size()][2];
        for (int i = 0; i < items.size(); i++) {
            result[i][0] = items.get(i).getType();
            result[i][1] = String.valueOf(items.get(i).getNumber());
        }
        return result;
    }

    /**
     * Retorna los numeros de las tazas tapadas en formato legible.
     */
    public String lidedCupsInfo() {
        int[] lided = lidedCups();
        String result = "[";
        for (int i = 0; i < lided.length; i++) {
            result += lided[i];
            if (i < lided.length - 1) result += ", ";
        }
        return result + "]";
    }

    /**
     * Retorna los elementos apilados en formato legible.
     */
    public String stackingItemsInfo() {
        String[][] stacking = stackingItems();
        String result = "{";
        for (int i = 0; i < stacking.length; i++) {
            result += "{\"" + stacking[i][0] + "\",\"" + stacking[i][1] + "\"}";
            if (i < stacking.length - 1) result += ",";
        }
        return result + "}";
    }

    /**
     * Hace visible el simulador dibujando la torre y sus elementos.
     * Redimensiona el canvas para que se ajuste exactamente a la torre.
     * Si la torre no cabe en la pantalla del sistema, no se hace visible.
     */
    public void makeVisible() {
        int towerWidthPx  = width * SCALE;
        int towerHeightPx = maxHeight * SCALE;
        int neededWidth   = TOWER_X + towerWidthPx + WALL + 20;
        int neededHeight  = TOWER_Y + towerHeightPx + WALL + 20;

        Canvas canvas = Canvas.getCanvas();
        Dimension screen = canvas.getScreenSize();

        if (neededWidth > screen.width || neededHeight > screen.height) {
            ok = false;
            return;
        }

        canvas.resize(neededWidth, neededHeight);
        visible = true;

        leftWall = new Rectangle();
        leftWall.changeSize(towerHeightPx, WALL);
        leftWall.moveHorizontal(TOWER_X - 70);
        leftWall.moveVertical(TOWER_Y - 15);
        leftWall.changeColor("black");
        leftWall.makeVisible();

        rightWall = new Rectangle();
        rightWall.changeSize(towerHeightPx, WALL);
        rightWall.moveHorizontal(TOWER_X + towerWidthPx - 70);
        rightWall.moveVertical(TOWER_Y - 15);
        rightWall.changeColor("black");
        rightWall.makeVisible();

        floor = new Rectangle();
        floor.changeSize(WALL, towerWidthPx + WALL);
        floor.moveHorizontal(TOWER_X - 70);
        floor.moveVertical(TOWER_Y + towerHeightPx - 15);
        floor.changeColor("black");
        floor.makeVisible();

        drawMarks(towerHeightPx);
        if (items.size() > 0) redraw();
    }

    /**
     * Hace invisible el simulador ocultando la torre y sus elementos.
     */
    public void makeInvisible() {
        visible = false;
        if (leftWall != null)  leftWall.makeInvisible();
        if (rightWall != null) rightWall.makeInvisible();
        if (floor != null)     floor.makeInvisible();
        for (Rectangle mark : marks) mark.makeInvisible();
        marks.clear();
        for (StackableItem item : items) item.makeInvisible();
    }

    /**
     * Termina el simulador.
     */
    public void exit() {
        makeInvisible();
        System.exit(0);
    }

    /**
     * Retorna si la ultima operacion fue exitosa.
     */
    public boolean ok() {
        return ok;
    }

    // -------------------------------------------------------------------------
    // Metodos privados auxiliares
    // -------------------------------------------------------------------------

    /*
     * Busca el indice de un elemento por su descriptor {tipo, numero}.
     * Retorna -1 si no se encuentra.
     */
    private int findIndex(String[] descriptor) {
        String type = descriptor[0].toLowerCase();
        int number = Integer.parseInt(descriptor[1]);
        for (int i = 0; i < items.size(); i++) {
            StackableItem item = items.get(i);
            if (item.getType().equals(type) && item.getNumber() == number) return i;
        }
        return -1;
    }

    /*
     * Intercambia los elementos en las posiciones i y j de la lista.
     */
    private void doSwap(int i, int j) {
        StackableItem temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

    /*
     * Dibuja las marcaciones de centimetros a lo largo de la pared izquierda.
     */
    private void drawMarks(int towerHeightPx) {
        for (int i = 0; i <= maxHeight; i++) {
            Rectangle mark = new Rectangle();
            mark.changeSize(3, 10);
            mark.moveHorizontal(TOWER_X - 15 - 70);
            mark.moveVertical(TOWER_Y + towerHeightPx - i * SCALE - 15);
            mark.changeColor("red");
            mark.makeVisible();
            marks.add(mark);
        }
    }

    /*
     * Busca y retorna la taza con el numero dado, o null si no existe.
     */
    private Cup findCup(int number) {
        for (StackableItem item : items) {
            if (item.isCup() && item.getNumber() == number) return (Cup) item;
        }
        return null;
    }

    /*
     * Busca y retorna la tapa con el numero dado, o null si no existe.
     */
    private Lid findLid(int number) {
        for (StackableItem item : items) {
            if (item.isLid() && item.getNumber() == number) return (Lid) item;
        }
        return null;
    }

    /*
     * Verifica si ya existe una taza con ese numero en la torre.
     */
    public boolean cupExists(int number) {
        return findCup(number) != null;
    }

    /*
     * Verifica si ya existe una tapa con ese numero en la torre.
     */
    private boolean lidExists(int number) {
        return findLid(number) != null;
    }

    /*
     * Simula agregar el elemento y verifica si cabe en la torre.
     */
    private boolean fitsInTower(StackableItem newItem) {
        backupItems(); 
        
        this.items.add(newItem); 
        int newHeight = height();
        
        restoreItems();
        return newHeight <= maxHeight;
    }

    private ArrayList<StackableItem> backupList;

    private void backupItems() {
        backupList = new ArrayList<>(this.items);
    }

    private void restoreItems() {
        this.items = new ArrayList<>(backupList);
    }

    /*
     * Elimina la tapa con el numero dado si existe en la torre.
     */
    private void removeLidIfExists(int number) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isLid() && items.get(i).getNumber() == number) {
                items.get(i).makeInvisible();
                items.remove(i);
                return;
            }
        }
    }

    /*
     * Filtra una lista de elementos dejando solo los que quepan en la torre.
     */
    private ArrayList<StackableItem> filterByHeight(ArrayList<StackableItem> newItems) {
        ArrayList<StackableItem> result = new ArrayList<>();
        items = new ArrayList<>();
        for (StackableItem item : newItems) {
            if (item.isCup()) {
                Cup cup = (Cup) item;
                if (!cup.isTooBig(width) && fitsInTower(cup)) {
                    items.add(cup);
                    result.add(cup);
                } else {
                    cup.makeInvisible();
                }
            } else if (item.isLid()) {
                if (fitsInTower(item)) {
                    items.add(item);
                    result.add(item);
                } else {
                    item.makeInvisible();
                }
            }
        }
        return result;
    }

    /*
     * Redibuja todos los elementos de la torre en orden secuencial.
     */
    /*
     * Redibuja todos los elementos de la torre en orden secuencial.
     */
    private void redraw() {
        for (StackableItem item : items) {
            item.makeInvisible();
        }
        int floorY = TOWER_Y + maxHeight * SCALE;
        int centerX = TOWER_X + (width * SCALE) / 2;
        int prevItemTopY = floorY;
        int prevCupOuterBottomY = floorY;
        int prevCupNumber = 0;
        boolean prevWasCup = false;

        java.util.HashMap<Integer, Integer> cupTopYMap = new java.util.HashMap<>();

        for (StackableItem item : items) {
            if (item.isCup()) {
                Cup cup = (Cup) item;
                boolean nests = prevWasCup && cup.getNumber() < prevCupNumber;
                int outerBottomY = nests ? prevCupOuterBottomY - SCALE : prevItemTopY;
                int cupTopY = outerBottomY - cup.getHeightCm() * SCALE;
                
                cup.drawAt(centerX - cup.getCupWidthPx() / 2, cupTopY);
                
                cupTopYMap.put(cup.getNumber(), cupTopY);
                
                prevCupOuterBottomY = outerBottomY;
                prevItemTopY = cupTopY;
                prevCupNumber = cup.getNumber();
                prevWasCup = true;
                
            } else if (item.isLid()) {
                Lid lid = (Lid) item;
                boolean nests = prevWasCup && lid.getNumber() < prevCupNumber;
                int outerBottomY = nests ? prevCupOuterBottomY - SCALE : prevItemTopY;

                if (!nests && cupTopYMap.containsKey(lid.getNumber())) {
                    outerBottomY = Math.min(outerBottomY, cupTopYMap.get(lid.getNumber()));
                }
                
                int lidTopY = outerBottomY - lid.getHeightCm() * SCALE;
                lid.drawAt(centerX - lid.getWidthPx() / 2, lidTopY);
                
                prevItemTopY = lidTopY;
                prevWasCup = false;
            }
        }
    }
    
    /*
     * Verifica todas las FragileCup de la torre y elimina las que tengan
     * 3 o mas elementos encima.
     */
    private void checkFragile() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).isFragile()) {
                int elementsAbove = items.size() - 1 - i;
                if (elementsAbove >= 3) {
                    items.get(i).makeInvisible();
                    items.remove(i);
                }
            }
        }
    }

    /*
     * Permite a los elementos del paquete acceder a la lista para aplicar sus reglas de entrada.
     */
    ArrayList<StackableItem> getItems() {
        return this.items;
    }
}
