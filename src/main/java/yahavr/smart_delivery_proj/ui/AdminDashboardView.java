package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "admin-dashboard", layout = MainLayout.class)
public class AdminDashboardView extends VerticalLayout {

    public AdminDashboardView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHeightFull();

        add(new H1("Smart Delivery - ממשק מנהל"));
        add(new H2("מרכז שליטה ובקרה לוגיסטי"));

        // 1. ניהול תשתית
        Button btnInfrastructure = new Button("ניהול מחסן וצי רכב",
                e -> UI.getCurrent().navigate(AdminManagementView.class));
        btnInfrastructure.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_LARGE);
        btnInfrastructure.setHeight("100px");
        btnInfrastructure.setWidth("250px");

        // 2. הרצת אלגוריתם
        Button btnRunAlgorithm = new Button("הרצת אלגוריתם ניתוב",
                e -> UI.getCurrent().navigate("algorithm-runner"));
        btnRunAlgorithm.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_LARGE);
        btnRunAlgorithm.setHeight("100px");
        btnRunAlgorithm.setWidth("250px");

        // 3. מסלולים שמורים - התוספת החדשה
        Button btnSavedRoutes = new Button("היסטוריית מסלולים",
                e -> UI.getCurrent().navigate(SavedRoutesView.class));
        btnSavedRoutes.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        btnSavedRoutes.setHeight("100px");
        btnSavedRoutes.setWidth("250px");

        // סידור הכפתורים המרכזיים בשורה
        HorizontalLayout mainActions = new HorizontalLayout(btnInfrastructure, btnRunAlgorithm, btnSavedRoutes);
        mainActions.setSpacing(true);

        // כפתורים משניים
        Button btnUsers = new Button("ניהול משתמשי מערכת 👥", e -> UI.getCurrent().navigate(UserDetailsView.class));
        btnUsers.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        add(mainActions, btnUsers);
    }
}