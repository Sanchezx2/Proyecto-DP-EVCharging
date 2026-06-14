import java.util.*;

/**
 * Models an Electric Vehicle Charging Station.
 * A charging station contains multiple {@link Charger} units.
 * @author DP classes 
 * @version 2025
 */
public class ChargingStation implements Iterable<Charger>
{
    private String id;
    private String city;
    private Location location;
    
    /** * Lista de cargadores. 
     * El PDF exige que esta lista esté SIEMPRE ordenada.
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
       if (city == null || id == null || location == null) {
            throw new NullPointerException("City, ID and Location cannot be null");
       }
       this.city = city;
       this.id = id;
       this.location = location;
       this.chargers = new ArrayList<>();
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
        return Collections.unmodifiableList(this.chargers);
    }

    /**
     * Adds a new {@link Charger} to the station and keeps the list sorted.
     * @param charger The new charger unit.
     */
    public void addCharger(Charger charger)
    {
        this.chargers.add(charger);
        // Usamos la clase ComparatorChargersId que ya tienes
        Collections.sort(this.chargers, new ComparatorChargersId());
    }

    /**
     * Finds the best available charger for a specific vehicle.
     * It iterates through the sorted list and returns the first one that is 
     * both FREE and COMPATIBLE with the vehicle.
     * @param v The vehicle requesting a charger.
     * @return A compatible and free {@link Charger}, or null if none is available.
     */
    /**
     * Finds the CHEAPEST available charger for a specific vehicle.
     * It iterates through the list, filters by FREE and COMPATIBLE, 
     * and returns the one with the lowest cost.
     * @param v The vehicle requesting a charger.
     * @return The cheapest compatible and free {@link Charger}, or null if none is available.
     */
    public Charger getFreeCharger(ElectricVehicle v)
    {
        // 1. LÓGICA EXCLUSIVA PARA VTC (Busca el más barato)
        if (v instanceof VtcEV) {
            Charger cheapestCharger = null;
            float minCost = Float.MAX_VALUE; 
            
            for (Charger c : this.chargers) {
                // Comprobamos si está libre y es compatible (Standard o Solar)
                if (c.isFree() && c.checkCompatibility(v)) {
                    float currentCost = c.getChargingFee(); 
                    
                    // Si es estrictamente más barato, lo guardamos.
                    // Al usar "<" y no "<=", respetamos automáticamente la regla de desempate
                    // del proyecto: si valen lo mismo, nos quedamos con el primero que vimos.
                    if (currentCost < minCost) {
                        minCost = currentCost;
                        cheapestCharger = c;
                    }
                }
            }
            return cheapestCharger;
        } 
        
        // 2. LÓGICA PARA STANDARD, PREMIUM Y PRIORITY (Buscan el primero)
        else {
            for (Charger c : this.chargers) {
                // Como la lista ya está ordenada por velocidad (criterio principal), 
                // el primer cargador libre y compatible que encontremos es el correcto.
                if (c.isFree() && c.checkCompatibility(v)) {
                    return c;
                }
            }
            return null; // Si termina el bucle y no hay ninguno, devuelve null
        }
    }
    /**
     * Checks if the station has at least one charger compatible with the vehicle,
     * regardless of whether it is currently free or busy.
     * Used by route planning to discard incompatible stations.
     * @param v The vehicle checking compatibility.
     * @return true if there is at least one compatible charger.
     */
    public boolean hasCompatibleCharger(ElectricVehicle v)
    {
        for (int i = 0; i < this.chargers.size(); i++) {
            Charger c = this.chargers.get(i);
            if (c.checkCompatibility(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the total number of {@link ElectricVehicle}s recharged across all {@link Charger}s at this station.
     * @return The total number of unique recharges.
     */
    public int getNumerEVRecharged(){
        int total = 0;
        for (Charger charger : this.chargers) {
            // Asumimos que Charger tiene un método para contar sus recargas (tamaño de la lista)
            // Si no tienes un método público, usa charger.getEVsRecharged().size()
            total += charger.getEVsRecharged().size();
        }
        return total;
    }

    /**
     * @return A string representation of the charging station.
     */
    @Override
    public String toString()
    {
        return "(ChargingStation: " + this.id + ", " +
               this.city + ", " +
               this.getNumerEVRecharged() + ", " +
               this.location.toString() + ")";
    }

    /**
     * Devuelve toda la información de la estación y de sus cargadores.
     * Método necesario para el informe final (Anexo I).
     * IMPORTANTE: Requiere que Charger tenga el método getCompleteInfo().
     */
    public String getCompleteInfo()
    {
        StringBuilder sb = new StringBuilder();
        
        // 1. Info de la estación
        sb.append(this.toString());
        
        // 2. Info completa de cada cargador
        for (Charger charger : this.chargers) {
            sb.append("\n"); // Salto de línea
            sb.append(charger.getCompleteInfo()); 
        }
        return sb.toString();
    }

    // --- MÉTODOS EXTRA (Iterator, Equals, HashCode) ---

    @Override
    public Iterator<Charger> iterator() {
        return chargers.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChargingStation other = (ChargingStation) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}