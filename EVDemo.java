import java.util.*;

public class EVDemo
{
    // CONSTANTES Y ATRIBUTOS
    public static final int MAXX = 20; 
    public static final int MAXY = 20; 
    public static final int MAXSTEPS = 50; 
    
    private EVCompany company;
    private List<ElectricVehicle> vehicles; 
    private List<ChargingStation> stations;
    
    private static final DemoType DEMO = DemoType.SIMPLE; 

    public EVDemo()
    {
        this.company = EVCompany.getInstance();
        this.vehicles = new ArrayList<>();
        this.stations = new ArrayList<>();
        reset();
    }
    public void run() {        
        for(int step = 0; step < MAXSTEPS; step++) {
            step(step);
        }
        showFinalInfo();
    }

    public void step(int step) {
        for (ElectricVehicle ev : vehicles) {
            ev.act(step);
            System.out.println(ev.getStepInfo(step));
        }
    }

    public void reset() {
        vehicles.clear();
        stations.clear();
        company.reset();
        createElectricVehicles();
        createStations(); 
        createChargers();
        configureRoutes();
        showInitialInfo();
    }

    private void createElectricVehicles() {
        Location [] locations = {new Location(1,1), new Location(1,1), new Location(1,19), new Location(1,19), 
                                new Location(19,1), new Location(19,1), new Location(10,19), new Location(19,10),
                                new Location(10,10), new Location(10,10)};

        Location [] targetLocations = {new Location(20,20), new Location(20,20), new Location(19,1), new Location(19,1), 
                                       new Location(1,19), new Location(1,19), new Location(19,10), new Location(10,19),
                                       new Location(10,20), new Location(20,10)};
                                        
        for (int i=0; i < DEMO.getNumVehiclesToCreate(); i++){
            ElectricVehicle ev;
            int module = i % VehicleTier.numTiers();

            if (VehicleTier.values()[module] == VehicleTier.PRIORITY) 
                ev = new PriorityEV(company, locations[i], targetLocations[i], ("EV"+i), (i+"CCC"), (i+1)*(20-i));
            else if (VehicleTier.values()[module] == VehicleTier.VTC) 
                ev = new VtcEV(company, locations[i], targetLocations[i], ("EV"+i), (i+"CCC"), (i+1)*(20-i));
            else if (VehicleTier.values()[module] == VehicleTier.PREMIUM) 
                ev = new PremiumEV(company, locations[i], targetLocations[i], ("EV"+i), (i+"CCC"), (i+1)*(20-i));
            else 
                ev = new StandardEV(company, locations[i], targetLocations[i], ("EV"+i), (i+"CCC"), (i+1)*(20-i));
            
            // Los guardamos en nuestra lista local para que el bucle "step" los encuentre
            vehicles.add(ev); 
            company.addElectricVehicle(ev);
        }
        Collections.sort(this.vehicles, new ComparatorEVPlate());
    }

    private void createStations() {  
        Location [] locations = {new Location(5,5), new Location(15,15), new Location(5,15), new Location(15,5), new Location(10,10)};
                                
        for (int i=0; i < DEMO.getNumStationsToCreate(); i++){
            ChargingStation newStation = new ChargingStation("Cáceres", "CC0" + i, locations[i]);
            stations.add(newStation); // Los guardamos en nuestra lista local
            company.addChargingStation(newStation);
        }
        Collections.sort(this.stations, new ComparatorChargingStationId());
    }

    private void createChargers() {  
        //Usamos nuestra lista local 'this.stations' que SÍ está perfectamente ordenada por ID
        int j = 0;
        for (ChargingStation station : this.stations){
            for (int i = 0; i < DEMO.getNumChargersToCreate(); i++){
                Charger ch;
                if (i % DEMO.getNumChargersToCreate() == (j % DEMO.getNumStationsToCreate() - 1)) {
                    ch = new SolarCharger(station.getId() + "_00" + i, ((i + j + 1) * 20), ((i + 1) * 0.20f));
                }    
                else if (i % DEMO.getNumChargersToCreate() == (j % DEMO.getNumStationsToCreate())) {
                    ch = new UltraFastCharger(station.getId() + "_00" + i, ((i + j + 1) * 20), ((i + 1) * 0.20f));
                } 
                else if (i % DEMO.getNumChargersToCreate() == (j % DEMO.getNumStationsToCreate() + 1)) {
                    ch = new PriorityCharger(station.getId() + "_00" + i, ((i + j + 1) * 20), ((i + 1) * 0.20f));
                }    
                else {
                    ch = new StandardCharger(station.getId() + "_00" + i, ((i + 1) * 20), ((i + 1) * 0.20f));
                }    
                station.addCharger(ch);
            }
            j++;
        }    
    }
    
    private void configureRoutes() {
        for(ElectricVehicle ev : vehicles) {
            ev.calculateRoute();
        }
    }

    // MÉTODOS DE IMPRESIÓN
    private void showInitialInfo() {
        System.out.println("( "+company.getName()+" )");
        System.out.println("(-------------------)");
        System.out.println("( Electric Vehicles )");
        System.out.println("(-------------------)");
        for (ElectricVehicle ev : vehicles) {
            System.out.println(ev.getInitialFinalInfo());
        }
        System.out.println("(-------------------)");
        System.out.println("( Charging Stations )");
        System.out.println("(-------------------)");
        for (ChargingStation station : stations) {
            System.out.println(station.toString());
            for (Charger charger : station.getChargers()) {
                System.out.println(charger.toString());
            }
        }
        System.out.println("(------------------)");
        System.out.println("( Simulation start )");
        System.out.println("(------------------)");
    }

    private void showFinalInfo() {
        System.out.println("(-------------------)");
        System.out.println("( Final information )");        
        System.out.println("(-------------------)");
        System.out.println("( Electric Vehicles )");
        System.out.println("(-------------------)");
        
        Collections.sort(vehicles, new ComparatorEVArrivingStep());
        for (ElectricVehicle ev : vehicles) {
            System.out.println(ev.getInitialFinalInfo());
        }
        System.out.println("(-------------------)");
        System.out.println("( Charging Stations )");
        System.out.println("(-------------------)");
        
        Collections.sort(stations, new ComparatorChargingStationNumberRecharged());
        for (ChargingStation station : stations) {
            System.out.println(station.getCompleteInfo());
        }
    }

    // EL ARRANQUE GLOBAL
    public static void main() {
        EVDemo demo = new EVDemo();
        demo.run();
    }
}