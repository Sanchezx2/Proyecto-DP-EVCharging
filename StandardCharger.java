public class StandardCharger extends Charger {
    public StandardCharger(String id, int speed, float fee) {
        super(id, speed, fee);
    }

    @Override
    public boolean checkCompatibility(ElectricVehicle v) {
        // Acepta StandardEV o VtcEV
        return (v instanceof StandardEV) || (v instanceof VtcEV);
    }
    
    @Override
    public String toString() {
        return "(StandardCharger: " + id + ", " + chargingSpeed + "kwh, " + chargingFee + "€, " + 
               eVsRecharged.size() + ", " + amountCollected + "€)";
    }
}