package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import yahavr.smart_delivery_proj.datamodels.User;
import yahavr.smart_delivery_proj.services.UserService;

@Route("/register")
public class RegisterView extends VerticalLayout {
    private UserService userService;
    private Button btnInsert;
    private TextField txUn;
    private TextField txPw;

    public RegisterView(UserService userService) {
        this.userService = userService;

        add(new H1("RegisterView"));

        HorizontalLayout layout = new HorizontalLayout(Alignment.BASELINE);
        layout.add(txUn = new TextField("username"));
        layout.add(txPw = new TextField("password"));
        layout.add(btnInsert = new Button("הירשם"));
        btnInsert.addClickListener(clickEvent -> insertUserToDB());
        add(layout);
    }

    private void insertUserToDB() {
        // textField חילוץ הערכים מתוך
        String un = txUn.getValue();
        String pw = txPw.getValue();

        // validation check
        if (un.isEmpty() || pw.isEmpty()) {
            Notification.show("שם המשתמש או הסיסמא שגויים", 3000, Position.MIDDLE);
            return;
        }

        if (un.length() < 3) {
            Notification.show("שם המשתמש חייב להיות לפחות 3 תווים", 3000, Position.MIDDLE);
            return;
        }
        try {
            User user = new User(un,pw);
            userService.insertUser(user);
            Notification.show("המשתמש נוצר בהצלחה!", 3000, Position.MIDDLE);
            UI.getCurrent().navigate(LoginView.class);

        } catch (Exception exp) {
            // notify user by notification of this error
            Notification.show("שגיאה! " + exp.getMessage(), 5000, Position.MIDDLE);
        }
    }
}
