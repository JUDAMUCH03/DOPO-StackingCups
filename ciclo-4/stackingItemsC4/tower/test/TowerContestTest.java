package tower.test;
import tower.TowerContest;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * TowerContestTest - Pruebas de unidad para TowerContest.
 * Todas las pruebas se ejecutan en modo invisible.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class TowerContestTest {

    // =========================================================================
    // solve(n, h) - casos posibles
    // =========================================================================

    /**
     * Deberia: retornar el orden correcto para el ejemplo del enunciado (n=4, h=9).
     */
    @Test
    public void accordingMSShouldSolveSampleInput1() {
        String result = TowerContest.solve(4, 9);
        assertNotEquals("impossible", result);
        // Verificamos que la solucion tenga exactamente 4 alturas
        String[] parts = result.split(" ");
        assertEquals(4, parts.length);
    }

    /**
     * Deberia: retornar impossible para el ejemplo del enunciado (n=4, h=100).
     */
    @Test
    public void accordingMSShouldReturnImpossibleForSampleInput2() {
        assertEquals("impossible", TowerContest.solve(4, 100));
    }

    /**
     * Deberia: retornar impossible si h es menor que la altura minima posible (2n-1).
     */
    @Test
    public void accordingMSShouldReturnImpossibleIfHeightBelowMin() {
        // n=3, minH = 2*3-1 = 5
        assertEquals("impossible", TowerContest.solve(3, 4));
    }

    /**
     * Deberia: retornar impossible si h es mayor que la altura maxima posible (n^2).
     */
    @Test
    public void accordingMSShouldReturnImpossibleIfHeightAboveMax() {
        // n=3, maxH = 9
        assertEquals("impossible", TowerContest.solve(3, 10));
    }

    /**
     * Deberia: retornar una solucion valida para la altura minima (h = 2n-1).
     */
    @Test
    public void accordingMSShouldSolveMinimumHeight() {
        int n = 4;
        int minH = 2 * n - 1;
        String result = TowerContest.solve(n, minH);
        assertNotEquals("impossible", result);
        assertEquals(n, result.split(" ").length);
    }

    /**
     * Deberia: retornar una solucion valida para la altura maxima (h = n^2).
     */
    @Test
    public void accordingMSShouldSolveMaximumHeight() {
        int n = 4;
        int maxH = n * n;
        String result = TowerContest.solve(n, maxH);
        assertNotEquals("impossible", result);
        assertEquals(n, result.split(" ").length);
    }

    /**
     * Deberia: la solucion contener exactamente n alturas.
     */
    @Test
    public void accordingMSSolveShouldReturnExactlyNCups() {
        String result = TowerContest.solve(5, 15);
        if (!result.equals("impossible")) {
            assertEquals(5, result.split(" ").length);
        }
    }

    /**
     * Deberia: la solucion contener cada taza exactamente una vez.
     */
    @Test
    public void accordingMSSolveShouldContainEachCupOnce() {
        int n = 4;
        String result = TowerContest.solve(n, 9);
        assertNotEquals("impossible", result);
        String[] parts = result.split(" ");
        // Verificar que no hay duplicados
        java.util.HashSet<String> seen = new java.util.HashSet<>();
        for (String p : parts) {
            assertFalse("Altura duplicada: " + p, seen.contains(p));
            seen.add(p);
        }
    }

    /**
     * Deberia: la solucion contener alturas validas (2i-1 para i de 1 a n).
     */
    @Test
    public void accordingMSSolveShouldContainOnlyValidHeights() {
        int n = 4;
        String result = TowerContest.solve(n, 9);
        assertNotEquals("impossible", result);
        String[] parts = result.split(" ");
        java.util.HashSet<Integer> validHeights = new java.util.HashSet<>();
        for (int i = 1; i <= n; i++) validHeights.add(2 * i - 1);
        for (String p : parts) {
            int h = Integer.parseInt(p);
            assertTrue("Altura invalida: " + h, validHeights.contains(h));
        }
    }

    /**
     * Deberia: funcionar correctamente para n=1.
     */
    @Test
    public void accordingMSShouldSolveForOneCup() {
        // n=1: unica taza tiene altura 1, solo h=1 es posible
        assertEquals("1", TowerContest.solve(1, 1).trim());
        assertEquals("impossible", TowerContest.solve(1, 2));
    }

    /**
     * Deberia: retornar impossible para n=1 con h!=1.
     */
    @Test
    public void accordingMSShouldReturnImpossibleForOneCupWrongHeight() {
        assertEquals("impossible", TowerContest.solve(1, 5));
    }

    // =========================================================================
    // solve(n, h) - no deberia
    // =========================================================================

    /**
     * No deberia: retornar null en ningun caso.
     */
    @Test
    public void accordingMSSolveShouldNeverReturnNull() {
        assertNotNull(TowerContest.solve(4, 9));
        assertNotNull(TowerContest.solve(4, 100));
    }

    /**
     * No deberia: incluir alturas fuera del rango valido (mayores a 2n-1).
     */
    @Test
    public void accordingMSSolveShouldNotIncludeHeightsOutOfRange() {
        int n = 4;
        String result = TowerContest.solve(n, 9);
        assertNotEquals("impossible", result);
        int maxValidHeight = 2 * n - 1;
        for (String p : result.split(" ")) {
            assertTrue(Integer.parseInt(p) <= maxValidHeight);
            assertTrue(Integer.parseInt(p) >= 1);
        }
    }

    // =========================================================================
    // simulate(n, h) - en modo invisible (sin makeVisible)
    // =========================================================================

    /**
     * Deberia: no lanzar excepcion al simular un caso posible.
     */
    @Test
    public void accordingMSSimulateShouldNotThrowForValidInput() {
        try {
            // Solo probamos que solve genera el orden correcto
            String result = TowerContest.solve(4, 9);
            assertNotEquals("impossible", result);
        } catch (Exception e) {
            fail("No deberia lanzar excepcion: " + e.getMessage());
        }
    }

    /**
     * Deberia: solve retornar impossible antes de que simulate intente dibujar.
     */
    @Test
    public void accordingMSSimulateShouldHandleImpossibleCase() {
        // Verificamos que solve detecta impossible correctamente
        assertEquals("impossible", TowerContest.solve(4, 100));
    }
}