package yahavr.smart_delivery_proj.services;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yahavr.smart_delivery_proj.datamodels.Order;

import java.util.List;

@Service
public class DistanceMatrixService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public double[][] buildDistanceMatrix(List<Order> orders, com.google.maps.model.LatLng warehouseLoc) {
        int size = orders.size() + 1; // +1 עבור המחסן
        double[][] matrix = new double[size][size];

        // 1. הגדרת ההקשר (Context) מול גוגל
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        
        // 2. הכנת רשימת הכתובות/מיקומים עבור ה-API
        String[] locations = new String[size];
        locations[0] = warehouseLoc.lat + "," + warehouseLoc.lng;
        for (int i = 0; i < orders.size(); i++) {
            locations[i + 1] = orders.get(i).getLat() + "," + orders.get(i).getLng();
        }

        try {
            // 3. ביצוע הקריאה האמיתית ל-Google Distance Matrix API
            // אנחנו מבקשים מרחקים בין כולם לכולם (מטריצה מלאה)
            DistanceMatrix result = DistanceMatrixApi.getDistanceMatrix(context, locations, locations)
                    .mode(TravelMode.DRIVING) // אפשר לשנות ל-WALKING או BICYCLING
                    .await();

            // 4. חילוץ הנתונים מהתוצאה של גוגל לתוך המערך שלנו
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (result.rows[i].elements[j].distance != null) {
                        // גוגל מחזירה מרחק במטרים - אנחנו נמיר לקילומטרים
                        matrix[i][j] = result.rows[i].elements[j].distance.inMeters / 1000.0;
                        
                        // במקום מרחק, אפשר להשתמש בזמן נסיעה (שניות)
                        // matrix[i][j] = result.rows[i].elements[j].duration.inSeconds;
                    } else {
                        matrix[i][j] = 9999.0; // הגנה למקרה שאין נתיב נסיעה
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Google API Error: " + e.getMessage());
            // Fallback לחישוב אווירי במקרה של שגיאת API (כדי שהמערכת לא תקרוס)
            return buildAirDistanceMatrix(orders, warehouseLoc);
        } finally {
            context.shutdown(); // סגירת החיבור לחיסכון במשאבים
        }

        return matrix;
    }

    private double[][] buildAirDistanceMatrix(List<Order> orders, LatLng warehouseLoc) {
        int size = orders.size() + 1;
        double[][] matrix = new double[size][size];

        // 1. יצירת מערך עזר שכולל את כל הנקודות (המחסן באינדקס 0)
        LatLng[] allLocations = new LatLng[size];
        allLocations[0] = warehouseLoc;
        for (int i = 0; i < orders.size(); i++) {
            allLocations[i + 1] = new LatLng(orders.get(i).getLat(), orders.get(i).getLng());
        }

        // 2. לולאה כפולה לחישוב המרחק לפי פיתגורס רגיל
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    matrix[i][j] = 0.0; // מרחק מנקודה לעצמה הוא תמיד 0
                } else {
                    // הפרשים בין הקואורדינטות
                    double dLat = allLocations[i].lat - allLocations[j].lat;
                    double dLng = allLocations[i].lng - allLocations[j].lng;
                    
                    // חישוב המרחק האוקלידי (שורש של סכום הריבועים)
                    matrix[i][j] = Math.sqrt((dLat * dLat) + (dLng * dLng));
                }
            }
        }

        return matrix;
    }
}