package yahavr.smart_delivery_proj.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import yahavr.smart_delivery_proj.services.OrderService;
import yahavr.smart_delivery_proj.services.RouteHistoryService;
import yahavr.smart_delivery_proj.services.VehicleService;

@com.vaadin.flow.router.Route(value = "saved-routes", layout = MainLayout.class)
public class SavedRoutesView extends VerticalLayout {

    private final RouteHistoryService historyService;
    private final VehicleService vehicleService; // וודא שזה מוגדר כאן
    private final OrderService orderService; // וודא שזה מוגדר כאן
    private Grid<yahavr.smart_delivery_proj.datamodels.Route> grid;

    public SavedRoutesView(RouteHistoryService historyService, VehicleService vehicleService,
            OrderService orderService) {
        this.historyService = historyService;
        this.orderService = orderService;
        this.vehicleService = vehicleService;
        setSizeFull();

        add(new H2("היסטוריית מסלולים שמורים"));

        // הגדרת הגריד
        grid = new Grid<>(yahavr.smart_delivery_proj.datamodels.Route.class, false);
        refreshGrid();

        // הגדרת עמודות עם פורמט קריא
        grid.addColumn(r -> r.getVehicleId()).setHeader("רכב");
        grid.addColumn(r -> String.format("%.2f ק\"מ", r.getTotalDistance())).setHeader("מרחק");

        grid.addColumn(r -> r.getOrderIds() != null ? r.getOrderIds().size() : 0).setHeader("כמות הזמנות");

        // הוספת עמודת פעולות
        grid.addComponentColumn(route -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button deleteBtn = new Button("מחק", e -> {
                historyService.deleteRoute(route.getId());
                refreshGrid(); // רענון הגריד לאחר מחיקה
                Notification.show("המסלול נמחק מהמערכת");
            });
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);

            actions.add(deleteBtn);
            return actions;
        }).setHeader("פעולות");

        grid.setWidthFull();
        add(grid);
    }

    private void refreshGrid() {
        grid.setItems(historyService.getAllRoutes());
    }
}