package yahavr.smart_delivery_proj.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import yahavr.smart_delivery_proj.datamodels.Vehicle;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    List<Vehicle> findByWarehouseId(String warehouseId);
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}