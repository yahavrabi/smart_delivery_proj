package yahavr.smart_delivery_proj.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yahavr.smart_delivery_proj.datamodels.Warehouse;

@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, String> {
    Warehouse findByName(String name);
}