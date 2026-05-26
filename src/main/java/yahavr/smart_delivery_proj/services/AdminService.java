package yahavr.smart_delivery_proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahavr.smart_delivery_proj.repositories.AdminRepository;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepo;

    public boolean authenticate(String username, String password) {
        return adminRepo.findByUsername(username)
                .map(admin -> admin.getPassword().equals(password))
                .orElse(false);
    }
}