package yahavr.smart_delivery_proj.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "Routes")
public class Route {
    @Id
    private String id;
    private String vehicleId;
    private String warehouseId;
    private List<String> orderIds;
    
    private double totalDistance; // בק"מ
    private long totalDurationSeconds; // בשניות
    
    public Route() {}
    public Route(String vehicleId, String warehouseId, List<String> orderIds) {
        this.vehicleId = vehicleId;
        this.warehouseId = warehouseId;
        this.orderIds = orderIds;
    }

    // Getters & Setters
    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
    public long getTotalDurationSeconds() { return totalDurationSeconds; }
    public void setTotalDurationSeconds(long totalDurationSeconds) { this.totalDurationSeconds = totalDurationSeconds; }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVehicleId() {
        return vehicleId;
    }
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
    public String getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
    public List<String> getOrderIds() {
        return orderIds;
    }
    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }
    
    // ... שאר ה-Getters וה-Setters
}