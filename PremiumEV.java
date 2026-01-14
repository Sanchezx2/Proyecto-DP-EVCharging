public class PremiumEV extends ElectricVehicle {
    public PremiumEV(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        super(company, location, targetLocation, name, plate, batteryCapacity);
    }
    // Su lógica especial de buscar solo UltraFast se maneja 
    // en checkCompatibility de UltraFastCharger
}