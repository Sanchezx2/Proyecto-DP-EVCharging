import java.util.List;
/**
 * Model the common elements of an Electric Vehicle (EV) that operates 
 * within the simulation, moving towards a target and potentially recharging.
 * * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2024.10.07
 */
public class ElectricVehicle 
{
    private EVCompany company;          //compañía
    
    private String name;                //modelo del vehículo
    
    private String plate;               //matrícula
    
    private int batteryCapacity;        //Capacidad de la batería
    
    private int batteryLevel;           //Nivel actual de batería   (entre 0 y batteryCapacity)
    
    private Location location;          // ubicación 
    
    private Location targetLocation;    //Destino del vehículo
    
    private Location rechargingLocation; // El destino INTERMEDIO (una estación) si necesita recargar
    
    // --- Campos para Estadísticas ---
    
    private int idleCount;      //Contador de turnos que el vehículo está parado
    
    private int kwsCharged;     //Total de KWs cargados en toda la simulación
    
    private int chargesCount;   //Número total de recargas hechas
    

    private float chargesCost;  //Coste total de todas las recargas
    
    private int arrivingStep;   //Turno (step) en el que llegó a su destino final

    /**
     * Constructor of class ElectricVehicle.
     * @param company The EV's operating company. Must not be null.
     * @param location The EV's starting {@link Location}. Must not be null.
     * @param targetLocation The EV's final destination {@link Location}. Must not be null.
     * @param name The name of the vehicle.
     * @param plate The license plate of the vehicle.
     * @param batteryCapacity The maximum capacity of the battery.
     * @throws NullPointerException If company, location, or targetLocation is null.
     */
    public ElectricVehicle(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity)
    {
        if(company == null || location == null || targetLocation == null) {
            throw new NullPointerException("Company, Location y TargetLocation no pueden ser nulos.");
        }
        this.company = company;
        this.location = location;
        this.targetLocation = targetLocation;
        this.name = name;
        this.plate = plate;
        this.batteryCapacity = batteryCapacity;
        this.batteryLevel = batteryCapacity; // Batería llena al empezar 
        this.rechargingLocation = null;      // Aún no hay ruta de recarga
        
        this.idleCount = 0;
        this.kwsCharged = 0;
        this.chargesCount = 0;
        this.chargesCost = 0.0f;
        
        // Un número alto para indicar que no ha llegado.
        this.arrivingStep = EVDemo.MAXSTEPS + 1;
    }

    
    /**
     * Get the current location.
     * @return Where this vehicle is currently located.
     */
    public Location getLocation()
    {
        return this.location;
    }

    /**
     * Set the current location.
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
     * Get the final target location.
     * @return Where this vehicle is ultimately headed.
     */
    public Location getTargetLocation()
    {
        return this.targetLocation;
    }

    /**
     * Get the temporary recharging location.
     * @return The {@link Location} of the next {@link ChargingStation} to visit, or null if no recharge is planned.
     */
    public Location getRechargingLocation()
    {
        return this.rechargingLocation;
    }
    
    
    /**
     * Get the simulation step when the vehicle arrived at its final target location.
     * @return The arriving step.
     */
    public int getArrivingStep()
    {
        return this.arrivingStep;
    }
    
    
    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setTargetLocation(Location location)
    {
        if(location == null) {
            throw new NullPointerException("El destino final no puede ser nulo.");
        }
        this.targetLocation = location;
    }

    
    /**
     * Calculates the optimal route for the vehicle. 
     * If there isn't enough battery to reach the target, it attempts to find an intermediate 
     * {@link ChargingStation} and sets it as the {@code rechargingLocation}.
     */
    public void calculateRoute()
    {
        int distanceToFinalTarget = this.location.distance(this.targetLocation);
    
        if (this.enoughBattery(distanceToFinalTarget)) {
            this.rechargingLocation = null;
        }
        else {
            this.calculateRechargingPosition();
        }
    }
    
    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public String getStringRoute()
    {
        if(this.rechargingLocation == null) {
            // Ruta directa
            return this.location.toString() + " -> " + this.targetLocation.toString();
        }
        else {
            // Ruta con parada
            return this.location.toString() + " -> " + 
                   this.rechargingLocation.toString() + " -> " + 
                   this.targetLocation.toString();
        }
    }
    

