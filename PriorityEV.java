public class PriorityEV extends ElectricVehicle {
    public PriorityEV(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        super(company, location, targetLocation, name, plate, batteryCapacity);
    }

    /**
     * Sobrescribe act para moverse 2 veces por turno.
     */
    @Override
public void act(int step) {
    // Si ya está en su destino final definitivo, suma inactividad y termina
    if (this.location.equals(this.targetLocation)) {
        this.incrementIdleCount();
        return;
    }

    // Damos un máximo de dos saltos por turno (bucle de 2 iteraciones)
    for (int salto = 0; salto < 2; salto++) {
        // Determinamos a dónde va en este instante
        Location currentTarget = (this.hasRechargingLocation()) ? this.rechargingLocation : this.targetLocation;
        
        // Si ya está en el objetivo intermedio o final en mitad de los saltos, frena
        if (this.location.equals(currentTarget)) {
            break; 
        }

        // Freno de seguridad por batería
        if (!this.hasRechargingLocation() && !this.enoughBattery(this.distanceToTheTargetLocation())) {
            break;
        }

        // Dar el salto
        Location nextStep = this.location.nextLocation(currentTarget);
        this.setLocation(nextStep);
        this.reduceBatteryLevel(); // Resta 5kwh por celda

        // Comprobamos si este salto le ha hecho llegar a la estación de recarga
        if (this.hasRechargingLocation() && this.location.equals(this.rechargingLocation)) {
            this.recharge(step); // Se recarga e iguala rechargingLocation a null
            break; // Regla de negocio: si llega a cargar, se para en este turno
        } 
        // Comprobamos si este salto le ha hecho llegar al destino definitivo
        else if (this.location.equals(this.targetLocation)) {
            this.arrivingStep = step;
            System.out.println("(step: " + step + " - " + this.getClass().getSimpleName() + ": " + this.plate + " at target destination ********)");
            break; // Frena de inmediato
        }
    }
}
}