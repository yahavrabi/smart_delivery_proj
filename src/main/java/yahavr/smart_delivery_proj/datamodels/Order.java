package yahavr.smart_delivery_proj.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Orders")
public class Order {
    @Id
    private String id;
    private String description; // תיאור החבילה
    private String shippingAddress; // הכתובת המקורית שהוזנה
    
    // קואורדינטות לצורך הצגה על המפה וחישוב מרחקים באלגוריתם
    private double lat;
    private double lng;
    
    private String status; // PENDING, DELIVERED
    private String userId;

    public Order() {}

    public Order(String description, String shippingAddress, double lat, double lng, String userId) {
        this.description = description;
        this.shippingAddress = shippingAddress;
        this.lat = lat;
        this.lng = lng;
        this.userId = userId;
        this.status = "PENDING";
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}