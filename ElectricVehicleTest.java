import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias para la clase ElectricVehicle.
 */
public class ElectricVehicleTest
{
    private ElectricVehicle ev;
    private Location startLoc;
    private Location targetLoc;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @Before
    public void setUp()
    {
        // ARREGLO: EVCompany pide un nombre (String), así que se lo damos.
        EVCompany company = new EVCompany("Test Company"); 

        startLoc = new Location(0, 0);
        targetLoc = new Location(10, 10);
        
        // Creamos el coche con los 6 argumentos necesarios
        ev = new ElectricVehicle(company, startLoc, targetLoc, "Modelo Test", "TEST-01", 100);
    }

    /**
     * Prueba que el vehículo se crea correctamente.
     */
    @Test
    public void testCreation()
    {
        assertEquals("TEST-01", ev.getPlate());
        assertEquals(startLoc, ev.getLocation());
        assertEquals(100, ev.getBatteryLevel());
    }

    /**
     * Prueba que moverse reduce la batería en 5 unidades.
     */
    @Test
    public void testMoveReducesBattery()
    {
        int initialBattery = ev.getBatteryLevel();
        
        ev.act(1); // El coche debería moverse un paso
        
        // ARREGLO: Usamos assertFalse para asegurar compatibilidad con tu BlueJ
        // "Comprobamos que NO esté en la posición inicial"
        assertFalse(startLoc.equals(ev.getLocation()));
        
        // La batería debería haber bajado en 5
        assertEquals(initialBattery - 5, ev.getBatteryLevel());
    }

    /**
     * Prueba que el coche sabe si tiene batería suficiente.
     * Distancia 10 pasos * 5 kwh = 50 kwh necesarios.
     */
    @Test
    public void testEnoughBattery()
    {
        // Distancia a (10,0) es 10 pasos. Necesita 50 kwh.
        // Tiene 100, así que true.
        assertTrue(ev.enoughBattery(10));
        
        // Si la distancia fuera 30 pasos (150 kwh), no tendría suficiente.
        assertFalse(ev.enoughBattery(30));
    }
}