    /**
     * Checks if the current battery level is sufficient to cover a given distance.
     * @param distanceToTargetLocation The distance to check.
     * @return {@code true} if the battery level is enough, {@code false} otherwise.
     */
    public boolean enoughBattery(int distanceToTargetLocation)
    {
        // Calcular la energía necesaria
        int energyNeeded = distanceToTargetLocation * EVCompany.MOVINGCOST;
         return this.batteryLevel >= energyNeeded;

    }
    
    
    /**
     * Determines the optimal intermediate {@link ChargingStation} to visit for recharging
     * if the vehicle cannot reach the final target directly.
     * Sets {@code rechargingLocation} to the chosen station's location.
     */
    public void calculateRechargingPosition()
    {
        ChargingStation bestStation = null;
        int minDistance = Integer.MAX_VALUE;
        List<ChargingStation> allStations = company.getCityStations();
        
        // Recorremos la lista estación por estación
        for (ChargingStation station : allStations) {
            
            // comprobamos si la estación NO está en mi ubicación.
            // Solo entramos al bloque si la estación es distinta a donde estoy.
            if (!station.getLocation().equals(this.location)) {
                
                // Distancia desde MÍ a esta ESTACIÓN
                int distToStation = this.location.distance(station.getLocation());
                
                // ¿Tengo batería para llegar a esta estación?
                if (this.enoughBattery(distToStation)) {
                    
                    // SÍ. Ahora calculo la ruta completa del desvío
                    int distStationToFinalTarget = station.getLocation().distance(this.targetLocation);
                    int totalDetourDistance = distToStation + distStationToFinalTarget;
                    
                    // ¿Es esta ruta mejor que la que ya tenía?
                    if (totalDetourDistance < minDistance) {
                        
                        // Si es mejor, la guardo como la nueva ganadora
                        minDistance = totalDetourDistance;
                        bestStation = station;
                    }
                }
            }
        }
        
        if (bestStation != null) {
            // Encontramos una estación: guardamos su ubicación
            this.rechargingLocation = bestStation.getLocation();
        } else {
            this.rechargingLocation = null;
        }
    }
    
    
     /**
      * Checks if the vehicle has a planned recharging stop.
      * @return Whether or not this vehicle has a recharging location set.
      */
     public boolean hasRechargingLocation(){
        return this.rechargingLocation != null;     //si no devuelve null, significa que hay parada
     }

    
     /**
      * @return The number of simulation steps this vehicle has been idle.
      */
    public int getIdleCount()
    {
        return this.idleCount;
    }

     /**
      * @return The count of total recharges performed by this vehicle.
      */
    public int getChargesCount()
    {
        return this.chargesCount;
    }
    
    /**
     * Increment the number of steps on which this vehicle has been idle.
     */
    public void incrementIdleCount()
    {
        this.idleCount++; // Suma 1 al contador
    }

    
     /**
      * Get the Manhattan-like distance to the final target location from the current location.
      * @return The distance to the target location.
      */
     public int distanceToTheTargetLocation()
     {
        Location currentTarget;
        
        if (this.hasRechargingLocation()) {
            // El destino es la estación de recarga
            currentTarget = this.rechargingLocation;
        } else {
            // El destino es el final
            currentTarget = this.targetLocation;
        }
        // Calculamos la distancia desde donde estamos (this.location)
        // hasta el destino actual (currentTarget)
        return this.location.distance(currentTarget);
     }

     /**
      * Simulates the recharging process when the vehicle arrives at a {@code rechargingLocation}.
      * The battery is charged to full capacity, the cost is calculated, and the route is recalculated.
      * @param step The current step of the simulation.
      */
    public void recharge(int step)
    {
        int kwsNeeded = this.batteryCapacity - this.batteryLevel;
        
        //Encontramos la estación y el cargador en el que estamos
        ChargingStation station = company.getChargingStation(this.location);
        Charger charger = station.getFreeCharger(); // Busca el mejor cargador libre
        
        if (charger != null) {
            //Ocupamos el cargador
            charger.setFree(false);
            // Calcular el coste
            float cost = charger.recharge(this, kwsNeeded);
            this.incrementCharges();
            this.incrementChargesCost(cost);
            this.kwsCharged += kwsNeeded; // Sumamos los kwh cargados
            
            //Rellenamos la batería
            this.batteryLevel = this.batteryCapacity;
            
            //Eliminamos la parada de recarga (ya hemos recargado)
            this.rechargingLocation = null;
            
            //¡Liberamos el cargador!
            charger.setFree(true);
            
            //Imprimimos el mensaje de recarga
            System.out.println("(step: " + step + " - ElectricVehicle: " + this.plate + 
                               " recharges: " + kwsNeeded + "kwh at charger: " + 
                               charger.getId() + " with cost: " + cost + "€ ********)");
            calculateRoute();
        }
    } 
    
