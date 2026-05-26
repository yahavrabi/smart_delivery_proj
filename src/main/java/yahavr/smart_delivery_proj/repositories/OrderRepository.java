package yahavr.smart_delivery_proj.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yahavr.smart_delivery_proj.datamodels.Order;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByStatus(String status);
    List<Order> findByUserId(String userId);
    void deleteByUserId(String userId);
}