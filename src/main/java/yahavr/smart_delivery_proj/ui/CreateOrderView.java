package yahavr.smart_delivery_proj.ui;

import com.google.maps.model.LatLng;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import org.springframework.beans.factory.annotation.Value;
import yahavr.smart_delivery_proj.datamodels.Order;
import yahavr.smart_delivery_proj.services.MapsService;
import yahavr.smart_delivery_proj.services.OrderService;
import yahavr.smart_delivery_proj.components.GoogleMapComponent;

@Route(value = "create-order", layout = MainLayout.class)
public class CreateOrderView extends VerticalLayout {

    private final MapsService mapsService;
    private final OrderService orderService;
    private GoogleMapComponent map;

    // משתני עזר לשמירת המיקום שנמצא ב-Geocoding
    private double currentLat;
    private double currentLng;
    private boolean isLocationVerified = false;

    public CreateOrderView(MapsService mapsService, OrderService orderService,
            @Value("${google.maps.api.key}") String apiKey) {
        this.mapsService = mapsService;
        this.orderService = orderService;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        add(new H2("יצירת משלוח חדש"));

        // שדות טופס
        TextField txDescription = new TextField("מה שולחים?");
        txDescription.setPlaceholder("למשל: חבילת ספרים, כלי עבודה...");
        txDescription.setWidth("400px");

        TextField txAddress = new TextField("כתובת למשלוח");
        txAddress.setPlaceholder("עיר, רחוב ומספר בית");
        txAddress.setWidth("400px");

        // רכיב המפה שבנינו
        map = new GoogleMapComponent(apiKey);

        // כפתור לבדיקת הכתובת במפה (Geocoding)
        Button btnCheckAddress = new Button("בדוק מיקום במפה", e -> {
            String address = txAddress.getValue();
            if (address.isEmpty()) {
                Notification.show("נא להזין כתובת לפני הבדיקה");
                return;
            }

            LatLng loc = mapsService.getLatLngFromAddress(address);
            if (loc != null) {
                currentLat = loc.lat;
                currentLng = loc.lng;
                isLocationVerified = true;

                map.setCenter(currentLat, currentLng);
                map.addMarker(currentLat, currentLng, "מיקום המשלוח המבוקש");
                Notification.show("הכתובת אותרה בהצלחה");
            } else {
                isLocationVerified = false;
                Notification.show("שגיאה: לא מצאנו את הכתובת הזו. נסה לדייק.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        // כפתור השמירה הסופי
        Button btnSave = new Button("אשר והזמן משלוח", e -> {
            if (!isLocationVerified) {
                Notification.show("חובה לבדוק את הכתובת במפה לפני האישור", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_WARNING);
                return;
            }

            if(txDescription.isEmpty()){
                Notification.show("נא לציין את תכולת המשלוח", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_WARNING);
                return;
            }

            // שליפת ה-ID של המשתמש מה-Session (חשוב מאוד לשיוך ההזמנה!)
            String currentUserId = (String) VaadinSession.getCurrent().getAttribute("userId");

            if (currentUserId == null) {
                Notification.show("שגיאת אבטחה: המשתמש לא מחובר", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                UI.getCurrent().navigate(LoginView.class);
                return;
            }

            // יצירת אובייקט ההזמנה (שימוש בבנאי שמעדכן סטטוס ל-PENDING)
            Order newOrder = new Order(
                    txDescription.getValue(),
                    txAddress.getValue(),
                    currentLat,
                    currentLng,
                    currentUserId);

            orderService.saveOrder(newOrder);

            Notification.show("ההזמנה נרשמה במערכת וממתינה לשיבוץ!", 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // חזרה ללוח הבקרה של המשתמש
            UI.getCurrent().navigate("user-dashboard");
        });

        Button btnBack = new Button("חזור ללוח הבקרה", e -> UI.getCurrent().navigate("user-dashboard"));

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);

        // סידור האלמנטים בשורה
        HorizontalLayout addressLayout = new HorizontalLayout(txAddress, btnCheckAddress);
        addressLayout.setVerticalComponentAlignment(Alignment.END, btnCheckAddress);

        add(txDescription, addressLayout, map, btnSave, btnBack);
    }
}