package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo; 

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        H3 logo = new H3("Smart Delivery System");
        logo.getStyle().set("margin", "0 20px");

        // שליפת שם המשתמש מה-Session
        String username = (String) VaadinSession.getCurrent().getAttribute("username");
        if (username == null) username = "אורח";

        Span userLabel = new Span(VaadinIcon.USER.create(), new Span(username));
        userLabel.getStyle().set("font-weight", "bold");

        

        // --- כפתור Dark Mode ---
        Button themeToggle = createThemeToggle();

        // כפתור התנתקות
        Button logoutBtn = new Button("התנתק", e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().navigate("");
        });
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);

        // סידור הבר העליון - הוספתי את themeToggle לפני פרטי המשתמש
        HorizontalLayout header = new HorizontalLayout(logo, logoutBtn, themeToggle, userLabel);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo); 
        header.setWidthFull();
        header.getStyle().set("padding", "10px 20px");
        header.getStyle().set("background-color", "var(--lumo-base-color)"); // משתמש בצבע הרקע של התמה
        header.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

        addToNavbar(header);
    }

    private Button createThemeToggle() {
        // יצירת הכפתור עם אייקון ירח כברירת מחדל
        Button themeToggle = new Button(VaadinIcon.MOON.create());
        themeToggle.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        themeToggle.addClickListener(e -> {
            // גישה לרשימת התמות של ה-UI הנוכחי
            var themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                themeToggle.setIcon(VaadinIcon.MOON.create());
            } else {
                themeList.add(Lumo.DARK);
                themeToggle.setIcon(VaadinIcon.SUN_O.create());
            }
        });

        return themeToggle;
    }
}