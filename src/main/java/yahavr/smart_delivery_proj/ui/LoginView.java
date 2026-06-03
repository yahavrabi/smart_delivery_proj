package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import yahavr.smart_delivery_proj.datamodels.User;
import yahavr.smart_delivery_proj.services.AdminService;
import yahavr.smart_delivery_proj.services.UserService;

@Route("login")
public class LoginView extends VerticalLayout {
    
    private final AdminService adminService;
    private final UserService userService;
    
    private final TextField txUn = new TextField("Username");
    private final PasswordField txPw = new PasswordField("Password");

    public LoginView(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
        
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHeightFull();

        Button btnLogin = new Button("Login", e -> {
            try {
                login();
            } catch (Exception e1) {
                Notification.show("שם המשתמש או הסיסמא שגויים");
            }
        });
        btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        add(new H1("Login"), txUn, txPw, btnLogin);
    }

    private void login() throws Exception {
        String un = txUn.getValue().trim();
        String pw = txPw.getValue().trim();

        // 1. ניסיון כניסה כאדמין
        if (adminService.authenticate(un, pw)) {
            VaadinSession.getCurrent().setAttribute("username", un);
            VaadinSession.getCurrent().setAttribute("role", "ADMIN");
            
            Notification.show("שלום המנהל! כניסה לממשק ניהול", 3000, Position.TOP_CENTER);
            UI.getCurrent().navigate(AdminDashboardView.class);
            return;
        }

        // 2. ניסיון כניסה כמשתמש רגיל
        User user = userService.authenticate(un, pw);
        if (user != null) {
            VaadinSession.getCurrent().setAttribute("userId", user.getId());
            VaadinSession.getCurrent().setAttribute("username", user.getUsername());
            VaadinSession.getCurrent().setAttribute("role", "USER");

            Notification.show("התחברת בהצלחה", 3000, Position.TOP_CENTER);
            UI.getCurrent().navigate(UserDashboardView.class);
        } else {
            Notification.show("שם משתמש או סיסמה שגויים", 3000, Position.MIDDLE);
        }
    }
}