    /**
     * Increments the count of recharges performed by this vehicle.
     */
    public void incrementCharges()
    {
         this.chargesCount++; 
    }
    
    /**
     * Adds a cost amount to the total charges cost.
     * @param cost The cost of the last recharge.
     */
    public void incrementChargesCost(float cost)
    {
         this.chargesCost += cost;
    }   
     
     /**
      * Carries out a single step of the vehicle's actions.
      * Moves one step towards the target (recharging or final) or stays idle.
      * @param step The current step of the simulation.
      */
     public void act(int step)
     {
        // 1. ¿Ya estaba en el destino FINAL al empezar el turno?
        if (this.location.equals(this.targetLocation)) {
            this.incrementIdleCount();
            return;
        }

        // 2. SEGURIDAD: ¿Estoy "tirado"? (Antes de moverme)
        // Si NO voy a una estación Y NO tengo batería para el destino final...
        if (!this.hasRechargingLocation() && !this.enoughBattery(this.distanceToTheTargetLocation())) {
            this.incrementIdleCount(); 
            return; // Me quedo quieto (esto arregla el EV0)
        }

        // 3. Preparar movimiento
        Location currentTarget;
        if (this.hasRechargingLocation()) {
            currentTarget = this.rechargingLocation;
        } else {
            currentTarget = this.targetLocation;
        }

        // 4. MOVERSE
        Location nextStep = this.location.nextLocation(currentTarget);
        this.setLocation(nextStep);
        this.reduceBatteryLevel();
        
        // 5. Comprobar dónde he aterrizado (DESPUÉS de moverme)
        
        // Caso A: ¿Acabamos de llegar a la ESTACIÓN de recarga?
        if (this.hasRechargingLocation() && this.location.equals(this.rechargingLocation)) {
            // ¡Sí! Recargamos inmediatamente en este mismo turno.
            this.recharge(step); 
        }
        
        // Caso B: ¿Acabamos de llegar al DESTINO FINAL?
        else if (this.location.equals(this.targetLocation)) {
            // ¡Sí! Registramos la llegada.
            this.arrivingStep = step;
            System.out.println("(step: " + step + " - ElectricVehicle: " + this.plate +
                               " at target destination ********)");
        }
    }
     
    /**
     * Reduces the battery level by the cost of one movement step (defined in {@link EVCompany#MOVINGCOST}).
     * Ensures the battery level does not go below zero.
     */
    public void reduceBatteryLevel(){
        this.batteryLevel -= EVCompany.MOVINGCOST;
        
        // Nos aseguramos de que la batería no sea negativa
        if (this.batteryLevel < 0) {
            this.batteryLevel = 0;
        }   
    }

    
    /**
     * Returns a detailed string representation of the electric vehicle.
     * @return A string containing the vehicle's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append(", ");
        sb.append(this.plate).append(", ");
        sb.append(this.batteryCapacity).append("kwh, ");
        sb.append(this.batteryLevel).append("kwh, ");
        sb.append(this.chargesCount).append(", ");
        sb.append(this.chargesCost).append("€, ");
        sb.append(this.idleCount).append(", ");
        sb.append(this.location.toString());
        
        // Añadimos la estación de recarga SOLO si existe
        if (this.hasRechargingLocation()) {
            sb.append(", ").append(this.rechargingLocation.toString());
        }
        
        sb.append(", ").append(this.targetLocation.toString());
        
        return sb.toString();
    }

    /**
     * Generates a string containing the vehicle's details prefixed with the current step number.
     * @param step The current simulation step.
     * @return A formatted string for a step log.
     */
    public String getStepInfo(int step){
         return "(step: " + step + " - ElectricVehicle: " + this.toString() + ")";
    }
    
    /**
     * Generates a string of the vehicle's initial or final status for summary display.
     * @return The output of {@link #toString()} wrapped in parentheses.
     */
    public String getInitialFinalInfo(){
         return "(ElectricVehicle: " + this.toString() + ")";
    }

    /**
     * @return La matrícula (plate) del vehículo.
     */
    public String getPlate()
    {
        return this.plate;
    }
}