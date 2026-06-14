import java.util.*;

/**
 * Models the operation of an Electric Vehicle (EV) Company. 
 * It manages a fleet of {@link ElectricVehicle}s and a network of {@link ChargingStation}s.
 * @author DP classes
 * @version 2024.10.07
 */
public class EVCompany  
{
    /** El coste de batería (en kwh) por cada paso que da un vehículo */
    public static final int MOVINGCOST = 5;
    
    /** Atributo estático privado que guardará la dirección única */
    private static EVCompany instancia = null;
    
    /** Nombre de la compañía */
    private String name;
    
    /** Lista de todos los vehículos suscritos */
    private List<ElectricVehicle> subscribedVehicles;
    
    /** Lista de todas las estaciones de carga gestionadas */
    private List<ChargingStation> stations;
    /** Para registrar todo para el companyInfo */
    private Map<Charger, Set<ElectricVehicle>> chargeRegistry;
    
    /**
     * Constructor for objects of class EVCompany.
     * @param name The name of the company.
     */
    private EVCompany(String name)
    {
        this.name = name;    
        // Inicializamos las listas como vacías
        this.subscribedVehicles = new ArrayList<>();
        this.stations = new ArrayList<>();
        this.chargeRegistry = new TreeMap<>(Comparator.comparing(Charger::getId));
    
    }
    
    /** Método estático global para obtener la instancia única */
    public static EVCompany getInstance() {
        if (instancia == null) {
            // Aquí llamamos al constructor privado pasándole el nombre predefinido
            instancia = new EVCompany("Compañía EVCharging Cáceres");
        }
        return instancia;
    }

     /**
     * @return The name of the company.
     */
    public String getName()
    {
        return this.name;
    }
    
    
    /**
     * @return An unmodifiable list of all {@link ElectricVehicle}s.
     */
    public List<ElectricVehicle> getVehicles()
    {       
        return Collections.unmodifiableList(this.subscribedVehicles);
    }

    /**
     * Adds an {@link ElectricVehicle} to the company's fleet.
     * @param vehicle The electric vehicle to add.
     */
    public void addElectricVehicle(ElectricVehicle vehicle)
    {       
        this.subscribedVehicles.add(vehicle);
    }

    
    /**
     * Adds a {@link ChargingStation} to the company's network.
     * @param station The charging station to add.
     */
    public void addChargingStation(ChargingStation station)
    {       
        this.stations.add(station);
    }
    
    
    /**
     * Retrieves a {@link ChargingStation} by its unique ID.
     * @param id The ID of the station to find.
     * @return The {@link ChargingStation} with the matching ID, or {@code null} if not found.
     */
    public ChargingStation getChargingStation(String id)
    {
        // Recorremos la lista de estaciones
        for (ChargingStation station : this.stations) {
            // Comparamos el ID de la estación con el ID que buscamos
            if (station.getId().equals(id)) {
                return station; //Encontrada
            }
        }
        return null; // No se encontró
    }

    /**
     * Retrieves a {@link ChargingStation} by its {@link Location}.
     * @param location The {@link Location} of the station to find.
     * @return The {@link ChargingStation} at the matching location, or {@code null} if not found.
     */
    public ChargingStation getChargingStation(Location location)
    {
        // Recorremos la lista de estaciones
        for (ChargingStation station : this.stations) {
            // Comparamos la Ubicación de la estación con la que buscamos
            // (Usamos .equals() que ya rellenamos en Location.java)
            if (station.getLocation().equals(location)) {
                return station; // Encontrada
            }
        }
        return null; // No se encontró
    }
    
    /**
     * @return An unmodifiable list of all managed {@link ChargingStation}s.
     */
    public List<ChargingStation> getCityStations()
    {
       return Collections.unmodifiableList(this.stations);
    }
    
    
    /**
     * @return The total number of managed {@link ChargingStation}s.
     */
    public int getNumberOfStations(){
        return this.stations.size();
    }
    public void notifyCharge(Charger charger, ElectricVehicle vehicle) {
        // Si el cargador no está en el registro, lo añadimos con una lista vacía
        this.chargeRegistry.putIfAbsent(charger, new LinkedHashSet<>());
        // Añadimos el vehículo (LinkedHashSet ignora automáticamente los duplicados)
        this.chargeRegistry.get(charger).add(vehicle);
    }
    public void printCompanyInfo() {
        System.out.println("(--------------)");
        System.out.println("( Company Info )");
        System.out.println("(--------------)");
        System.out.println("(EVCompany: " + this.name + ")");
        
        for (Map.Entry<Charger, Set<ElectricVehicle>> entry : this.chargeRegistry.entrySet()) {
            Charger c = entry.getKey();
            // Imprime la info básica del cargador (asegúrate de que tu c.toString() devuelve la línea correcta)
            System.out.println(c.toString()); 
            
            // Imprime los vehículos que notificaron en ese cargador
            for (ElectricVehicle v : entry.getValue()) {
                System.out.println(v.getInitialFinalInfo());
            }
        }
    }
    
    /**
     * Clears all managed vehicles and stations, resetting the company to an empty state.
     */
    public void reset(){
        this.subscribedVehicles.clear();
        this.stations.clear();
        this.chargeRegistry.clear();
    }
    
}