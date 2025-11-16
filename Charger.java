import java.util.ArrayList;
import java.util.List;
/**
 * Model a charger unit within a {@link ChargingStation}.
 * It tracks its charging capabilities, fee, and the electric vehicles it has recharged.
 * * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2024.10.07
 */
public class Charger
{
    private String id;
    private int chargingSpeed; // kwh
    private float chargingFee; // € / kwh
    
    /** Colección de vehículos que han cargado aquí */
    private List<ElectricVehicle> eVsRecharged; 
    
    /** Cantidad total recaudada */
    private float amountCollected; 
    
    /** Indica si el cargador está libre u ocupado */
    private boolean free;
    

    /**
     * Constructor for objects of class Charger.
     * @param id The unique identifier of the charger.
     * @param speed The maximum charging speed in kWh.
     * @param fee The cost per kWh for charging.
     */
    public Charger(String id, int speed, float fee)
    {
        // Asigna los valores recibidos
        this.id = id;
        this.chargingSpeed = speed;
        this.chargingFee = fee;
        
        // Asigna los valores por defecto
        this.eVsRecharged = new ArrayList<>();
        this.amountCollected = 0.0f;    //Porque es de tipo float
        this.free = true;
    }

    
    /**
     * Returns a string representation of the charger, including its ID, speed, fee, and the number of EVs recharged.
     * @return A string representation of the charger.
     */
    @Override
    public String toString()
    {
        // Debe coincidir con el formato: (Charger: CC00_003, 80kwh, 0.8€, 0, 0.0€) 
        return "(Charger: " + this.id + ", " +
               this.chargingSpeed + "kwh, " +
               this.chargingFee + "€, " +
               this.getNumerEVRecharged() + ", " +
               this.amountCollected + "€)";
    }

    
    /**
     * Returns a complete string representation of the charger, including details of all {@link ElectricVehicle}s it has recharged.
     * @return A string containing complete information about the charger and its usage history.
     */
    public String getCompleteInfo()
    {
         // 1. Empezamos con la info normal del toString
         StringBuilder sb = new StringBuilder();
         sb.append(this.toString());
         
         // 2. Añadimos cada vehículo de la lista (ordenados cronológicamente)
         for(ElectricVehicle ev : this.eVsRecharged) {
             sb.append("\n"); // Añade un salto de línea
             
             // ¡OJO! Esta línea dará error hasta que rellenemos la clase ElectricVehicle.
             // Es normal, no te preocupes. Solo la dejamos preparada.
             sb.append(ev.getInitialFinalInfo()); 
         }
         return sb.toString();
    }
    
    /**
     * Adds an {@link ElectricVehicle} to the list of vehicles that have been recharged by this charger.
     * @param vehicle The electric vehicle that was recharged.
     */
    public void addEvRecharged(ElectricVehicle vehicle){
        // Usamos el método .add() de ArrayList para añadir el vehículo a nuestra lista
        this.eVsRecharged.add(vehicle);
    }
    
    /**
     * @return The total number of {@link ElectricVehicle}s that have been recharged by this charger.
     */
    public int getNumerEVRecharged(){
        // Usamos el método .size() de ArrayList para saber cuántos elementos hay
        return this.eVsRecharged.size();
    }
    
    
    /**
     * Simulates the charging process for an {@link ElectricVehicle}.
     * Increases the amount collected and registers the vehicle as recharged.
     * @param vehicle The vehicle to recharge.
     * @param kwsRecharging The amount of kWh to be recharged.
     * @return The cost of the recharge operation.
     */
    public float recharge(ElectricVehicle vehicle,int kwsRecharging){
        // 1. Calcular el coste
        float cost = kwsRecharging * this.chargingFee;
        
        // 2. Sumar al total recaudado
        this.amountCollected += cost;
        
        // 3. Añadir el vehículo a la lista de recargados
        this.addEvRecharged(vehicle);
        
        // 4. Devolver el coste de esta operación
        return cost;
    }
    
    // --- MÉTODOS GETTERS Y SETTERS ---

    /**
     * @return El ID único del cargador.
     */
    public String getId() {
        return id;
    }

    /**
     * @return La velocidad de carga en kwh.
     */
    public int getChargingSpeed() {
        return chargingSpeed;
    }

    /**
     * @return La tarifa de carga en €/kwh.
     */
    public float getChargingFee() {
        return chargingFee;
    }
    
    /**
     * @return true si el cargador está libre, false si está ocupado.
     */
    public boolean isFree() {
        return free;
    }
    
    /**
     * Permite cambiar el estado del cargador (libre/ocupado).
     * @param free El nuevo estado.
     */
    public void setFree(boolean free) {
        this.free = free;
    }
}