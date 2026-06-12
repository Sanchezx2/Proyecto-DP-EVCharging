public class PriorityEV extends ElectricVehicle {
    public PriorityEV(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        super(company, location, targetLocation, name, plate, batteryCapacity);
    }

    /**
     * Sobrescribe act para moverse 2 veces por turno.
     */
    @Override
    public void act(int step) {
        if (this.location.equals(this.targetLocation)) {
            this.incrementIdleCount();
            return;
        }

        boolean seHaMovido = false;

        for (int salto = 0; salto < 2; salto++) {
            Location currentTarget = (this.hasRechargingLocation()) ? this.rechargingLocation : this.targetLocation;
            
            if (this.location.equals(currentTarget)) {
                break; 
            }

            // EL FRENO ORIGINAL SALVADOR: Si no tiene estación y no llega a su destino, se queda quieta.
            if (!this.hasRechargingLocation() && !this.enoughBattery(this.distanceToTheTargetLocation())) {
                break; 
            }

            Location nextStep = this.location.nextLocation(currentTarget);
            this.setLocation(nextStep);
            this.reduceBatteryLevel(); 
            seHaMovido = true;

            if (this.hasRechargingLocation() && this.location.equals(this.rechargingLocation)) {
                this.recharge(step); 
                break; 
            } 
            else if (this.location.equals(this.targetLocation)) {
                this.arrivingStep = step;
                System.out.println("(step: " + step + " - " + this.getClass().getSimpleName() + ": " + this.plate + " at target destination ********)");
                break; 
            }
        }

        // Si el freno actuó y no dio ningún salto, se considera inactividad
        if (!seHaMovido) {
            this.incrementIdleCount();
        }
    }
}