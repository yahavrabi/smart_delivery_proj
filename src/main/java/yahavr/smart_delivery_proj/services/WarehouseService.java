package yahavr.smart_delivery_proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahavr.smart_delivery_proj.datamodels.Warehouse;
import yahavr.smart_delivery_proj.repositories.WarehouseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepo;

    /**
     * מחזיר את כל המחסנים הקיימים בבסיס הנתונים.
     */
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepo.findAll();
    }

    /**
     * שומר או מעדכן מחסן.
     */
    public Warehouse saveWarehouse(Warehouse warehouse) {
        return warehouseRepo.save(warehouse);
    }

    /**
     * מוצא מחסן לפי ה-ID שלו.
     */
    public Optional<Warehouse> getWarehouseById(String id) {
        return warehouseRepo.findById(id);
    }

    /**
     * מוחק מחסן לפי ID.
     */
    public void deleteWarehouse(String id) {
        warehouseRepo.deleteById(id);
    }

    /**
     * פונקציית עזר לשליפת המחסן הראשון (שימושי אם יש רק מחסן מרכזי אחד כרגע).
     */
    public Warehouse getMainWarehouse() {
        List<Warehouse> warehouses = warehouseRepo.findAll();
        return warehouses.isEmpty() ? null : warehouses.get(0);
    }

    /**
     * מחפשת מחסן על פי שם המחסן
     * @param name
     * @return
     */
    public Warehouse findByName(String name) {
        // השם של המתודה ב-Repository חייב להיות findByName
        return warehouseRepo.findByName(name);
    }

    public Optional<Warehouse> getWarehouseDetails() {
        List<Warehouse> warehouses = warehouseRepo.findAll();
        return warehouses.stream().findFirst();
    }
}