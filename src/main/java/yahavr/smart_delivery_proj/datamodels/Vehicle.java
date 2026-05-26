package yahavr.smart_delivery_proj.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Vehicle")
public class Vehicle {
    @Id
    private String id;
    private String licensePlate; // מספר רישוי
    private double maxCapacity;   // קיבולת מקסימלית (למשל בק"ג או נפח)
    private String warehouseId;  // השיוך למחסן שממנו הוא יוצא

    public Vehicle() {}

    public Vehicle(String licensePlate, double maxCapacity, String warehouseId) {
        this.licensePlate = licensePlate;
        this.maxCapacity = maxCapacity;
        this.warehouseId = warehouseId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public double getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(double maxCapacity) { this.maxCapacity = maxCapacity; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
}