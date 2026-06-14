import java.util.List;
import java.util.Objects;

/**
 * Abstract class representing a generic Electric Vehicle.
 * Subclasses define specific behaviors (Standard, Priority, Premium, VTC).
 */
public abstract class ElectricVehicle 
{
    protected EVCompany company;
    protected String name;
    protected String plate;
    protected int batteryCapacity;
    protected int batteryLevel;
    protected Location location;
    protected Location targetLocation;
    protected Location rechargingLocation;
    
    // Estadísticas
    protected int idleCount;
    protected int kwsCharged;
    protected int chargesCount;
    protected float chargesCost;
    protected int arrivingStep;

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
        this.batteryLevel = batteryCapacity;
        this.rechargingLocation = null;
        
        this.idleCount = 0;
        this.kwsCharged = 0;
        this.chargesCount = 0;
        this.chargesCost = 0.0f;
        this.arrivingStep = EVDemo.MAXSTEPS + 1;
    }

    // --- Getters y Setters Básicos ---
    public Location getLocation() { return this.location; }
    public void setLocation(Location location) {
        if(location == null) throw new NullPointerException("Location null");
        this.location = location;
    }
    public Location getTargetLocation() { return this.targetLocation; }
    public Location getRechargingLocation() { return this.rechargingLocation; }
    public int getArrivingStep() { return this.arrivingStep; }
    public void setTargetLocation(Location location) {
        if(location == null) throw new NullPointerException("Target null");
        this.targetLocation = location;
    }
    public int getBatteryLevel() { return this.batteryLevel; }
    public String getPlate() { return this.plate; }
    public String getName() { return this.name; }
    public int getIdleCount() { return this.idleCount; }
    public int getChargesCount() { return this.chargesCount; }
    
    // --- Lógica Principal ---

    public void calculateRoute() {
        int distanceToFinalTarget = this.location.distance(this.targetLocation);
        if (this.enoughBattery(distanceToFinalTarget)) {
            this.rechargingLocation = null;
        } else {
            this.calculateRechargingPosition();
        }
    }
    
    public String getStringRoute() {
        if(this.rechargingLocation == null) {
            return this.location.toString() + " -> " + this.targetLocation.toString();
        } else {
            return this.location.toString() + " -> " + this.rechargingLocation.toString() + " -> " + this.targetLocation.toString();
        }
    }

    public boolean enoughBattery(int distance) {
        // Usa el coste definido en la compañía
        int energyNeeded = distance * EVCompany.MOVINGCOST;
        return this.batteryLevel >= energyNeeded;
    }
    
    /**
     * IMPORTANTE: Ahora filtramos las estaciones.
     * Solo valen aquellas que tengan AL MENOS UN cargador compatible con ESTE vehículo.
     */
    public void calculateRechargingPosition() {
        ChargingStation bestStation = null;
        int minDistance = Integer.MAX_VALUE;
        List<ChargingStation> allStations = company.getCityStations();
        
        for (ChargingStation station : allStations) {
            if (station.getLocation().equals(this.location)) continue;
            
            // NUEVO: Chequeo de compatibilidad
            // (Asumimos que ChargingStation tendrá un método hasCompatibleCharger(ElectricVehicle v))
            // Si la estación no tiene cargadores para mí, la ignoro.
            if (!station.hasCompatibleCharger(this)) {
                continue; 
            }

            int distToStation = this.location.distance(station.getLocation());
            if (this.enoughBattery(distToStation)) {
                int distStationToFinalTarget = station.getLocation().distance(this.targetLocation);
                int totalDetourDistance = distToStation + distStationToFinalTarget;
                
                if (totalDetourDistance < minDistance) {
                    minDistance = totalDetourDistance;
                    bestStation = station;
                }
            }
        }
        
        if (bestStation != null) {
            this.rechargingLocation = bestStation.getLocation();
        } else {
            this.rechargingLocation = null;
        }
    }
    
    public boolean hasRechargingLocation(){
        return this.rechargingLocation != null;
    }
    
    public void incrementIdleCount() { this.idleCount++; }
    public void incrementCharges() { this.chargesCount++; }
    public void incrementChargesCost(float cost) { this.chargesCost += cost; }
    
    public void reduceBatteryLevel() {
        this.batteryLevel -= EVCompany.MOVINGCOST;
        if (this.batteryLevel < 0) this.batteryLevel = 0;
    }

    /**
     * Método act estándar. 
     * PriorityEV lo sobrescribirá para moverse el doble.
     */
    public void act(int step) {
        if (this.location.equals(this.targetLocation)) {
            this.incrementIdleCount();
            return;
        }
        // Freno de seguridad
        if (!this.hasRechargingLocation() && !this.enoughBattery(this.distanceToTheTargetLocation())) {
            this.incrementIdleCount(); 
            return;
        }

        Location currentTarget = (this.hasRechargingLocation()) ? this.rechargingLocation : this.targetLocation;
        
        // Moverse
        Location nextStep = this.location.nextLocation(currentTarget);
        this.setLocation(nextStep);
        this.reduceBatteryLevel();
        
        // Comprobar llegada
        if (this.hasRechargingLocation() && this.location.equals(this.rechargingLocation)) {
            this.recharge(step); 
        } else if (this.location.equals(this.targetLocation)) {
            this.arrivingStep = step;
            System.out.println("(step: " + step + " - " + this.getClass().getSimpleName() + ": " + this.plate + " at target destination ********)");
        }
    }

    public void recharge(int step) {
        int kwsNeeded = this.batteryCapacity - this.batteryLevel;
        ChargingStation station = company.getChargingStation(this.location);
        
        //Pedimos un cargador COMPATIBLE y libre
        Charger charger = station.getFreeCharger(this); 
        
        if (charger != null) {
            charger.setFree(false);
            float cost = charger.recharge(this, kwsNeeded);
            
            this.incrementCharges();
            this.incrementChargesCost(cost);
            this.kwsCharged += kwsNeeded;
            this.batteryLevel = this.batteryCapacity;
            this.rechargingLocation = null;
            charger.setFree(true);
            // Log actualizado con el tipo de vehículo
            System.out.println("(step: " + step + " - " + this.getClass().getSimpleName() + ": " + this.plate + 
           " recharges: " + kwsNeeded + "kwh at " + charger.getClass().getSimpleName() + ": " + 
           charger.getId() + " with cost: " + String.format(java.util.Locale.US, "%.2f", cost) + "€ ********)");
            calculateRoute();
            // --- NUEVO: Notificar a la empresa si corresponde ---
            if (this.notifiesCompany()) {
                this.company.notifyCharge(charger, this);
            }
        }
    } 

    public int distanceToTheTargetLocation() {
        Location currentTarget = (this.hasRechargingLocation()) ? this.rechargingLocation : this.targetLocation;
        return this.location.distance(currentTarget);
    }

    // --- toString y Equals ---
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        // Usamos getClass().getSimpleName() para que salga "StandardEV", "PriorityEV", etc.
        // Pero el formato pide "ElectricVehicle: ..." o el nombre específico? 
        // En los logs nuevos sale el tipo: (step: 47 - StandardEV: ...)
        // Mantengamos el toString interno simple.
        sb.append(this.name).append(", ");
        sb.append(this.plate).append(", ");
        sb.append(this.batteryCapacity).append("kwh, ");
        sb.append(this.batteryLevel).append("kwh, ");
        sb.append(this.chargesCount).append(", ");
        // Formato con 2 decimales para el coste si es necesario, o float simple
        sb.append(String.format(java.util.Locale.US, "%.2f", this.chargesCost)).append("€, ");
        sb.append(this.idleCount).append(", ");
        sb.append(this.location.toString());
        if (this.hasRechargingLocation()) {
            sb.append(", ").append(this.rechargingLocation.toString());
        }
        sb.append(", ").append(this.targetLocation.toString());
        return sb.toString();
    }
    
    public String getStepInfo(int step){
         // Ajuste: El prefijo ahora debe ser el nombre de la clase (StandardEV, etc.)
         return "(step: " + step + " - " + this.getClass().getSimpleName() + ": " + this.toString() + ")";
    }
    
    public String getInitialFinalInfo(){
         return "(" + this.getClass().getSimpleName() + ": " + this.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ElectricVehicle other = (ElectricVehicle) obj;
        return Objects.equals(plate, other.plate);
    }
    @Override
    public int hashCode() {
        return Objects.hash(plate);
    }
    protected boolean notifiesCompany() {
        return true; // Por defecto (Standard, Premium y VTC) SÍ notifican
    }
}