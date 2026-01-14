public class SolarCharger extends Charger {
    public SolarCharger(String id, int speed, float fee) {
        super(id, speed, fee);
    }

    @Override
    public boolean checkCompatibility(ElectricVehicle v) {
        return (v instanceof VtcEV);
    }

    @Override
    protected float calculateCost(int kwh) {
        float baseCost = super.calculateCost(kwh);
        return baseCost * 0.90f; // Aplica 10% descuento
    }
    
    @Override
    public String toString() {
        return "(SolarCharger: " + id + ", " + chargingSpeed + "kwh, " + chargingFee + "€, " + 
               eVsRecharged.size() + ", " + amountCollected + "€)";
    }
}