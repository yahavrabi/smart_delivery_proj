package yahavr.smart_delivery_proj.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yahavr.smart_delivery_proj.datamodels.Route;
import java.util.List;

@Repository
public interface RouteRepository extends MongoRepository<Route, String> {
    List<Route> findByVehicleId(String vehicleId);
}