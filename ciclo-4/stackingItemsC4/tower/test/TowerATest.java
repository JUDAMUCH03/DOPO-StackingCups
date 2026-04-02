package tower.test;
import tower.Tower;

import javax.swing.JOptionPane;

/**
 * TowerATest - Pruebas de aceptacion del ciclo 4.
 * Pruebas VISIBLES con delays para observar el comportamiento de los tipos nuevos.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class TowerATest {

    private static final int DELAY = 800;

    private void pause() {
        try { Thread.sleep(DELAY); } catch (Exception e) {}
    }

    // =========================================================================
    // Prueba 1: OpenerCup elimina FearfulLids al entrar
    // Se espera ver: dos tazas con tapas moradas, luego entra la OpenerCup cyan
    // y todas las tapas desaparecen de golpe.
    // =========================================================================
    public void accordingMSAcceptanceOpenerCupRemovesFearfulLids() {
        Tower t = new Tower(12, 30);
        t.makeVisible();
        pause();

        t.pushCup(1);
        pause();
        t.pushLid("fearful", 1);
        pause();
        t.pushCup(2);
        pause();
        t.pushLid("fearful", 2);
        pause();

        JOptionPane.showMessageDialog(null,
            "Se ven dos tazas con tapas moradas (FearfulLid).\n" +
            "Al presionar OK entrara una OpenerCup (cyan)\n" +
            "y debera eliminar ambas tapas automaticamente.");

        t.pushCup("opener", 3);
        pause();

        String resultado = (t.ok() && t.lidedCups().length == 0)
            ? "CORRECTO: la OpenerCup elimino todas las tapas."
            : "ERROR: quedaron tapas en la torre.";
        JOptionPane.showMessageDialog(null, resultado);
    }

    // =========================================================================
    // Prueba 2: HierarchicalCup bloqueada en el fondo
    // Se espera ver: HierarchicalCup dorada desplaza tazas menores al entrar.
    // Al intentar quitarla del fondo, la torre rechaza la operacion.
    // Luego una CrazyLid entra en la base como demostracion extra.
    // =========================================================================
    public void accordingMSAcceptanceHierarchicalCupBlockedAtBottom() {
        Tower t = new Tower(12, 30);
        t.makeVisible();
        pause();

        t.pushCup(1);
        pause();
        t.pushCup(2);
        pause();

        JOptionPane.showMessageDialog(null,
            "Se ven las tazas 1 y 2.\n" +
            "Al presionar OK entrara una HierarchicalCup (dorada, numero 4)\n" +
            "y debera desplazarlas hacia arriba quedando en la base.");

        t.pushCup("hierarchical", 4);
        pause();

        JOptionPane.showMessageDialog(null,
            "La HierarchicalCup esta en la base.\n" +
            "Al presionar OK se intentara removerla.\n" +
            "La torre debera rechazar la operacion.");

        t.removeCup(4);
        pause();

        String resultadoBloqueo = !t.ok()
            ? "CORRECTO: la HierarchicalCup no se puede quitar del fondo."
            : "ERROR: la HierarchicalCup fue removida cuando no debia.";
        JOptionPane.showMessageDialog(null, resultadoBloqueo);

        JOptionPane.showMessageDialog(null,
            "Ahora entrara una CrazyLid (naranja)\n" +
            "que se ubicara en la posicion mas baja de la torre.");

        t.pushLid("crazy", 3);
        pause();

        JOptionPane.showMessageDialog(null, "Demostracion finalizada.");
    }
}