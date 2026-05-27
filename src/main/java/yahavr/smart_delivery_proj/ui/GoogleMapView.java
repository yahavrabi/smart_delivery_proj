package yahavr.smart_delivery_proj.ui;

import com.google.maps.model.LatLng;
import com.vaadin.flow.component.html.Div;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleMapView extends Div {

    public GoogleMapView(String apiKey) {
        setId("map");
        setWidthFull();
        setHeight("500px");
        getStyle().set("border", "1px solid #ccc");

        // אתחול המפה ושמירת מערכים ריקים לאחסון אובייקטים
        String initScript = "window.initMap = function() {" +
                "  this.map = new google.maps.Map(document.getElementById('map'), {" +
                "    center: {lat: 31.7683, lng: 35.2137}," +
                "    zoom: 12" +
                "  });" +
                "  this.markers = [];" +
                "  this.directionsRenderers = [];" +
                "  this.polylines = [];" +
                "}.bind(this);" + // bind(this) כדי ש-this בתוך initMap יהיה ה-Div
                
                "if (!document.getElementById('google-maps-script')) {" +
                "  var script = document.createElement('script');" +
                "  script.id = 'google-maps-script';" +
                "  script.src = 'https://maps.googleapis.com/maps/api/js?key=' + $0 + '&callback=initMap';" +
                "  script.async = true;" +
                "  script.defer = true;" +
                "  document.head.appendChild(script);" +
                "} else if (window.google && window.google.maps) {" +
                "  window.initMap();" +
                "}";

        getElement().executeJs(initScript, apiKey);
    }

    public void addMarker(double lat, double lng, String title) {
        getElement().executeJs(
                "if (!this.markers) this.markers = [];" +
                "var marker = new google.maps.Marker({" +
                "  position: {lat: $0, lng: $1}," +
                "  map: this.map," +
                "  title: $2" +
                "});" +
                "this.markers.push(marker);",
                lat, lng, title);
    }

    public void addNumberedMarker(double lat, double lng, String title, String label) {
        getElement().executeJs(
                "if (!this.markers) this.markers = [];" +
                "var marker = new google.maps.Marker({" +
                "  position: {lat: $0, lng: $1}," +
                "  map: this.map," +
                "  title: $2," +
                "  label: { text: $3, color: 'white', fontWeight: 'bold' }" +
                "});" +
                "this.markers.push(marker);",
                lat, lng, title, label);
    }

    public void addWarehouseMarker(double lat, double lng, String title) {
        getElement().executeJs(
                "if (!this.markers) this.markers = [];" +
                "var marker = new google.maps.Marker({" +
                "  position: {lat: $0, lng: $1}," +
                "  map: this.map," +
                "  title: $2," +
                "  icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'," +
                "  label: { text: 'W', color: 'white', fontWeight: 'bold' }" +
                "});" +
                "this.markers.push(marker);",
                lat, lng, title);
    }

    public void drawRealRoadRoute(List<LatLng> points, String color) {
        if (points.size() < 2) return;

        // הכנת הנקודות בפורמט JSON עבור JS
        String originJson = String.format("{lat: %f, lng: %f}", points.get(0).lat, points.get(0).lng);
        String destJson = String.format("{lat: %f, lng: %f}", points.get(points.size()-1).lat, points.get(points.size()-1).lng);
        
        String waypointsJson = points.subList(1, points.size() - 1).stream()
                .map(p -> String.format("{location: {lat: %f, lng: %f}, stopover: true}", p.lat, p.lng))
                .collect(Collectors.joining(",", "[", "]"));

        getElement().executeJs(
                "if (!this.directionsRenderers) this.directionsRenderers = [];" +
                "var directionsService = new google.maps.DirectionsService();" +
                "var directionsRenderer = new google.maps.DirectionsRenderer({" +
                "  map: this.map," +
                "  suppressMarkers: true," +
                "  polylineOptions: { strokeColor: $0, strokeWeight: 5, strokeOpacity: 0.8 }" +
                "});" +
                "this.directionsRenderers.push(directionsRenderer);" +
                
                "directionsService.route({" +
                "  origin: " + originJson + "," +
                "  destination: " + destJson + "," +
                "  waypoints: " + waypointsJson + "," +
                "  travelMode: 'DRIVING'" +
                "}, function(response, status) {" +
                "  if (status === 'OK') {" +
                "    directionsRenderer.setDirections(response);" +
                "  } else {" +
                "    console.error('Directions request failed: ' + status);" +
                "  }" +
                "});", color);
    }

    public void addPolyline(List<LatLng> points, String color) {
        String pathJson = points.stream()
                .map(p -> String.format("{lat: %f, lng: %f}", p.lat, p.lng))
                .collect(Collectors.joining(",", "[", "]"));

        getElement().executeJs(
                "if (!this.polylines) this.polylines = [];" +
                "var poly = new google.maps.Polyline({" +
                "  path: " + pathJson + "," +
                "  geodesic: true," +
                "  strokeColor: $0," +
                "  strokeOpacity: 1.0," +
                "  strokeWeight: 3," +
                "  map: this.map" +
                "});" +
                "this.polylines.push(poly);", color);
    }

    public void setCenter(double lat, double lng) {
        getElement().executeJs("if (this.map) this.map.setCenter({lat: $0, lng: $1});", lat, lng);
    }

    /**
     * מנקה את כל האלמנטים שנוספו למפה
     */
    public void clear() {
        getElement().executeJs(
                "if (this.markers) {" +
                "  this.markers.forEach(m => m.setMap(null));" +
                "  this.markers = [];" +
                "}" +
                "if (this.directionsRenderers) {" +
                "  this.directionsRenderers.forEach(d => d.setMap(null));" +
                "  this.directionsRenderers = [];" +
                "}" +
                "if (this.polylines) {" +
                "  this.polylines.forEach(p => p.setMap(null));" +
                "  this.polylines = [];" +
                "}");
    }
}