package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import yahavr.smart_delivery_proj.datamodels.Order;
import yahavr.smart_delivery_proj.services.OrderService;

import java.util.List;

@Route(value = "my-orders", layout = MainLayout.class)
public class MyOrdersView extends VerticalLayout {

    private final OrderService orderService;
    private Grid<Order> grid = new Grid<>(Order.class, false);

    public MyOrdersView(OrderService orderService) {
        this.orderService = orderService;

        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        add(new H2("היסטוריית המשלוחים שלי"));

        // הגדרת עמודות הטבלה
        grid.addColumn(Order::getDescription).setHeader("תיאור החבילה").setSortable(true);
        grid.addColumn(Order::getShippingAddress).setHeader("כתובת יעד").setSortable(true);
        
        // עמודת סטטוס עם "תגית" (Badge) צבעונית
        grid.addComponentColumn(order -> {
            Span statusBadge = new Span(order.getStatus());
            String status = order.getStatus();
            
            // עיצוב לפי סטטוס
            statusBadge.getElement().getThemeList().add("badge");
            if ("PENDING".equals(status)) {
                statusBadge.getStyle().set("background-color", "#f3f4f6").set("color", "#4b5563");
            } else if ("SHIPPED".equals(status)) {
                statusBadge.getElement().getThemeList().add("success");
            } else if ("DELIVERED".equals(status)) {
                statusBadge.getElement().getThemeList().add("contrast");
            }
            return statusBadge;
        }).setHeader("סטטוס");

        grid.setWidth("95%");
        grid.setHeight("450px");

        // --- השיוך למשתמש המחובר ---
        String currentUserId = (String) VaadinSession.getCurrent().getAttribute("userId");

        if (currentUserId != null) {
            updateGrid(currentUserId);
        } else {
            Notification.show("נא להתחבר כדי לצפות בהזמנות");
            UI.getCurrent().navigate(LoginView.class);
        }

        // כפתורי ניווט
        Button btnNewOrder = new Button("ליצירת הזמנה חדשה +", e -> UI.getCurrent().navigate("create-order"));
        Button btnBack = new Button("חזור ללוח הבקרה", e -> UI.getCurrent().navigate("user-dashboard"));
        
        add(grid, btnNewOrder, btnBack);
    }

    private void updateGrid(String userId) {
        List<Order> userOrders = orderService.getOrdersByUserId(userId);
        grid.setItems(userOrders);
    }
}