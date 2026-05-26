package yahavr.smart_delivery_proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

import yahavr.smart_delivery_proj.datamodels.Vehicle;
import yahavr.smart_delivery_proj.repositories.VehicleRepository;
import java.util.List;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepo;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAll();
    }

    public void saveVehicle(Vehicle vehicle) {
        vehicleRepo.save(vehicle);
    }

    public void deleteVehicle(String id) {
        vehicleRepo.deleteById(id);
    }

    // חפש את המתודה הזו בתוך VehicleService ומחק את מה שיש בתוכה
    public Optional<Vehicle> existsByLicense(String licensePlate) {
        return vehicleRepo.findByLicensePlate(licensePlate);
    }


    public Vehicle findById(String id) {
        if (id == null)
            return null;
        return vehicleRepo.findById(id).orElse(null);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return vehicleRepo.findByLicensePlate(licensePlate).isPresent();
    }
}