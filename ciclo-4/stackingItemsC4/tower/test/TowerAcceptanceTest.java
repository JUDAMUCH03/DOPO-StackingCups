package tower.test;
import tower.Tower;
/**
* TowerAcceptanceTest - Pruebas de aceptacion del ciclo 1.
* Estas pruebas son VISIBLES y tienen delays para poder ver
* cada accion sobre el simulador.
*
* @author Daniel Felipe Sua y Juan David Munar
*/
public class TowerAcceptanceTest {
 
    private static final int DELAY = 800;
 
    /*
     * Pausa la ejecucion el tiempo indicado en ms.
     */
    private void pause() {
        try { Thread.sleep(DELAY); } catch (Exception e) {}
    }
 
    // =========================================================================
    // Prueba 1: pushCup - las tazas se apilan correctamente de menor a mayor
    // =========================================================================
 
    /**
     * Se espera ver: taza 1 (roja), luego taza 2 (azul) encima,
     * luego taza 3 (verde) encima. Cada una mas ancha que la anterior.
     */
    public void accordingMSAcceptancePushCupsInOrder() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(1);
        pause();
        t.pushCup(2);
        pause();
        t.pushCup(3);
        pause();
    }
 
    // =========================================================================
    // Prueba 2: pushCup - taza mas pequena anida dentro de la mas grande
    // =========================================================================
 
    /**
     * Se espera ver: taza 3 (grande), luego taza 1 (pequena) anidada dentro.
     * La taza 1 solo debe sobresalir 1 cm por encima de la taza 3.
     */
    public void accordingMSAcceptanceSmallCupNestsInsideBigCup() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(3);
        pause();
        t.pushCup(1);
        pause();
    }
 
    // =========================================================================
    // Prueba 3: popCup - la ultima taza desaparece
    // =========================================================================
 
    /**
     * Se espera ver: tres tazas apiladas, luego la del tope desaparece.
     */
    public void accordingMSAcceptancePopCupRemovesTopCup() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        pause();
 
        t.popCup();
        pause();
    }
 
    // =========================================================================
    // Prueba 4: removeCup - elimina una taza del medio
    // =========================================================================
 
    /**
     * Se espera ver: tazas 1, 2, 3 apiladas, luego la taza 2 desaparece
     * y la taza 3 baja a ocupar su lugar.
     */
    public void accordingMSAcceptanceRemoveCupFromMiddle() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        pause();
 
        t.removeCup(2);
        pause();
    }
 
    // =========================================================================
    // Prueba 5: pushLid - la tapa aparece encima de su taza
    // =========================================================================
 
    /**
     * Se espera ver: taza 2 en la torre, luego su tapa aparece encima.
     * La tapa debe tener el mismo color que la taza.
     */
    public void accordingMSAcceptancePushLidAppearsOnTopOfCup() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(2);
        pause();
        t.pushLid(2);
        pause();
    }
 
    // =========================================================================
    // Prueba 6: popLid - la ultima tapa desaparece
    // =========================================================================
 
    /**
     * Se espera ver: taza 2 con su tapa, luego la tapa desaparece.
     */
    public void accordingMSAcceptancePopLidRemovesTopLid() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(2);
        t.pushLid(2);
        pause();
 
        t.popLid();
        pause();
    }
 
    // =========================================================================
    // Prueba 7: orderTower - los elementos se ordenan de mayor a menor
    // =========================================================================
 
    /**
     * Se espera ver: tazas en orden aleatorio, luego se reordenan
     * de mayor (abajo) a menor (arriba).
     */
    public void accordingMSAcceptanceOrderTowerSortsDescending() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(1);
        t.pushCup(3);
        t.pushCup(2);
        pause();
 
        t.orderTower();
        pause();
    }
 
    // =========================================================================
    // Prueba 8: orderTower - la tapa queda encima de su taza al ordenar
    // =========================================================================
 
    /**
     * Se espera ver: tazas y tapas desordenadas, luego al ordenar
     * cada tapa queda inmediatamente encima de su taza.
     */
    public void accordingMSAcceptanceOrderTowerKeepsLidOnCup() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(1);
        t.pushCup(3);
        t.pushLid(1);
        t.pushCup(2);
        pause();
 
        t.orderTower();
        pause();
    }
 
    // =========================================================================
    // Prueba 9: reverseTower - los elementos se invierten
    // =========================================================================
 
    /**
     * Se espera ver: tazas 1, 2, 3 de abajo a arriba, luego se invierten
     * quedando 3, 2, 1 de abajo a arriba.
     */
    public void accordingMSAcceptanceReverseTowerInvertsOrder() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        pause();
 
        t.reverseTower();
        pause();
    }
 
    // =========================================================================
    // Prueba 10: makeInvisible / makeVisible - el simulador desaparece y reaparece
    // =========================================================================
 
    /**
     * Se espera ver: torre con tazas, luego desaparece, luego reaparece.
     */
    public void accordingMSAcceptanceMakeInvisibleAndVisible() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        t.pushCup(1);
        t.pushCup(2);
        pause();
 
        t.makeInvisible();
        pause();
 
        t.makeVisible();
        pause();
    }
 
    // =========================================================================
    // Prueba 11: torre demasiado grande - no se hace visible
    // =========================================================================
 
    /**
     * Se espera ver: NO aparece ninguna ventana porque la torre no cabe
     * en la pantalla. ok() debe ser false.
     */
    public void accordingMSAcceptanceTowerTooBigDoesNotShow() {
        Tower t = new Tower(1000, 1000);
        t.makeVisible();
        System.out.println("ok() debe ser false: " + t.ok());
    }
 
    // =========================================================================
    // Prueba 12: popCup elimina tambien su tapa
    // =========================================================================
 
    /**
     * Se espera ver: taza 2 con su tapa, al hacer popCup ambas desaparecen.
     */
    public void accordingMSAcceptancePopCupAlsoRemovesItsLid() {
        Tower t = new Tower(10, 20);
        t.makeVisible();
        pause();
 
        t.pushCup(2);
        t.pushLid(2);
        pause();
 
        t.popCup();
        pause();
    }
}