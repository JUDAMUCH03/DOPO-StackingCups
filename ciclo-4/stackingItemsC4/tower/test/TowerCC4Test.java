

package tower.test;
import tower.Tower;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * TowerCC4Test - Casos para la prueba colectiva del ciclo 4.
 * Escenarios combinados que involucran multiples tipos interactuando.
 * Todas las pruebas se ejecutan en modo invisible.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class TowerCC4Test {

    // Escenario 1: OpenerCup elimina FearfulLids al entrar
    @Test
    public void accordingMSOpenerShouldRemoveFearfulLidsOnEntry() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushLid("fearful", 1);
        t.pushCup(2);
        t.pushLid("fearful", 2);
        t.pushCup("opener", 3);
        assertTrue(t.ok());
        int lidCount = 0;
        for (String[] item : t.stackingItems()) {
            if (item[0].equals("lid")) lidCount++;
        }
        assertEquals(0, lidCount);
    }

    // Escenario 2: CrazyLid queda debajo de HierarchicalCup
    @Test
    public void accordingMSCrazyLidGoesToBottomBelowHierarchical() {
        Tower t = new Tower(10, 20);
        t.pushCup("hierarchical", 3);
        t.pushLid("crazy", 1);
        String[][] stacking = t.stackingItems();
        assertEquals("lid", stacking[0][0]);
        assertEquals("cup", stacking[1][0]);
    }

    // Escenario 3: HierarchicalCup no desplaza tazas mayores
    @Test
    public void accordingMSHierarchicalShouldNotDisplaceLargerCups() {
        Tower t = new Tower(10, 20);
        t.pushCup(4);
        t.pushCup("hierarchical", 2);
        String[][] stacking = t.stackingItems();
        assertEquals("4", stacking[0][1]);
        assertEquals("2", stacking[1][1]);
    }

    // Escenario 4: FearfulLid no entra si su taza no esta en la torre
    @Test
    public void accordingMSFearfulLidCannotEnterWithoutItsCup() {
        Tower t = new Tower(10, 20);
        t.pushCup("opener", 2);
        t.pushLid("fearful", 1);
        assertFalse(t.ok());
    }

    // Escenario 5: torre con todos los tipos mezclados cuenta elementos correctamente
    @Test
    public void accordingMSMixedTowerShouldCountElementsCorrectly() {
        Tower t = new Tower(12, 30);
        t.pushCup(1);
        t.pushCup("hierarchical", 3);
        t.pushLid("crazy", 2);
        t.pushCup(4);
        t.pushLid("fearful", 1);
        assertTrue(t.ok());
        assertEquals(5, t.stackingItems().length);
    }

    // Escenario 6: HierarchicalCup bloqueada no impide remover otras tazas
    @Test
    public void accordingMSBlockedHierarchicalShouldNotBlockOtherCups() {
        Tower t = new Tower(10, 20);
        t.pushCup("hierarchical", 3);
        t.pushCup(1);
        t.popCup();
        assertTrue(t.ok());
        assertEquals(1, t.stackingItems().length);
        assertEquals("3", t.stackingItems()[0][1]);
    }

    // Escenario 7: OpenerCup entra a torre vacia sin errores
    @Test
    public void accordingMSOpenerCupShouldEnterEmptyTowerWithoutError() {
        Tower t = new Tower(10, 20);
        t.pushCup("opener", 1);
        assertTrue(t.ok());
        assertEquals(1, t.stackingItems().length);
    }

    // Escenario 8: orderTower con tipos nuevos no rompe la torre
    @Test
    public void accordingMSOrderTowerShouldWorkWithNewTypes() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushCup("opener", 3);
        t.pushCup(2);
        t.orderTower();
        assertTrue(t.ok());
        assertEquals("3", t.stackingItems()[0][1]);
    }
}
