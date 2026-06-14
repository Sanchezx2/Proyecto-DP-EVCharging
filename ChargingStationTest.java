import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * Pruebas unitarias para la clase ChargingStation.
 */
public class ChargingStationTest {

    private ChargingStation station;
    private EVCompany company;
    
    // Variables para los vehículos y cargadores
    private ElectricVehicle cocheNormal;
    private ElectricVehicle cocheVtc;
    
    private Charger cargadorCaro;
    private Charger cargadorBarato;

    @Before
    public void setUp() {
        // 1. Inicializamos la estación
        Location loc = new Location(5, 5);
        station = new ChargingStation("Cáceres", "CC_TEST", loc);
        company = EVCompany.getInstance();
        
        // 2. IMPORTANTE: Aquí debes instanciar tus clases reales
        // Sustituye "StandardEV", "VtcEV", "StandardCharger" por los constructores que uses.
        
        // cocheNormal = new StandardEV(company, loc, new Location(10,10), "Normal", "1111AAA", 50);
        // cocheVtc = new VtcEV(company, loc, new Location(10,10), "VTC", "2222BBB", 50);
        
        // Creamos dos cargadores compatibles, uno caro (ej. 1.0€) y otro barato (ej. 0.2€)
        // cargadorCaro = new StandardCharger("CH_01", 50, 1.0f);
        // cargadorBarato = new StandardCharger("CH_02", 50, 0.2f);
    }

    @Test
    public void testAddCharger() {
        // Descomenta estas líneas cuando tengas los cargadores instanciados arriba
        /*
        station.addCharger(cargadorCaro);
        assertEquals("La estación debería tener 1 cargador", 1, station.getChargers().size());
        
        station.addCharger(cargadorBarato);
        assertEquals("La estación debería tener 2 cargadores", 2, station.getChargers().size());
        */
    }

    @Test
    public void testGetFreeChargerStandard() {
        // Comprueba que el coche normal coge el PRIMER cargador libre (el caro, si se insertó primero)
        /*
        station.addCharger(cargadorCaro);
        station.addCharger(cargadorBarato);
        
        Charger asignado = station.getFreeCharger(cocheNormal);
        assertNotNull("Debería encontrar un cargador", asignado);
        assertEquals("El coche normal debería coger el primero de la lista (CH_01)", "CH_01", asignado.getId());
        */
    }

    @Test
    public void testGetFreeChargerVTC() {
        // Comprueba que el VTC ignora el primero y coge el MÁS BARATO
        /*
        station.addCharger(cargadorCaro);
        station.addCharger(cargadorBarato);
        
        Charger asignado = station.getFreeCharger(cocheVtc);
        assertNotNull("Debería encontrar un cargador", asignado);
        assertEquals("El VTC debería saltarse el primero y coger el más barato (CH_02)", "CH_02", asignado.getId());
        */
    }

    @Test
    public void testGetFreeChargerBusy() {
        // Comprueba qué pasa si todos están ocupados
        /*
        cargadorCaro.setFree(false);
        cargadorBarato.setFree(false);
        
        station.addCharger(cargadorCaro);
        station.addCharger(cargadorBarato);
        
        Charger asignado = station.getFreeCharger(cocheNormal);
        assertNull("Si todos están ocupados, debe devolver null", asignado);
        */
    }
}