package yahavr.smart_delivery_proj.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yahavr.smart_delivery_proj.datamodels.User;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // לצורך התחברות
    User findByUsername(String username);
    
    // לצורך בדיקה לפני הרשמה
    boolean existsByUsernameIgnoreCase(String username);
    
    // לצורך חיפוש ברשימה
    List<User> findByUsernameContainingIgnoreCase(String username);
}