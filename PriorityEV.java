public class PriorityEV extends ElectricVehicle {
    public PriorityEV(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        super(company, location, targetLocation, name, plate, batteryCapacity);
    }

    /**
     * Sobrescribe act para moverse 2 veces por turno.
     */
    @Override
    public void act(int step) {
        // 1. Si ya está en su destino final definitivo, suma inactividad de turno y termina
        if (this.location.equals(this.targetLocation)) {
            this.incrementIdleCount();
            return;
        }
    
        boolean seHaMover = false;
    
        // Damos un máximo de dos saltos por turno
        for (int salto = 0; salto < 2; salto++) {
            Location currentTarget = (this.hasRechargingLocation()) ? this.rechargingLocation : this.targetLocation;
            
            if (this.location.equals(currentTarget)) {
                break; 
            }
    
            // CORRECCIÓN: El freno de seguridad solo se activa si la batería está completamente vacía (nivel <= 0)
            // o si no tiene suficiente para dar un único paso (5kwh)
            if (this.batteryLevel < 5) {
                break;
            }
    
            // Dar el salto
            Location nextStep = this.location.nextLocation(currentTarget);
            this.setLocation(nextStep);
            this.reduceBatteryLevel(); // Resta 5kwh por celda
            seHaMover = true;
    
            // Comprobamos si este salto le ha hecho llegar a la estación de recarga
            if (this.hasRechargingLocation() && this.location.equals(this.rechargingLocation)) {
                this.recharge(step); 
                break; 
            } 
            // Comprobamos si este salto le ha hecho llegar al destino definitivo
            else if (this.location.equals(this.targetLocation)) {
                this.arrivingStep = step;
                System.out.println("(step: " + step + " - " + this.getClass().getSimpleName() + ": " + this.plate + " at target destination ********)");
                break; 
            }
        }
    
        // CORRECCIÓN REGLA DE TUTORÍA: Si el coche no se ha podido mover en todo el turno 
        // porque está esperando en la estación o no tiene batería, suma 1 al idleCount del turno global
        if (!seHaMover) {
            this.incrementIdleCount();
        }
    }
}