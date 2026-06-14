

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * Clase de pruebas unitarias para EVCompany.
 */
public class EVCompanyTest {

    private EVCompany company;
    
    // Objetos de prueba (Dummies)
    private Location loc1;
    private Location loc2;
    private ChargingStation station1;
    private ChargingStation station2;
    // NOTA: Como ElectricVehicle es abstracta, instanciamos un coche concreto (ej. VtcEV o StandardEV)
    private ElectricVehicle ev1; 

    /**
     * Este método se ejecuta SIEMPRE antes de cada @Test.
     * Sirve para inicializar los datos y limpiar la empresa (Singleton).
     */
    @Before
    public void setUp() {
        // 1. Obtenemos la instancia única
        company = EVCompany.getInstance();
        
        // 2. IMPORTANTE: Limpiamos la empresa para que un test no afecte a otro
        company.reset();
        
        // 3. Preparamos datos de prueba
        loc1 = new Location(5, 5);
        loc2 = new Location(10, 10);
        
        station1 = new ChargingStation("Cáceres", "CC01", loc1);
        station2 = new ChargingStation("Mérida", "BA01", loc2);
        
        // Asumiendo que tienes una clase concreta como StandardEV o VtcEV
        Location target = new Location(20, 20);
        // ev1 = new StandardEV(company, loc1, target, "Coche1", "1234ABC", 50); 
    }

    @Test
    public void testSingleton() {
        // Comprobamos que si pedimos la instancia dos veces, es exactamente el mismo objeto en memoria
        EVCompany otraReferencia = EVCompany.getInstance();
        assertSame("El patrón Singleton falla, se han creado dos instancias diferentes", company, otraReferencia);
        
        // Comprobamos el nombre
        assertEquals("Compañía EVCharging Cáceres", company.getName());
    }

    @Test
    public void testAddAndGetStations() {
        // Verificamos que empieza vacía
        assertEquals(0, company.getNumberOfStations());
        
        // Añadimos estaciones
        company.addChargingStation(station1);
        company.addChargingStation(station2);
        
        // Verificamos contadores
        assertEquals(2, company.getNumberOfStations());
        assertEquals(2, company.getCityStations().size());
    }

    @Test
    public void testGetChargingStationById() {
        company.addChargingStation(station1);
        company.addChargingStation(station2);
        
        // Buscamos una estación que existe
        ChargingStation encontrada = company.getChargingStation("CC01");
        assertNotNull("Debería encontrar la estación CC01", encontrada);
        assertEquals("CC01", encontrada.getId());
        
        // Buscamos una estación que NO existe
        ChargingStation noEncontrada = company.getChargingStation("Invento");
        assertNull("Debería devolver null si la estación no existe", noEncontrada);
    }

    @Test
    public void testGetChargingStationByLocation() {
        company.addChargingStation(station1);
        
        // Buscamos por la misma localización (5, 5)
        ChargingStation encontrada = company.getChargingStation(new Location(5, 5));
        assertNotNull("Debería encontrar la estación en 5-5", encontrada);
        assertEquals("CC01", encontrada.getId());
    }

    @Test
    public void testReset() {
        // Llenamos la empresa
        company.addChargingStation(station1);
        // company.addElectricVehicle(ev1); // Descomenta cuando tengas la línea 39 lista
        
        // Verificamos que tiene datos
        assertEquals(1, company.getNumberOfStations());
        
        // Limpiamos
        company.reset();
        
        // Verificamos que está vacía de nuevo
        assertEquals(0, company.getNumberOfStations());
        assertEquals(0, company.getVehicles().size());
        assertEquals(0, company.getCityStations().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableLists() {
        company.addChargingStation(station1);
        
        // Obtenemos la lista de estaciones
        List<ChargingStation> lista = company.getCityStations();
        
        // Intentamos modificar la lista directamente (debería lanzar UnsupportedOperationException)
        // porque usaste Collections.unmodifiableList()
        lista.add(station2); 
    }
}