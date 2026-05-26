package yahavr.smart_delivery_proj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahavr.smart_delivery_proj.datamodels.Order;
import yahavr.smart_delivery_proj.repositories.OrderRepository;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    public void saveOrder(Order order) {
        // כאן אפשר להוסיף לוגיקה של סטטוס ראשוני
        if (order.getStatus() == null) {
            order.setStatus("PENDING");
        }
        orderRepo.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepo.findByUserId(userId);
    }

    public void deleteOrdersByUserId(String userId) {
        orderRepo.deleteByUserId(userId);
    }

    public void setOrdersStatus(List<String> orderIds, String status) {
        for (String id : orderIds) {
            Order o = orderRepo.findById(id).orElseThrow();
            o.setStatus(status);
            orderRepo.save(o);
        }
    }
}