import java.util.*;
/**
 * Provides a simple demonstration and simulation environment for the 
 * Electric Vehicle (EV) and Charging Station model.
 * It sets up the environment and runs the simulation for a fixed number of steps.
 *
 * Scenarios: 
 * <ul>
 * <li>Demo SIMPLE (demo=DemoType.SIMPLE): Two vehicles are created.</li>
 * <li>Demo MEDIUM (demo=DemoType.MEDIUM): Five vehicles are created.</li>
 * <li>Demo ADVANCED(demo=DemoType.ADVANCED): Eight vehicles are created.</li>
 * </ul>
 * * @author DP Clasess
 * @version 2025
 */
public class EVDemo
{
    
    /** The maximum X coordinate for the grid (number of rows). */
    public static final int MAXX = 20; 
    /** The maximum Y coordinate for the grid (number of columns). */
    public static final int MAXY = 20; 
    
    /** The maximum number of turns. */
    public static final int MAXSTEPS = 50; 
    
    /** The company managing the EVs and charging stations. */
    private EVCompany company;
    
    /** * The simulation's vehicles; they are the electric vehicles of the company. 
     * @see ElectricVehicle 
     */
    private List<ElectricVehicle> vehicles; 
    
    /** * The charging stations available in the city. 
     * @see ChargingStation 
     */
    private List<ChargingStation> stations;
    
    /** Constant for selecting the demo scenario, using the {@link DemoType} enumeration. */
    private static final DemoType DEMO=DemoType.SIMPLE;
    
        
    /**
     * Constructor for objects of class EVDemo.
     * Initializes the company, vehicles, and stations lists, and sets the number of 
     * vehicles based on the {@code DEMO} constant before calling {@code reset()}.
     */
    public EVDemo()
    {
        this.company = new EVCompany("Compañía EVCharging Cáceres"); // 
        this.vehicles = new ArrayList<>();
        this.stations = new ArrayList<>();
        reset();
    }

    /**
     * Run the demo for a fixed number of steps (50 steps).
     * In each step, all vehicles perform their action.
     */
    public void run()
    {        
        //Ejecutamos un número de pasos la simulación.
        //En cada paso, cada persona de reparto realiza su acción
        for(int step = 0; step < MAXSTEPS; step++) {
            step(step);
        }
        // Después de que el bucle termine, mostramos el informe final
        showFinalInfo();

    }

    /**
     * Run the demo for one step by requesting all vehicles to act.
     * @param step The current step number of the simulation.
     */
    public void step(int step)
    {
        // Recorremos la lista de vehículos (ordenada por matrícula)
        for (ElectricVehicle ev : vehicles) {
            ev.act(step);
            
            // Imprimimos su estado en CADA turno
            System.out.println(ev.getStepInfo(step));
        }
    }

    /**
     * Reset the demo to a starting point.
     * Clears existing vehicles and stations, resets the company, and re-creates
     * the initial setup.
     */
    public void reset()
    {
        vehicles.clear();
        stations.clear();
                
        company.reset();

        createElectricVehicles();
        createStations(); 
        createChargers();
        configureRoutes();
        showInitialInfo();
    }

    
    /**
     * Creates the {@link ElectricVehicle}s based on {@code numVehiclesCreated}, assigns them
     * starting and target {@link Location}s, and adds them to the company.
     * The vehicles list is sorted by plate using {@link ComparatorEVPlate}.
     */
    private void createElectricVehicles() {
        Location [] locations = {new Location(10,13), new Location(8,4), new Location(8,4), new Location(15,10), 
                                new Location(1,1), new Location(2,2), new Location(11,13), new Location(14,16)};
        Location [] targetLocations = {new Location(1,1), new Location(19,19), new Location(12,17), new Location(4,4), 
                                        new Location(1,10), new Location(5,5), new Location(8,7), new Location(19,19)};
                                        
        //createLocations(locations,targetLocations);
        for (int i=0;i < DEMO.getNumVehiclesToCreate();i++){
            ElectricVehicle ev = new ElectricVehicle(company, locations[i],targetLocations[i],("EV"+i),(i+"CCC"),(i+1)*15);
            vehicles.add(ev);
            company.addElectricVehicle(ev);
        }
        
        Collections.sort(vehicles, new ComparatorEVPlate());
        
    }
    

