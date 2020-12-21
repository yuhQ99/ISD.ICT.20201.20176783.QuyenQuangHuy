package views.screen.invoice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import common.exception.ProcessInvoiceException;
import controller.PaymentController;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.payment.PaymentScreenHandler;
import views.screen.shipping.RushOrderScreenHandler;

public class RushOrderInvoiceHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(InvoiceScreenHandler.class.getName());

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label province;

    @FXML
    private Label address;

    @FXML
    private Label instructions;

    @FXML
    private Label time;

    @FXML
    private Label rush_order_instruction;

    @FXML
    private Label subtotal;

    @FXML
    private Label shippingFees;

    @FXML
    private Label total;

    @FXML
    private VBox vboxRushItems;

    @FXML
    private VBox vboxNormalItems;

    private Invoice invoice;

    public RushOrderInvoiceHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        this.invoice = invoice;
        setRushInvoiceInfo();
    }

    private void setRushInvoiceInfo() {
        HashMap<String, String> deliveryInfo = invoice.getOrder().getDeliveryInfo();
        name.setText(deliveryInfo.get("name"));
        phone.setText(deliveryInfo.get("phone"));
        province.setText(deliveryInfo.get("province"));
        instructions.setText(deliveryInfo.get("instructions"));
        address.setText(deliveryInfo.get("address"));
        time.setText(deliveryInfo.get("time"));
        rush_order_instruction.setText(deliveryInfo.get("rush_order_instructions"));
        subtotal.setText(Utils.getCurrencyFormat(invoice.getOrder().getAmount()));
        shippingFees.setText(Utils.getCurrencyFormat(invoice.getOrder().getShippingFees()));
        int amount = invoice.getOrder().getAmount() + invoice.getOrder().getShippingFees();
        total.setText(Utils.getCurrencyFormat(amount));
        invoice.setAmount(amount);
        invoice.getOrder().getlstOrderMedia().forEach(orderMedia -> {
            try {
                MediaInvoiceScreenHandler mis = new MediaInvoiceScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                mis.setOrderMedia((OrderMedia) orderMedia);
                OrderMedia om = (OrderMedia) orderMedia;
                if(om.getMedia().getIsRushOrder()) {
                    vboxRushItems.getChildren().add(mis.getContent());
                }
                else {
                    vboxNormalItems.getChildren().add(mis.getContent());
                }

            } catch (IOException | SQLException e) {
                System.err.println("errors: " + e.getMessage());
                throw new ProcessInvoiceException(e.getMessage());
            }
        });
    }

    @FXML
    void confirmInvoice(MouseEvent event) throws IOException {
        BaseScreenHandler paymentScreen = new PaymentScreenHandler(this.stage, Configs.PAYMENT_SCREEN_PATH, invoice);
        paymentScreen.setBController(new PaymentController());
        paymentScreen.setPreviousScreen(this);
        paymentScreen.setHomeScreenHandler(homeScreenHandler);
        paymentScreen.setScreenTitle("Payment Screen");
        paymentScreen.show();
        LOGGER.info("Confirmed invoice");
    }
}
