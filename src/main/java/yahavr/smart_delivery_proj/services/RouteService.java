package yahavr.smart_delivery_proj.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import yahavr.smart_delivery_proj.datamodels.Order;
import yahavr.smart_delivery_proj.datamodels.Route;
import yahavr.smart_delivery_proj.repositories.RouteRepository;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    public void saveRouteSnapshot(String vehicleId, String warehouseId, List<Order> orders, double dist, long time) {
        List<String> orderIds = orders.stream().map(Order::getId).sorted().collect(Collectors.toList());

        // בדיקה: האם קיים מסלול עם אותם נתונים?
        boolean exists = routeRepository.findAll().stream()
                .anyMatch(r -> r.getVehicleId().equals(vehicleId)
                        && r.getOrderIds().stream().sorted().collect(Collectors.toList()).equals(orderIds));

        if (!exists) {
            Route route = new Route(vehicleId, warehouseId, orderIds);
            route.setTotalDistance(dist);
            route.setTotalDurationSeconds(time);
            routeRepository.save(route);
        }
    }

    
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<yahavr.smart_delivery_proj.datamodels.Route> getAllRoutes() {
        return routeRepository.findAll();
    }
    
    public void deleteRoute(String id) {
        routeRepository.deleteById(id);
    }
}
