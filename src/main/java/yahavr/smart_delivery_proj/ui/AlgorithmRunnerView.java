package yahavr.smart_delivery_proj.ui;

import com.google.maps.model.LatLng;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Value;
import yahavr.smart_delivery_proj.datamodels.*;
import yahavr.smart_delivery_proj.services.*;
import yahavr.smart_delivery_proj.components.GoogleMapComponent;

import java.util.*;
import java.util.stream.Collectors;

@com.vaadin.flow.router.Route(value = "algorithm-runner", layout = MainLayout.class)
public class AlgorithmRunnerView extends VerticalLayout {

    private final OrderService orderService;
    private final VehicleService vehicleService;
    private final WarehouseService warehouseService;
    private final DistanceMatrixService matrixService;
    private final GeneticRoutingService geneticService;
    private final RouteHistoryService routeHistoryService;
    
    private GoogleMapComponent map;
    private VerticalLayout sidebarContent;
    private Button btnSave;
    
    // נשמור את המצב הנוכחי לטובת כפתור השמירה
    private Map<String, List<Order>> currentResults;
    private double[][] currentMatrix;

    public AlgorithmRunnerView(OrderService orderService, VehicleService vehicleService,
                               WarehouseService warehouseService, DistanceMatrixService matrixService,
                               GeneticRoutingService geneticService, RouteHistoryService routeHistoryService,
                               @Value("${google.maps.api.key}") String apiKey) {
        this.orderService = orderService;
        this.vehicleService = vehicleService;
        this.warehouseService = warehouseService;
        this.matrixService = matrixService;
        this.geneticService = geneticService;
        this.routeHistoryService = routeHistoryService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Header Section
        H2 title = new H2("מערכת אופטימיזציית מסלולים");
        Button btnRun = new Button("הפעל אלגוריתם", e -> executeAlgorithm());
        btnRun.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        btnSave = new Button("שמור מסלולים", e -> saveCurrentRoutes());
        btnSave.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btnSave.setVisible(false);

        Button btnBack = new Button("חזור", e -> UI.getCurrent().navigate(AdminDashboardView.class));
        
        HorizontalLayout header = new HorizontalLayout(title, btnRun, btnSave, btnBack);
        header.setAlignItems(Alignment.CENTER);
        header.setPadding(true);
        add(header);

        // Main Content: Map + Sidebar
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        
        map = new GoogleMapComponent(apiKey);
        map.getStyle().set("flex-grow", "1");

        sidebarContent = new VerticalLayout();
        Scroller sidebarScroller = new Scroller(sidebarContent);
        sidebarScroller.setWidth("400px");
        sidebarScroller.getStyle().set("border-left", "1px solid #ddd");

        mainLayout.add(map, sidebarScroller);
        add(mainLayout);
        expand(mainLayout);
    }

    private void executeAlgorithm() {
        sidebarContent.removeAll();
        List<Order> orders = orderService.getAllOrders().stream()
                .filter(o -> "PENDING".equals(o.getStatus()))
                .collect(Collectors.toList());
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        Warehouse depot = warehouseService.getMainWarehouse();

        if (orders.isEmpty() || vehicles.isEmpty() || depot == null) {
            Notification.show("חסרים נתונים להרצה (וודא קיום הזמנות ורכבים)");
            return;
        }

        LatLng depotLoc = new LatLng(depot.getLat(), depot.getLng());
        // שמירת המטריצה לשימוש עתידי בחישוב מרחקים
        this.currentMatrix = matrixService.buildDistanceMatrix(orders, depotLoc);
        this.currentResults = geneticService.calculateRoutes(orders, vehicles, depot, currentMatrix);

        if (currentResults != null && !currentResults.isEmpty()) {
            drawResults(currentResults, depotLoc, orders);
            btnSave.setVisible(true);
            btnSave.setEnabled(true);
        }
    }

    private void drawResults(Map<String, List<Order>> results, LatLng depotLoc, List<Order> allOrders) {
        map.clear();
        String[] colors = { "#FF0000", "#0000FF", "#008000", "#FFA500", "#800080" };
        int colorIdx = 0;

        map.addWarehouseMarker(depotLoc.lat, depotLoc.lng, "מחסן מרכזי");

        for (var entry : results.entrySet()) {
            List<Order> routeOrders = entry.getValue();
            if (routeOrders.isEmpty()) continue;

            String color = colors[colorIdx++ % colors.length];
            
            // חישוב מדדים עבור הכרטיס
            double dist = calculateRouteTotal(routeOrders, allOrders, true);
            long time = (long) calculateRouteTotal(routeOrders, allOrders, false);
            
            createVehicleCard(entry.getKey(), routeOrders, color, dist, time);

            List<LatLng> path = new ArrayList<>();
            path.add(depotLoc);
            routeOrders.forEach(o -> path.add(new LatLng(o.getLat(), o.getLng())));
            path.add(depotLoc);
            map.drawRealRoadRoute(path, color);
        }
    }

    private void saveCurrentRoutes() {
        if (currentResults == null) return;
        Warehouse depot = warehouseService.getMainWarehouse();
        List<Order> allOrders = orderService.getAllOrders();

        currentResults.forEach((vId, routeOrders) -> {
            double dist = calculateRouteTotal(routeOrders, allOrders, true);
            long time = (long) calculateRouteTotal(routeOrders, allOrders, false);
            routeHistoryService.saveRouteSnapshot(vId, depot.getId(), routeOrders, dist, time);
        });
        
        Notification.show("המסלולים נשמרו בהצלחה בהיסטוריה!");
        btnSave.setEnabled(false);
    }

    private double calculateRouteTotal(List<Order> route, List<Order> allOrders, boolean isDistance) {
        double total = 0;
        Map<String, Integer> orderToIdx = new HashMap<>();
        for (int i = 0; i < allOrders.size(); i++) orderToIdx.put(allOrders.get(i).getId(), i + 1);

        int prev = 0;
        for (Order o : route) {
            int curr = orderToIdx.get(o.getId());
            total += isDistance ? currentMatrix[prev][curr] : currentMatrix[prev][curr] * 0.5; // הנחה: זמן יחסי למרחק
            prev = curr;
        }
        total += isDistance ? currentMatrix[prev][0] : 0;
        return total;
    }

    private void createVehicleCard(String vehicleId, List<Order> orders, String color, double dist, long sec) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle().set("border", "2px solid " + color).set("border-radius", "8px").set("background-color", "#f9f9f9");
        
        H4 header = new H4("רכב: " + vehicleId);
        header.getStyle().set("color", color);
        
        card.add(header);
        orders.forEach(o -> card.add(new Span(o.getDescription()), new Span("🏠 " + o.getShippingAddress())));
        sidebarContent.add(card);
    }
}