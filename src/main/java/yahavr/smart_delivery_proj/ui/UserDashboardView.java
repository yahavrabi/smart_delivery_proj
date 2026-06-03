package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(value = "user-dashboard", layout = MainLayout.class)
public class UserDashboardView extends VerticalLayout {
    public UserDashboardView() {

        setAlignItems(Alignment.CENTER);
        add(new H1("אזור אישי - לקוח"));
        Button btnNewOrder = new Button("יצירת הזמנה חדשה", e -> UI.getCurrent().navigate("create-order"));
        // בתוך UserDashboardView
        Button btnMyOrders = new Button("ההזמנות שלי", e -> UI.getCurrent().navigate("my-orders"));
        btnNewOrder.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(new HorizontalLayout(btnNewOrder, btnMyOrders));
    }
}