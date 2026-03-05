package yahavr.smart_delivery_proj.services;

import java.util.List;

import org.springframework.stereotype.Service;

import yahavr.smart_delivery_proj.datamodels.User;
import yahavr.smart_delivery_proj.repositories.UserRepository;

@Service
public class UserService {
    private UserRepository userRepo;

    /**
     * 
     * @param userRepo (Dependancy Injection) UserRepository הזרקת תלות ב
     */
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * 
     * @param user
     * @throws Exception - אם כבר קיים משתמש זרוק שגיאה
     */
    public void insertUser(User user) throws Exception {
        //
        if (userRepo.existsById(user.getUsername())) {
            throw new Exception("user already exists");
        }
        userRepo.insert(user);
    }

    // In UserService.java
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public List<User> searchUsers(String query) {
        // If the search query is empty or null, return all users
        if (query == null || query.trim().isEmpty()) {
            return userRepo.findAll();
        }

        // Otherwise, search for users whose names match the query
        return userRepo.findByUsernameContainingIgnoreCase(query);
    }

}
