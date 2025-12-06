import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Models an Electric Vehicle Charging Station.
 * A charging station contains multiple {@link Charger} units.
 * @author DP classes 
 * @version 2024.10.07
 */
public class ChargingStation
{
    private String id;
    private String city;
    private Location location;
    
    /** * Lista de cargadores. 
     * Lista esté SIEMPRE ordenada
     * usando el comparador de 3 criterios.
     */
    private List<Charger> chargers;

    /**
     * Constructor for objects of class ChargingStation.
     * @param city The city where the station is located.
     * @param id The unique identifier of the station.
     * @param location The {@link Location} of the station.
     */
    public ChargingStation(String city, String id, Location location)
    {
       this.city = city;
       this.id = id;
       this.location = location;
       this.chargers = new ArrayList<>();   //lista vacía
    }

    /**
     * @return The unique identifier of the charging station.
     */
    public String getId()
    {
        return this.id;
    }
    
    /**
     * @return The {@link Location} of the charging station.
     */
    public Location getLocation()
    { 
        return this.location;
    }

    
    /**
     * @return An unmodifiable list of all {@link Charger}s at the station.
     */
    public List<Charger> getChargers()
    {
        return Collections.unmodifiableList(this.chargers); //Lista inmodificable
    }
    
    /**
     * Retrieves the first free {@link Charger} found at the station.
     * @return A free {@link Charger}, or possibly throws an exception if none are found (depending on stream implementation details).
     * **Note: The implementation assumes at least one free charger exists if called.**
     */
    public Charger getFreeCharger()
    {
        // Recorremos nuestra lista de cargadores (que ya está ordenada)
        for (Charger charger : this.chargers) {
            
            // Usamos el getter que creamos en Charger.java
            if (charger.isFree()) {
                // ¡Encontrado! Es el mejor cargador disponible. Lo devolvemos.
                return charger;
            }
        }
        // Si el bucle termina, significa que no hay ninguno libre.
        return null;
    }
    
       
    
    /**
     * Set the current location of the charging station.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
         if(location == null) {
            throw new NullPointerException("La ubicación no puede ser nula.");
         }
         this.location = location;
    }

    /**
     * Returns a string containing complete information about the charging station, 
     * including details of all its {@link Charger}s and their usage history.
     * @return A comprehensive string representation of the station.
     */
    public String getCompleteInfo()     //Preguntar si esta bien
    {
        // Usamos StringBuilder para construir un String de varias líneas
        StringBuilder sb = new StringBuilder();
        
        // 1. Info de la estación (la línea del toString)
        sb.append(this.toString());
        
        // 2. Info completa de cada cargador
        for (Charger charger : this.chargers) {
            sb.append("\n"); // Añade un salto de línea
            
            // Llama al getCompleteInfo() del cargador
            sb.append(charger.getCompleteInfo()); 
        }
        return sb.toString();
    }
    

    /**
     * Shows a final information summary about the charging station (currently the same as {@code toString()}).
     * @return A string representation of the station's final status.
     * @deprecated Consider using {@link #toString()} or {@link #getCompleteInfo()} instead.
     */
    public String showFinalInfo()
    {
       return this.toString();
    }

    /**
     * @return A string representation of the charging station, including its ID, city, total number of EVs recharged, and location.
     */
    @Override
    public String toString()
    {
        //Formato de SIMPLE_Ouput.txt: (ChargingStation: CC00, Cáceres, 0, 10-5)
        return "(ChargingStation: " + this.id + ", " +
               this.city + ", " +
               this.getNumerEVRecharged() + ", " +
               this.location.toString() + ")"; // location.toString() da "10-5"
    }
    
    /**
     * Calculates the total number of {@link ElectricVehicle}s recharged across all {@link Charger}s at this station.
     * @return The total number of unique recharges.
     */
    public int getNumerEVRecharged(){
        int total = 0;
        // Recorremos la lista, preguntamos cuás recargas ha hecho y se suma 
        for (Charger charger : this.chargers) {
            total += charger.getNumerEVRecharged();
        }
        return total;
    }
    
    /**
     * Adds a new {@link Charger} to the station.
     * @param charger The new charger unit.
     */
    public void addCharger(Charger charger)
    {
        this.chargers.add(charger);
        //Reordena la lista
        Collections.sort(this.chargers, new ComparatorChargersId());
    }
    
}