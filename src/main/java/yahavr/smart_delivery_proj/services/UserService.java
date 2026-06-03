package yahavr.smart_delivery_proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yahavr.smart_delivery_proj.datamodels.User;
import yahavr.smart_delivery_proj.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    private OrderService orderService;

    // הרשמה
    public void insertUser(User user) throws Exception {
        if (userRepo.existsByUsernameIgnoreCase(user.getUsername())) {
            //user.setPassword(PasswordHelper.encode(user.getPassword()));
            throw new Exception("שם המשתמש כבר קיים במערכת");
        }
        userRepo.insert(user);
    }

    // התחברות
    public User authenticate(String username, String password) throws Exception {
        User user = userRepo.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("שם משתמש או סיסמה שגויים");
        }
        return user;
    }

    // חיפוש עבור דף הרשימה
    public List<User> searchUsers(String query) {
        if (query == null || query.isEmpty()) {
            return userRepo.findAll();
        }
        return userRepo.findByUsernameContainingIgnoreCase(query);
    }

    // בתוך UserService.java
    public void deleteUserAndData(String userId) {
        // 1. קודם כל מוחקים את כל ההזמנות ששייכות למשתמש
        orderService.deleteOrdersByUserId(userId);

        // 2. עכשיו אפשר למחוק את המשתמש עצמו בבטחה
        userRepo.deleteById(userId);
    }
}