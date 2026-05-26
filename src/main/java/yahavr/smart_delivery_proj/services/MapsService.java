package yahavr.smart_delivery_proj.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

@Service
public class MapsService {
    @Value("${google.maps.api.key}")
    private String apiKey;

    public LatLng getLatLngFromAddress(String address) {
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(apiKey)
            .build();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            if (results.length > 0) {
                return results[0].geometry.location;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
