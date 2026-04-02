package tower;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * TowerContest
 * Proporciona algoritmos para encontrar el orden de tazas que alcanza una altura especifica
 * y permite la visualizacion a traves del simulador Tower.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class TowerContest {

    /**
     * Resuelve el problema logistico de encontrar el orden de las tazas.
     * La entrada y la salida corresponden a lo definido en el problema de la maraton.
     * @param n cantidad de tazas a utilizar
     * @param h altura total objetivo de la torre
     * @return String con las alturas de las tazas separadas por espacios, o "impossible"
     */
    public static String solve(int n, int h) {
        if (!isValidBounds(n, h)) {
            return "impossible";
        }

        ArrayList<Integer> result = new ArrayList<>();
        int currentN = n;
        int currentH = h;

        while (currentN > 0) {
            int L = 2 * currentN - 1;
            int G = currentH - L;

            ArrayList<Integer> exactSequence = findExactSequence(currentN, L, G);

            if (exactSequence != null) {
                result.addAll(exactSequence);
                break;
            } else {
                result.add(L);
                currentH -= 1;
                currentN -= 1;

                if (currentN > 0 && !isValidBounds(currentN, currentH)) {
                    return "impossible";
                }
            }
        }

        if (currentN == 0 && currentH != 0) {
            return "impossible";
        }

        return formatSequence(result);
    }

    /**
     * Ejecuta el simulador visual de la torre basado en un objetivo de altura.
     * La entrada corresponde a lo definido en el problema de la maraton.
     * Si existe solucion y es posible graficarla, la muestra animada;
     * en caso contrario, presenta un mensaje indicandolo.
     * @param n cantidad de tazas a utilizar
     * @param h altura total objetivo de la torre
     */
    public static void simulate(int n, int h) {
        String result = solve(n, h);
        if (result.equals("impossible")) {
            JOptionPane.showMessageDialog(null, "Resultado: impossible");
            return;
        }

        int width = n * 2 + 2;
        int maxHeight = n * n;
        Tower tower = new Tower(width, maxHeight);
        tower.makeVisible();

        if (!tower.ok()) {
            JOptionPane.showMessageDialog(null, "La torre no cabe en la pantalla");
            return;
        }

        String[] heights = result.split(" ");
        for (String hStr : heights) {
            int heightCm = Integer.parseInt(hStr);
            int cupNumber = (heightCm + 1) / 2;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            tower.pushCup(cupNumber);
        }
    }

    /**
     * Valida si la altura objetivo se encuentra dentro de los limites teoricos posibles
     * para una cantidad dada de tazas.
     * @param n cantidad de tazas disponibles
     * @param h altura objetivo a evaluar
     * @return true si la altura esta dentro de los limites, false en caso contrario
     */
    private static boolean isValidBounds(int n, int h) {
        int minH = 2 * n - 1;
        int maxH = n * n;
        return h >= minH && h <= maxH;
    }

    /**
     * Intenta encontrar una combinacion exacta de tazas que sume el objetivo requerido.
     * @param currentN cantidad de tazas actualmente en evaluacion
     * @param L valor de la taza mas grande en la iteracion actual
     * @param targetSum altura restante a alcanzar
     * @return lista con la secuencia ordenada si se encuentra una combinacion exacta, o null
     */
    private static ArrayList<Integer> findExactSequence(int currentN, int L, int targetSum) {
        boolean[] used = new boolean[currentN + 1];
        int rem = targetSum;

        for (int i = currentN - 1; i >= 1; i--) {
            int cup = 2 * i - 1;
            if (cup <= rem) {
                used[i] = true;
                rem -= cup;
            }
        }

        if (rem == 0) {
            return buildTargetSequence(currentN, L, used);
        }
        return null;
    }

    /**
     * Construye la secuencia final de tazas a partir del arreglo de tazas utilizadas.
     * @param currentN cantidad de tazas actuales
     * @param L valor central de la secuencia
     * @param used arreglo booleano indicando cuales tazas forman parte del prefijo
     * @return lista con la secuencia estructurada en prefijo, centro y sufijo
     */
    private static ArrayList<Integer> buildTargetSequence(int currentN, int L, boolean[] used) {
        ArrayList<Integer> sequence = new ArrayList<>();
        
        for (int i = 1; i <= currentN - 1; i++) {
            if (used[i]) {
                sequence.add(2 * i - 1);
            }
        }

        sequence.add(L);

        for (int i = currentN - 1; i >= 1; i--) {
            if (!used[i]) {
                sequence.add(2 * i - 1);
            }
        }

        return sequence;
    }

    /**
     * Convierte la lista de resultados en una cadena de texto separada por espacios.
     * @param result lista de enteros con las alturas de las tazas
     * @return cadena formateada sin espacios al inicio o final
     */
    private static String formatSequence(ArrayList<Integer> result) {
        StringBuilder sb = new StringBuilder();
        for (int cup : result) {
            sb.append(cup).append(" ");
        }
        return sb.toString().trim();
    }
}