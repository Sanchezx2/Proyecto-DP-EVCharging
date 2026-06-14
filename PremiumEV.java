public class PremiumEV extends ElectricVehicle {
    public PremiumEV(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        super(company, location, targetLocation, name, plate, batteryCapacity);
    }
    @Override
    public void calculateRechargingPosition() {
        ChargingStation bestStation = null;
        int maxSpeed = -1;
        
        for (ChargingStation station : company.getCityStations()) {
            if (station.getLocation().equals(this.location)) continue;
            
            int distToStation = this.location.distance(station.getLocation());
            if (this.enoughBattery(distToStation)) {
                for (Charger c : station.getChargers()) {
                    if (c.checkCompatibility(this)) {
                        if (c.getChargingSpeed() > maxSpeed) {
                            maxSpeed = c.getChargingSpeed();
                            bestStation = station;
                        }
                    }
                }
            }
        }
        this.rechargingLocation = (bestStation != null) ? bestStation.getLocation() : null;
    }// Su lógica especial de buscar solo UltraFast se maneja 
    // en checkCompatibility de UltraFastCharger
}