package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import yahavr.smart_delivery_proj.datamodels.Order;
import yahavr.smart_delivery_proj.datamodels.User;
import yahavr.smart_delivery_proj.services.OrderService;
import yahavr.smart_delivery_proj.services.UserService;

import java.util.List;

@Route(value = "mission2")
public class MissionView extends VerticalLayout {

    private final UserService userService;
    private final OrderService orderService;
    private final VerticalLayout listContainer = new VerticalLayout();

    public MissionView(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
        setAlignItems(Alignment.CENTER);

        add(listContainer);
        refreshUserList("");
    }

    private void refreshUserList(String query) {
        listContainer.removeAll();
        List<User> users = userService.searchUsers(query);

        for (User user : users) {
            List<Order> orders = orderService.getOrdersByUserId(user.getId());
            VerticalLayout card = new VerticalLayout();
            card.getStyle().set("border", "1px solid #ddd")
                    .set("border-radius", "8px")
                    .set("padding", "15px")
                    .set("margin", "5px");
            card.setWidth("400px");

            Span userName = new Span("משתמש: " + user.getUsername());
            userName.getStyle().set("font-weight", "bold");

            Span userPass = new Span("סיסמה: " + user.getPassword());
            userPass.getStyle().set("font-size", "0.9em").set("color", "gray");

            HorizontalLayout cardContent = new HorizontalLayout(
                    new VerticalLayout(userName, userPass));

            for(Order order : orders){
                Span userOrder = new Span(order.getDescription());
                userOrder.getStyle().set("font-weight", "bold");
                cardContent.add((userOrder));
                cardContent.setWidthFull();
            }

            cardContent.setWidthFull();
            cardContent.setAlignItems(Alignment.CENTER);
            cardContent.setJustifyContentMode(JustifyContentMode.BETWEEN);

            card.add(cardContent);
            listContainer.add(card);
        }
    }
}