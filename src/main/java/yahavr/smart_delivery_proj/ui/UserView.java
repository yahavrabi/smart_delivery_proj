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

@Route("/")
public class UserView extends VerticalLayout
{
    private UserService userService;
    private Button btnInsert;
    private TextField txUn;
    private TextField txPw;
    public UserView(UserService userService)
    {
        this.userService = userService;

        add(new H1("UserView"));

        HorizontalLayout layout = new HorizontalLayout(Alignment.BASELINE);
        layout.add(txUn = new TextField("username"));
        layout.add(txPw = new TextField("password"));
        layout.add(btnInsert = new Button("Insert User to DB"));
        btnInsert.addClickListener(clickEvent -> insertUserToDB());
        add(layout);
    }
    private void insertUserToDB()
    {
        //textField חילוץ הערכים מתוך 
        String un = txUn.getValue();
        String pw = txPw.getValue();

        //validation check
        if(un == null || pw ==null || un.length() < 6){
            Notification.show("user already exists!", 3000, Position.MIDDLE);
            return;
        }
        try{
            userService.insertUser(new User(un,pw));
            Notification.show("user inserted!", 3000, Position.MIDDLE);
            UI.getCurrent().navigate(UserDetailsView.class);

        }catch(Exception exp){
            exp.printStackTrace();
            // notify user by notification of this error
            Notification.show("User NOT Created! " + exp.getMessage(), 5000, Position.MIDDLE);
        }
    }
}
