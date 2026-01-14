import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class representing a generic charger.
 * Subclasses define specific compatibility and pricing rules.
 * @author DP classes 
 * @version 2025
 */
public abstract class Charger
{
    protected String id;
    protected int chargingSpeed; // kwh
    protected float chargingFee; // € / kwh
    
    /** Colección de vehículos que han cargado aquí */
    protected List<ElectricVehicle> eVsRecharged; 
    
    /** Cantidad total recaudada */
    protected float amountCollected; 
    
    /** Indica si el cargador está libre u ocupado */
    protected boolean free;

    /**
     * Constructor for objects of class Charger.
     * @param id The unique identifier of the charger.
     * @param speed The maximum charging speed in kWh.
     * @param fee The cost per kWh for charging.
     */
    public Charger(String id, int speed, float fee)
    {
        this.id = id;
        this.chargingSpeed = speed;
        this.chargingFee = fee;
        
        this.eVsRecharged = new ArrayList<>();
        this.amountCollected = 0.0f;
        this.free = true;
    }

    // --- MÉTODOS ABSTRACTOS Y LÓGICA DE HERENCIA ---

    /**
     * Comprueba si este cargador es compatible con el vehículo dado.
     * Cada subclase (Standard, Priority, etc.) implementará su propia regla.
     */
    public abstract boolean checkCompatibility(ElectricVehicle v);

    /**
     * Realiza la recarga. Calcula el coste (con posibles descuentos/recargos)
     * y registra la operación.
     */
    public float recharge(ElectricVehicle vehicle, int kwsRecharging) {
        // 1. Verificación de seguridad (aunque ya se chequea antes)
        if (!checkCompatibility(vehicle)) {
            return 0.0f; 
        }
        
        // 2. Calcular el coste (usando el método que pueden sobrescribir los hijos)
        float cost = calculateCost(kwsRecharging);
        
        // 3. Actualizar métricas
        this.amountCollected += cost;
        this.addEvRecharged(vehicle);
        
        return cost;
    }

    /**
     * Calcula el coste base. Las subclases pueden sobrescribirlo (ej. SolarCharger).
     */
    protected float calculateCost(int kwh) {
        return kwh * this.chargingFee;
    }

    // --- MÉTODOS DE GESTIÓN DE DATOS ---

    public void addEvRecharged(ElectricVehicle vehicle){
        this.eVsRecharged.add(vehicle);
    }
    
    public int getNumerEVRecharged(){
        return this.eVsRecharged.size();
    }
    
    public List<ElectricVehicle> getEVsRecharged() {
        return new ArrayList<>(this.eVsRecharged);
    }

    // --- INFORMES ---

    public String getCompleteInfo()
    {
         StringBuilder sb = new StringBuilder();
         sb.append(this.toString());
         for(ElectricVehicle ev : this.eVsRecharged) {
             sb.append("\n"); 
             sb.append(ev.getInitialFinalInfo()); 
         }
         return sb.toString();
    }

    @Override
    public String toString()
    {
        // Formato: (StandardCharger: CC00_002, 60kwh, 0.6€, 0, 0.00€)
        // Usamos getClass().getSimpleName() para que salga "StandardCharger", "SolarCharger", etc.
        return "(" + this.getClass().getSimpleName() + ": " + this.id + ", " +
               this.chargingSpeed + "kwh, " +
               this.chargingFee + "€, " +
               this.getNumerEVRecharged() + ", " +
               this.amountCollected + "€)";
    }

    // --- GETTERS Y SETTERS ---

    public String getId() { return id; }
    public int getChargingSpeed() { return chargingSpeed; }
    public float getChargingFee() { return chargingFee; }
    public boolean isFree() { return free; }
    public void setFree(boolean free) { this.free = free; }
    
    // --- EQUALS Y HASHCODE ---
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Charger other = (Charger) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}