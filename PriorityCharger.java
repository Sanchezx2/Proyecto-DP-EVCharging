public class PriorityCharger extends Charger {
    public PriorityCharger(String id, int speed, float fee) {
        super(id, speed, fee);
    }

    @Override
    public boolean checkCompatibility(ElectricVehicle v) {
        return (v instanceof PriorityEV);
    }
    
    @Override
    public String toString() {
        return "(PriorityCharger: " + id + ", " + chargingSpeed + "kwh, " + chargingFee + "€, " + 
               eVsRecharged.size() + ", " + amountCollected + "€)";
    }
}