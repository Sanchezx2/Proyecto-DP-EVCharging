import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test implementation of the {@link Location} class.
 * Provides unit tests for methods like {@code distance()} and {@code nextLocation()}.
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 * @version 2024.10.07 DP classes (adaptado a Java 8+)
 */
public class LocationTest
{
    /**
     * Default constructor for test class LocationTest.
     */
    public LocationTest()
    {
    }

    /**
     * Sets up the test fixture.
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        // No setup required for Location tests
    }

    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        // No teardown required for Location tests
    }
    
    /**
     * Test the {@code distance} method of the {@link Location} class.
     * Checks distances from a central location to surrounding points.
     */
    @Test
    public void testDistance()
    {
        Location p0 = new Location(0, 0);
        Location p1 = new Location(1, 5);
        // p0 (0,0) y p1 (1,5) -> max(|1-0|, |5-0|) = max(1, 5) = 5
        assertEquals(5, p0.distance(p1));
            
        Location p2 = new Location(6, 6);
        Location p3 = new Location(5, 2);
        // p2 (6,6) y p3 (5,2) -> max(|5-6|, |2-6|) = max(1, 4) = 4
        assertEquals(4, p2.distance(p3));
            
        // Test misma localización
        assertEquals(0, p0.distance(p0));
    }
    
    /**
     * Test the {@code nextLocation} method when the destination is adjacent 
     * (one step away in any direction).
     */
    @Test
    public void testAdjacentLocations()
    {
        Location base = new Location(5, 5);
        
        // Probamos las 8 direcciones adyacentes
        Location destN = new Location(5, 4); // Norte
        Location destS = new Location(5, 6); // Sur
        Location destE = new Location(6, 5); // Este
        Location destW = new Location(4, 5); // Oeste
        Location destNE = new Location(6, 4); // Noreste
        Location destNW = new Location(4, 4); // Noroeste
        Location destSE = new Location(6, 6); // Sureste
        Location destSW = new Location(4, 6); // Suroeste
            
        // Si el destino es adyacente, el siguiente paso ES el destino.
        // Usamos .equals() de Location (que ya estaba implementado) para comparar.
        assertEquals(destN, base.nextLocation(destN));
        assertEquals(destS, base.nextLocation(destS));
        assertEquals(destE, base.nextLocation(destE));
        assertEquals(destW, base.nextLocation(destW));
        assertEquals(destNE, base.nextLocation(destNE));
        assertEquals(destNW, base.nextLocation(destNW));
        assertEquals(destSE, base.nextLocation(destSE));
        assertEquals(destSW, base.nextLocation(destSW));
    }
    
    /**
     * Test the {@code nextLocation} method when the destination is not adjacent 
     * (more than one step away).
     */
    @Test
    public void testNonAdjacentLocations()
    {
        // --- Pruebas de movimiento diagonal ---
        
        // p0 (0,0) a p1 (1,5) -> siguiente es (1,1)
        Location p0 = new Location(0, 0);
        Location p1 = new Location(1, 5);
        assertEquals(new Location(1, 1), p0.nextLocation(p1));
            
        // p2 (6,6) a p3 (5,2) -> siguiente es (5,5)
        Location p2 = new Location(6, 6);
        Location p3 = new Location(5, 2);
        assertEquals(new Location(5, 5), p2.nextLocation(p3));

        // p5 (3,2) a p6 (6,0) -> siguiente es (4,1)
        Location p5 = new Location(3, 2);
        Location p6 = new Location(6, 0);
        assertEquals(new Location(4, 1), p5.nextLocation(p6));
            
        // --- Prueba de movimiento recto (Caso B) ---
        
        // Estamos en (1,2) y vamos a (1,5) -> siguiente es (1,3)
        Location pRectaBase = new Location(1, 2);
        Location pRectaDest = new Location(1, 5);
        assertEquals(new Location(1, 3), pRectaBase.nextLocation(pRectaDest));
    
    }
}