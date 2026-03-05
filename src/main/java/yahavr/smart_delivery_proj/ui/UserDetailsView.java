package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import yahavr.smart_delivery_proj.datamodels.User;
import yahavr.smart_delivery_proj.services.UserService;

import java.util.List;

@Route("user-details")
public class UserDetailsView extends VerticalLayout {

    private final UserService userService;
    private final VerticalLayout listContainer = new VerticalLayout(); // Holds the user cards
    private final TextField searchField = new TextField("Search by username");

    public UserDetailsView(UserService userService) {
        this.userService = userService;

        add(new H2("System Users"));

        // Search Section
        HorizontalLayout searchBar = new HorizontalLayout();
        Button searchButton = new Button("Search");
        
        searchBar.add(searchField, searchButton);
        add(searchBar);
        add(listContainer); // The container where users will appear

        // Event listener for search
        searchButton.addClickListener(e -> refreshUserList(searchField.getValue()));

        // Initial load
        refreshUserList("");
    }

    private void refreshUserList(String query) {
        listContainer.removeAll(); // Clear existing cards
        
        List<User> users = userService.searchUsers(query);
        
        for (User user : users) {
            VerticalLayout userCard = new VerticalLayout();
            userCard.add(new Span("Username: " + user.getUsername()));
            userCard.add(new Span("Password: " + user.getPassword()));
            userCard.getStyle().set("border", "1px solid #e2e2e2");
            userCard.getStyle().set("padding", "10px");
            
            listContainer.add(userCard);
        }
    }
}