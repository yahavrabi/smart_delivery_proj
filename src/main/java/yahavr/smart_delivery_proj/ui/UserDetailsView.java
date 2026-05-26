package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import yahavr.smart_delivery_proj.datamodels.User;
import yahavr.smart_delivery_proj.services.UserService;

import java.util.List;

@Route(value = "user-details", layout = MainLayout.class)
public class UserDetailsView extends VerticalLayout {

    private final UserService userService;
    private final VerticalLayout listContainer = new VerticalLayout();
    private final TextField searchField = new TextField("חיפוש לפי שם משתמש");

    public UserDetailsView(UserService userService) {
        this.userService = userService;
        setAlignItems(Alignment.CENTER);

        add(new H2("ניהול משתמשי מערכת"));

        Button searchButton = new Button("חפש", e -> refreshUserList(searchField.getValue()));
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout searchBar = new HorizontalLayout(searchField, searchButton);
        searchBar.setVerticalComponentAlignment(Alignment.END, searchButton);

        add(searchBar, listContainer);
        refreshUserList("");
    }

    private void refreshUserList(String query) {
        listContainer.removeAll();
        List<User> users = userService.searchUsers(query);

        for (User user : users) {
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

            // --- כפתור המחיקה ---
            Button deleteButton = new Button("מחק משתמש ונתונים", e -> {
                // קריאה למתודה המאוחדת שמנקה הכל
                userService.deleteUserAndData(user.getId());

                Notification.show("המשתמש וכל הזמנותיו נמחקו לצמיתות.");
                refreshUserList(searchField.getValue());
            });

            // עיצוב הכפתור באדום (Error)
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);

            HorizontalLayout cardContent = new HorizontalLayout(
                    new VerticalLayout(userName, userPass),
                    deleteButton);
            cardContent.setWidthFull();
            cardContent.setAlignItems(Alignment.CENTER);
            cardContent.setJustifyContentMode(JustifyContentMode.BETWEEN);

            card.add(cardContent);
            listContainer.add(card);
        }
    }
}