    /**
     * Creates predefined {@link ChargingStation}s and adds them to the company.
     * The stations list is sorted by ID using {@link ComparatorChargingStationId}.
     */
    private void createStations() {  
        Location [] locations = {new Location(10,5), new Location(10,11), new Location(14,16), new Location(8,4)};
                                
        for (int i=0;i<DEMO.getNumStationsToCreate();i++){
            ChargingStation newStation = new ChargingStation("Cáceres","CC0" + i,locations[i]);
            stations.add(newStation);
            company.addChargingStation(newStation);
        }
        
        Collections.sort(stations, new ComparatorChargingStationId());
    }

    /**
     * Creates a fixed number of {@link Charger} units for each {@link ChargingStation}
     * and orders the chargers within each station.
     */
    private void createChargers() {  
            
            //Recorremos la lista de ESTACIONES
            for (ChargingStation station : stations){
                
                //Para cada estación, creamos 4 cargadores
                for (int i=0;i<DEMO.getNumChargersToCreate();i++){
                    
                    //Creamos el cargador (esta línea ya venía en el esqueleto)
                    Charger newCharger = new Charger(station.getId() + "_00" + i,((i+1)*20),((i+1)*0.20f));
                    
                    //Añadimos el cargador a la ESTACIÓN, addCharger ya ordena 
                    station.addCharger(newCharger);
                }
            }    
        }
    
    
     /**
      * Instructs each {@link ElectricVehicle} to calculate its initial route.
      * This determines if an intermediate recharging stop is necessary.
      */
     private void configureRoutes() {
         // Recorremos la lista de vehículos
         for(ElectricVehicle ev : vehicles) {
             // Le decimos a cada uno que calcule su ruta
             ev.calculateRoute();
         }
     }

    /**
     * Displays the initial information for the simulation, including 
     * the details of all {@link ElectricVehicle}s and {@link ChargingStation}s.
     */
    private void showInitialInfo() {
        System.out.println("( "+company.getName()+" )");
        System.out.println("(-------------------)");
        System.out.println("( Electric Vehicles )");
        System.out.println("(-------------------)");
        
        // Recorremos la lista de vehículos (ya ordenada por matrícula)
        for (ElectricVehicle ev : vehicles) {
            // Usamos el método que creamos en ElectricVehicle
            System.out.println(ev.getInitialFinalInfo());
        }

        System.out.println("(-------------------)");
        System.out.println("( Charging Stations )");
        System.out.println("(-------------------)");
        
        // Recorremos la lista de estaciones (ya ordenada por ID)
        for (ChargingStation station : stations) {
                    System.out.println(station.toString());
                    // (La lista 'getChargers()' ya viene ordenada desde ChargingStation)
                    for (Charger charger : station.getChargers()) {
                        // Imprimimos el cargador
                        System.out.println(charger.toString());
                    }
                }
        System.out.println("(------------------)");
        System.out.println("( Simulation start )");
        System.out.println("(------------------)");
    }

    /**
     * Displays the final information after the simulation has run.
     * Vehicles are sorted by arrival step using {@link ComparatorEVArrivingStep}.
     * Stations are sorted by the number of recharges using {@link ComparatorChargingStationNumerRecharged}.
     */
    private void showFinalInfo() {
 
        System.out.println("(-------------------)");
        System.out.println("( Final information )");        
        System.out.println("(-------------------)");

        System.out.println("(-------------------)");
        System.out.println("( Electric Vehicles )");
        System.out.println("(-------------------)");
        
        // 1. Ordenamos la lista 'vehicles' con el nuevo comparador
        Collections.sort(vehicles, new ComparatorEVArrivingStep());
        
        // 2. Imprimimos cada vehículo
        for (ElectricVehicle ev : vehicles) {
            System.out.println(ev.getInitialFinalInfo());
        }

        System.out.println("(-------------------)");
        System.out.println("( Charging Stations )");
        System.out.println("(-------------------)");
       
        // 1. Ordenamos la lista 'stations' con el comparador que ya teníamos
        Collections.sort(stations, new ComparatorChargingStationNumberRecharged());
        
        // 2. Imprimimos la INFO COMPLETA de cada estación
        for (ChargingStation station : stations) {
            // Usamos getCompleteInfo() como pide el PDF [cite: 364, 375]
            System.out.println(station.getCompleteInfo());
        }
    }
    
    
    /**
     * The main entry point for running the EVDemo simulation.
     * Creates an instance of {@code EVDemo} and starts the simulation.
     */
    public static void main() {
        EVDemo demo = new EVDemo();
        demo.run();
    }
}
