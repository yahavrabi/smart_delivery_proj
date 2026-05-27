package yahavr.smart_delivery_proj.ui;

import com.google.maps.model.LatLng;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Value;
import yahavr.smart_delivery_proj.datamodels.Vehicle;
import yahavr.smart_delivery_proj.datamodels.Warehouse;
import yahavr.smart_delivery_proj.services.MapsService;
import yahavr.smart_delivery_proj.services.VehicleService;
import yahavr.smart_delivery_proj.services.WarehouseService;


@Route(value = "admin-management", layout = MainLayout.class)
public class AdminManagementView extends VerticalLayout {

    private final WarehouseService warehouseService;
    private final VehicleService vehicleService;
    private final MapsService mapsService;

    private GoogleMapView map;
    private Grid<Vehicle> vehicleGrid = new Grid<>(Vehicle.class);

    public AdminManagementView(WarehouseService warehouseService,
            VehicleService vehicleService,
            MapsService mapsService,
            @Value("${google.maps.api.key}") String apiKey) {
        this.warehouseService = warehouseService;
        this.vehicleService = vehicleService;
        this.mapsService = mapsService;

        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        add(new H2("ניהול תשתית לוגיסטית - Smart Delivery"));

        // --- חלק א': הגדרת מחסן (Depot) ---
        add(new H3("הגדרת מחסן מרכזי"));

        TextField txWarehouseAddress = new TextField("כתובת מלאה");
        txWarehouseAddress.setWidth("350px");

        map = new GoogleMapView(apiKey);

        Button btnPreview = new Button("בדיקת מיקום במפה", e -> {
            String address = txWarehouseAddress.getValue();
            if (address.isEmpty()) {
                Notification.show("נא להזין כתובת לבדיקה");
                return;
            }

            LatLng loc = mapsService.getLatLngFromAddress(address);
            if (loc != null) {
                map.setCenter(loc.lat, loc.lng);
                map.addMarker(loc.lat, loc.lng, "מיקום שנבדק");
                Notification.show("המיקום נמצא במפה!");
            } else {
                Notification.show("הכתובת לא נמצאה", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        // --- כפתור השמירה (נשאר כמעט זהה) ---
        Button btnSaveWarehouse = new Button("שמור/עדכן מחסן", e -> {
            String address = txWarehouseAddress.getValue();
            if (address.isEmpty()) {
                Notification.show("נא להזין כתובת לשמירה");
                return;
            }

            LatLng loc = mapsService.getLatLngFromAddress(address);
            if (loc != null) {
                Warehouse existing = warehouseService.findByName("main-warehouse");
                if (existing != null) {
                    existing.setFullAddress(address);
                    existing.setLat(loc.lat);
                    existing.setLng(loc.lng);
                    warehouseService.saveWarehouse(existing);
                    Notification.show("המחסן עודכן בהצלחה");
                } else {
                    Warehouse newW = new Warehouse("main-warehouse", address, loc.lat, loc.lng);
                    warehouseService.saveWarehouse(newW);
                    Notification.show("מחסן חדש הוגדר בהצלחה");
                }
            } else {
                Notification.show("לא ניתן לשמור - הכתובת לא תקינה", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        btnSaveWarehouse.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout warehouseInput = new HorizontalLayout(txWarehouseAddress,btnPreview, btnSaveWarehouse);
        // יישור הכפתור לתחתית השורה (מול התיבות)
        warehouseInput.setVerticalComponentAlignment(Alignment.END, btnSaveWarehouse);
        warehouseInput.setVerticalComponentAlignment(Alignment.END, btnPreview);

        add(warehouseInput, map);

        // --- חלק ב': ניהול צי רכב ---
        add(new H3("ניהול צי רכב"));

        TextField txPlate = new TextField("מספר רישוי");
        NumberField numCapacity = new NumberField("קיבולת (קילו)");

        Button btnAddVehicle = new Button("הוסף רכב", e -> {
            String plate = txPlate.getValue();
            Double capacity = numCapacity.getValue();

            if (plate == null || plate.isEmpty() || capacity == null) {
                Notification.show("נא למלא את כל שדות הרכב");
                return;
            }

            // בדיקת כפילות לוחית רישוי
            if (vehicleService.existsByLicensePlate(plate)) {
                Notification.show("שגיאה: רכב עם מספר רישוי " + plate + " כבר קיים!",
                        5000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            Vehicle v = new Vehicle(plate, capacity, "MAIN_WAREHOUSE");
            vehicleService.saveVehicle(v);
            updateVehicleGrid();
            txPlate.clear();
            numCapacity.clear();
            Notification.show("הרכב נוסף בהצלחה");
        });
        btnAddVehicle.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout vehicleInput = new HorizontalLayout(txPlate, numCapacity, btnAddVehicle);
        vehicleInput.setVerticalComponentAlignment(Alignment.END, btnAddVehicle);

        // הגדרת הטבלה
        vehicleGrid.setColumns("licensePlate", "maxCapacity");

        vehicleGrid.addComponentColumn(vehicle -> {
            Button delBtn = new Button("מחק", e -> {
                vehicleService.deleteVehicle(vehicle.getId());
                updateVehicleGrid();
            });
            delBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            return delBtn;
        }).setHeader("פעולות");

        vehicleGrid.setWidth("850px");
        vehicleGrid.setHeight("300px");

        add(vehicleInput, vehicleGrid);
        updateVehicleGrid();
    }

    private void updateVehicleGrid() {
        vehicleGrid.setItems(vehicleService.getAllVehicles());
    }
}