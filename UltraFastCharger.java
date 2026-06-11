public class UltraFastCharger extends Charger {
    public UltraFastCharger(String id, int speed, float fee) {
        super(id, speed, fee);
    }

    @Override
    public boolean checkCompatibility(ElectricVehicle v) {
        return (v instanceof PremiumEV);
    }

    @Override
    protected float calculateCost(int kwh) {
        float baseCost = super.calculateCost(kwh);
        return baseCost * 1.10f; // Aplica 10% cargo adicional
    }
}