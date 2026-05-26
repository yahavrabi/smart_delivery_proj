package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public class GuestLayout extends AppLayout {
    public GuestLayout() {
        H3 title = new H3("Smart Delivery System");
        
        // יצירת אזור המשתמש האורח: סמל וטקסט
        HorizontalLayout guestInfo = new HorizontalLayout(
            new Icon(VaadinIcon.USER), 
            new Span("אורח")
        );
        guestInfo.setAlignItems(FlexComponent.Alignment.START);
        guestInfo.getStyle().set("margin-left", "20px");

        // כפתורים
        Button btnLogin = new Button("התחברות", e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
        Button btnRegister = new Button("הרשמה", e -> getUI().ifPresent(ui -> ui.navigate(RegisterView.class)));
        btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // סידור ה-Header: בצד ימין הכותרת והאורח, בצד שמאל הכפתורים
        HorizontalLayout leftSide = new HorizontalLayout(title, guestInfo);
        leftSide.setAlignItems(FlexComponent.Alignment.CENTER);
        
        HorizontalLayout rightSide = new HorizontalLayout(btnLogin, btnRegister);
        
        HorizontalLayout header = new HorizontalLayout(leftSide, rightSide);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("padding", "0 20px");
        header.getStyle().set("background-color", "var(--lumo-base-color)");

        addToNavbar(header);
    }
}