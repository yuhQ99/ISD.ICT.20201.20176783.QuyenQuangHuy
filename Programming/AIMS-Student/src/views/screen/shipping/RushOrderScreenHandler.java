package views.screen.shipping;

import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.RushOrderInvoiceHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RushOrderScreenHandler extends BaseScreenHandler implements Initializable {

    @FXML
    private TextField time;

    @FXML
    private TextField rush_order_instructions;

    @FXML
    private Label error_time;

    private Order order;

    public RushOrderScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
        super(stage, screenPath);
        this.order = order;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        time.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }

    @FXML
    void submitRushOrderForm(MouseEvent event) throws IOException, InterruptedException, SQLException {
        boolean validateTime = getBController().validateTimeExpectedInterval(time.getText(), error_time, "Time to deliver products is not identified" );

        // create invoice screen
        order.getDeliveryInfo().put("time", time.getText());
        order.getDeliveryInfo().put("rush_order_instructions", rush_order_instructions.getText());

        if(validateTime) {
            Invoice invoice = getBController().createInvoice(order);

            BaseScreenHandler RushOrderInvoiceHandler = new RushOrderInvoiceHandler(this.stage, Configs.INVOICE_RUSH_ORDER_PATH, invoice);
            RushOrderInvoiceHandler.setPreviousScreen(this);
            RushOrderInvoiceHandler.setHomeScreenHandler(homeScreenHandler);
            RushOrderInvoiceHandler.setScreenTitle("Invoice Screen");
            //InvoiceScreenHandler.setBController(new PlaceRushOrderController());
            RushOrderInvoiceHandler.show();
        }
    }

    public PlaceRushOrderController getBController(){
        return (PlaceRushOrderController) super.getBController();
    }

}
