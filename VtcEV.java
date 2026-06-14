public class VtcEV extends ElectricVehicle {
    public VtcEV(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        super(company, location, targetLocation, name, plate, batteryCapacity);
    }
    @Override
    public void calculateRechargingPosition() {
        ChargingStation bestStation = null;
        float minCost = Float.MAX_VALUE;
        
        for (ChargingStation station : company.getCityStations()) {
            if (station.getLocation().equals(this.location)) continue;
            
            int distToStation = this.location.distance(station.getLocation());
            if (this.enoughBattery(distToStation)) {
                // Buscamos si tiene el cargador más barato de la ciudad
                for (Charger c : station.getChargers()) {
                    if (c.checkCompatibility(this)) {
                        if (c.getChargingFee() < minCost) {
                            minCost = c.getChargingFee();
                            bestStation = station;
                        }
                    }
                }
            }
        }
        this.rechargingLocation = (bestStation != null) ? bestStation.getLocation() : null;
    }
}
