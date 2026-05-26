package yahavr.smart_delivery_proj.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Warehouse")
public class Warehouse {
    @Id
    private String id;
    private String name;
    private String fullAddress; // הכתובת כפי שהוזנה/חזרה מגוגל
    
    // שדות קריטיים ל-Google Maps
    private double lat; 
    private double lng;

    public Warehouse() {}

    public Warehouse(String name, String fullAddress, double lat, double lng) {
        this.name = name;
        this.fullAddress = fullAddress;
        this.lat = lat;
        this.lng = lng;
    }
    

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String setName() { return this.name = "main-warehouse"; }
    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }
}