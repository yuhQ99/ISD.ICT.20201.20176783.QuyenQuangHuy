package views.screen.shipping;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import controller.PlaceOrderController;
import common.exception.InvalidDeliveryInfoException;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.popup.PopupScreen;

public class ShippingScreenHandler extends BaseScreenHandler implements Initializable {

	@FXML
	private Label screenTitle;

	@FXML
	private TextField name;

	@FXML
	private TextField phone;

	@FXML
	private TextField address;

	@FXML
	private TextField instructions;

	@FXML
	private ComboBox<String> province;

	@FXML
	private Label error_name;

	@FXML
	private Label error_phone;

	@FXML
	private Label error_address;

	private Order order;

	private boolean rushOrderState;

	public ShippingScreenHandler(Stage stage, String screenPath, Order order, boolean rushOrderState) throws IOException {
		super(stage, screenPath);
		this.order = order;
		this.rushOrderState = rushOrderState;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
		name.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
		this.province.getItems().addAll(Configs.PROVINCES);
	}

	@FXML
	void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

		boolean validatePhone = getBController().validatePhoneNumber(phone.getText(), error_phone, "Phone number is not identified");
		boolean validateName = getBController().validateName(name.getText(), error_name, "Name is not identified");
		boolean validateAddress = getBController().validateAddress(address.getText(), error_address, "Address is not identified");

		// add info to messages
		HashMap messages = new HashMap<>();
		messages.put("name", name.getText());
		messages.put("phone", phone.getText());
		messages.put("address", address.getText());
		messages.put("instructions", instructions.getText());
		messages.put("province", province.getValue());

		try {
			// process and validate delivery info
			getBController().processDeliveryInfo(messages);


		} catch (InvalidDeliveryInfoException e) {
			throw new InvalidDeliveryInfoException(e.getMessage());
		}



		if(validatePhone && validateName && validateAddress && (!rushOrderState)) {
			int shippingFees;
			// calculate shipping fees
			shippingFees = getBController().calculateShippingFee(order);
			order.setShippingFees(shippingFees);
			order.setDeliveryInfo(messages);

			// create invoice screen
			Invoice invoice = getBController().createInvoice(order);
			BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
			InvoiceScreenHandler.setPreviousScreen(this);
			InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
			InvoiceScreenHandler.setScreenTitle("Invoice Screen");
			//InvoiceScreenHandler.setBController(getBController());
			InvoiceScreenHandler.show();
		}
		else if(validatePhone && validateName && validateAddress && rushOrderState) {
			PlaceRushOrderController placeRushOrderController = new PlaceRushOrderController();

			if(!placeRushOrderController.validateAddressForRushOrder(province.getValue())) {
				PopupScreen.errorAutoClose("Your address cannot execute Place Rush Order");
				PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
				delay.setOnFinished(e -> {
					try {
						int shippingFees = getBController().calculateShippingFee(order);
						order.setShippingFees(shippingFees);
						order.setDeliveryInfo(messages);

						// create invoice screen
						Invoice invoice = getBController().createInvoice(order);
						BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
						InvoiceScreenHandler.setPreviousScreen(this);
						InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
						InvoiceScreenHandler.setScreenTitle("Invoice Screen");
						//InvoiceScreenHandler.setBController(getBController());
						InvoiceScreenHandler.show();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				delay.play();

				return;
			}

			int shippingFees = placeRushOrderController.calculateShippingFee(order);
			order.setShippingFees(shippingFees);
			order.setDeliveryInfo(messages);

			RushOrderScreenHandler RushOrderScreenHandler = new RushOrderScreenHandler(this.stage, Configs.RUSH_ORDER_SCREEN_PATH, order);
			RushOrderScreenHandler.setPreviousScreen(this);
			RushOrderScreenHandler.setHomeScreenHandler(homeScreenHandler);
			RushOrderScreenHandler.setScreenTitle("Rush Order Screen");
			RushOrderScreenHandler.setBController(placeRushOrderController);
			RushOrderScreenHandler.show();
		}
	}

	public PlaceOrderController getBController(){
		return (PlaceOrderController) super.getBController();
	}

	public void notifyError(){
		// TODO: implement later on if we need
	}

}
