/**
 * Enumeration that defines the types of pre-configured demonstration scenarios 
 * for the simulation. Each type specifies the number of vehicles, stations, 
 * and chargers to be created.
 * * @author DP classes
 * @version 2025.20.10
 */
public enum DemoType
{
    /** Simple scenario: 2 vehicles, 5 stations, 4 chargers per station. */
    NANO("NANO DEMO", 2, 2, 3),
    /** Simple scenario: 2 vehicles, 5 stations, 4 chargers per station. */
    SIMPLE ("SIMPLE DEMO", 4, 5, 4),
    /** Medium scenario: 5 vehicles, 5 stations, 4 chargers per station. */
    MEDIUM ("MEDIUM DEMO", 7, 5, 4),
    /** Advanced scenario: 8 vehicles, 5 stations, 4 chargers per station. */
    ADVANCED ("ADVANCED DEMO", 10, 5, 4);
    
    /** Descriptive name of the scenario. */
    private String name;
    /** Number of {@link ElectricVehicle}s to create. */
    private int numVehiclesToCreate;
    /** Number of {@link ChargingStation}s to create. */
    private int numStationsToCreate;
    /** Number of {@link Charger}s to create per station. */
    private int numChargersToCreate;
    
    /**
     * Constructor for the demo types.
     * @param name Descriptive name.
     * @param numVehiclesToCreate Number of vehicles.
     * @param numStationsToCreate Number of stations.
     * @param numChargersToCreate Number of chargers per station.
     */
    DemoType(String name, int numVehiclesToCreate, int numStationsToCreate, int numChargersToCreate){
        this.name = name;
        this.numVehiclesToCreate = numVehiclesToCreate;
        this.numStationsToCreate = numStationsToCreate;
        this.numChargersToCreate = numChargersToCreate;
    }
    
    /**
     * @return The descriptive name of the demo.
     */
    public String getName(){
        return name;
    }
    
    /**
     * @return The number of vehicles that will be created for this scenario.
     */
    public int getNumVehiclesToCreate(){
        return numVehiclesToCreate;
    }
    
    /**
     * @return The number of charging stations that will be created for this scenario.
     */
    public int getNumStationsToCreate(){
        return numStationsToCreate;
    }
    
    /**
     * @return The number of chargers that will be created in each station.
     */
    public int getNumChargersToCreate(){
        return numChargersToCreate;
    }
}