package tower.test;
import tower.Tower;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * TowerC4Test - Pruebas de unidad para los tipos nuevos del ciclo 4.
 * Todas las pruebas se ejecutan en modo invisible.
 * Convencion de nombres: accordingMS (Munar, Sua en orden alfabetico)
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class TowerC4Test {

    // =========================================================================
    // FearfulLid - no entra sin su taza, no sale si la esta cubriendo
    // =========================================================================

    @Test
    public void accordingMSShouldNotPushFearfulLidWithoutItsCup() {
        Tower t = new Tower(10, 20);
        t.pushLid("fearful", 2);
        assertFalse(t.ok());
        assertEquals(0, t.stackingItems().length);
    }

    @Test
    public void accordingMSShouldPushFearfulLidWhenItsCupIsPresent() {
        Tower t = new Tower(10, 20);
        t.pushCup(2);
        t.pushLid("fearful", 2);
        assertTrue(t.ok());
        assertEquals(2, t.stackingItems().length);
        assertEquals("lid", t.stackingItems()[1][0]);
        assertEquals("2", t.stackingItems()[1][1]);
    }

    @Test
    public void accordingMSShouldNotRemoveFearfulLidWhileCupIsInTower() {
        Tower t = new Tower(10, 20);
        t.pushCup(2);
        t.pushLid("fearful", 2);
        t.removeLid(2);
        assertFalse(t.ok());
        assertEquals(2, t.stackingItems().length);
    }

    @Test
    public void accordingMSShouldNotPopFearfulLidWhileCupIsInTower() {
        Tower t = new Tower(10, 20);
        t.pushCup(2);
        t.pushLid("fearful", 2);
        t.popLid();
        assertFalse(t.ok());
        assertEquals(2, t.stackingItems().length);
    }

    @Test
    public void accordingMSShouldRemoveFearfulLidAfterItsCupIsRemoved() {
        Tower t = new Tower(10, 20);
        t.pushCup(2);
        t.pushLid("fearful", 2);
        t.removeCup(2);
        assertTrue(t.ok());
        assertEquals(0, t.stackingItems().length);
    }

    // =========================================================================
    // CrazyLid - siempre entra al fondo (indice 0)
    // =========================================================================

    @Test
    public void accordingMSShouldPushCrazyLidAtBottomOfTower() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushCup(2);
        t.pushLid("crazy", 3);
        assertTrue(t.ok());
        String[][] stacking = t.stackingItems();
        assertEquals("lid", stacking[0][0]);
        assertEquals("3", stacking[0][1]);
    }

    @Test
    public void accordingMSShouldPushCrazyLidAtBottomEvenWithEmptyTower() {
        Tower t = new Tower(10, 20);
        t.pushLid("crazy", 1);
        assertTrue(t.ok());
        assertEquals(1, t.stackingItems().length);
        assertEquals("lid", t.stackingItems()[0][0]);
    }

    @Test
    public void accordingMSCrazyLidShouldKeepOtherElementsAbove() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushCup(2);
        t.pushLid("crazy", 3);
        String[][] stacking = t.stackingItems();
        assertEquals(3, stacking.length);
        assertEquals("cup", stacking[1][0]);
        assertEquals("cup", stacking[2][0]);
    }

    @Test
    public void accordingMSCrazyLidCanBeRemovedNormally() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushLid("crazy", 2);
        t.removeLid(2);
        assertTrue(t.ok());
        assertEquals(1, t.stackingItems().length);
    }

    // =========================================================================
    // OpenerCup - elimina todas las tapas al entrar
    // =========================================================================

    @Test
    public void accordingMSOpenerCupShouldRemoveAllLidsOnEntry() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushLid(1);
        t.pushLid(2);
        t.pushCup("opener", 3);
        assertTrue(t.ok());
        for (String[] item : t.stackingItems()) {
            assertEquals("cup", item[0]);
        }
    }

    @Test
    public void accordingMSOpenerCupShouldEnterEvenWithNoLids() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup("opener", 3);
        assertTrue(t.ok());
        assertEquals(3, t.stackingItems().length);
    }

    @Test
    public void accordingMSOpenerCupShouldBeLastAfterEntry() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushLid(1);
        t.pushCup("opener", 2);
        String[][] stacking = t.stackingItems();
        assertEquals("2", stacking[stacking.length - 1][1]);
    }

    @Test
    public void accordingMSOpenerCupShouldLeaveOnlyCupsAfterEntry() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushLid(1);
        t.pushLid(2);
        t.pushLid(3);
        t.pushCup("opener", 4);
        // cup1 + opener4 = 2 elementos
        assertEquals(2, t.stackingItems().length);
    }

    // =========================================================================
    // HierarchicalCup - desplaza menores y no se puede quitar si esta al fondo
    // =========================================================================

    @Test
    public void accordingMSHierarchicalCupShouldDisplaceSmallerCups() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup("hierarchical", 3);
        String[][] stacking = t.stackingItems();
        // cup3 debe aparecer antes que cup1 y cup2
        int idx3 = -1, idx1 = -1;
        for (int i = 0; i < stacking.length; i++) {
            if (stacking[i][1].equals("3")) idx3 = i;
            if (stacking[i][1].equals("1")) idx1 = i;
        }
        assertTrue(idx3 < idx1);
    }

    @Test
    public void accordingMSHierarchicalCupAtBottomCannotBeRemoved() {
        Tower t = new Tower(10, 20);
        t.pushCup("hierarchical", 3);
        t.removeCup(3);
        assertFalse(t.ok());
        assertEquals(1, t.stackingItems().length);
    }

    @Test
    public void accordingMSHierarchicalCupNotAtBottomCanBeRemoved() {
        Tower t = new Tower(10, 20);
        // cup4 tiene numero mayor que hierarchical2, no es desplazada
        // → cup4 queda en el fondo (indice 0), hierarchical2 en indice 1
        t.pushCup(4);
        t.pushCup("hierarchical", 2);
        t.removeCup(2);
        assertTrue(t.ok());
        assertEquals(1, t.stackingItems().length);
    }

    @Test
    public void accordingMSHierarchicalCupAtBottomCannotBePopped() {
        Tower t = new Tower(10, 20);
        // Sin nadie más: hierarchical queda sola en indice 0
        t.pushCup("hierarchical", 3);
        t.popCup();
        assertFalse(t.ok());
        assertEquals(1, t.stackingItems().length);
    }

    // =========================================================================
    // pushCup(type, i) y pushLid(type, i) - comportamiento generico
    // =========================================================================

    @Test
    public void accordingMSPushCupWithDefaultTypeShouldWorkLikeNormal() {
        Tower t = new Tower(10, 20);
        t.pushCup("cup", 2);
        assertTrue(t.ok());
        assertEquals("2", t.stackingItems()[0][1]);
    }

    @Test
    public void accordingMSPushLidWithDefaultTypeShouldWorkLikeNormal() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushLid("lid", 1);
        assertTrue(t.ok());
        assertEquals(2, t.stackingItems().length);
    }

    @Test
    public void accordingMSShouldNotPushDuplicateCupWithType() {
        Tower t = new Tower(10, 20);
        t.pushCup("opener", 2);
        t.pushCup("opener", 2);
        assertFalse(t.ok());
        assertEquals(1, t.stackingItems().length);
    }

    @Test
    public void accordingMSShouldNotPushDuplicateLidWithType() {
        Tower t = new Tower(10, 20);
        t.pushCup(1);
        t.pushLid("fearful", 1);
        t.pushLid("fearful", 1);
        assertFalse(t.ok());
        assertEquals(2, t.stackingItems().length);
    }
}