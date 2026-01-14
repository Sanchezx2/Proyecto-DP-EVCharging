public class PriorityEV extends ElectricVehicle {
    public PriorityEV(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        super(company, location, targetLocation, name, plate, batteryCapacity);
    }

    /**
     * Sobrescribe act para moverse 2 veces por turno.
     */
    @Override
    public void act(int step) {
        // Primer movimiento
        super.act(step);
        
        // Si después del primer movimiento NO hemos llegado y NO estamos cargando...
        // Intentamos movernos otra vez en el mismo turno.
        // Ojo: Verificamos si seguimos teniendo batería y no hemos llegado.
        boolean atDestination = this.location.equals(this.targetLocation);
        boolean atCharging = (this.hasRechargingLocation() && this.location.equals(this.rechargingLocation));
        
        // Solo nos movemos de nuevo si no hemos terminado el turno por llegada/carga
        if (!atDestination && !atCharging) {
            super.act(step);
        }
    }
}