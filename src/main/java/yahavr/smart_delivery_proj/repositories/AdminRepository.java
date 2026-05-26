package yahavr.smart_delivery_proj.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import yahavr.smart_delivery_proj.datamodels.Admin;
import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByUsername(String username);
}