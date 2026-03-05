package yahavr.smart_delivery_proj.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import yahavr.smart_delivery_proj.datamodels.User;

@Repository
public interface UserRepository extends MongoRepository<User,String>
{
    List<User> findByUsernameContainingIgnoreCase(String username);
}
