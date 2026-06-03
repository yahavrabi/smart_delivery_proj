package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(value = "", layout = GuestLayout.class) // קישור ל-Layout החדש
public class HomeView extends VerticalLayout {
    public HomeView() {

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHeightFull();

        add(new H1("ברוכים הבאים למערכת מתכנן משלוחים ומסלולים"));
        add(new H3("פלטפורמה חכמה לניהול ואופטימיזציה של מערכי משלוחים, המיועדת לעסקים המפעילים ציי רכב להפצת סחורה"));
        Image remoteImage = new Image("https://img.freepik.com/free-photo/red-delivery-car-deliver-express-shipping-fast-delivery-background-3d-rendering-illustration_56104-1910.jpg", "תיאור התמונה");
        remoteImage.setWidth("300px");
        // add(new H2("online: " + onlineUsers));
        add(remoteImage);
        
        add(new Span("יהב רבינוביץ 328456348 מכללת כנפי רוח 2026"));
    }
}