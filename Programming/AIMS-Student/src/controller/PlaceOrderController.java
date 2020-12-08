package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order use case in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the availability of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
            order.getlstOrderMedia().add(orderMedia);
        }

        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
   * The method validates the info (still in progress)
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
    	for (String key : info.keySet()) {
            if (key.equals("name")) {
                if(validateName(info.get(key)) == false) {

                }
            }
    	    if (key.equals("phone")) {
                validatePhoneNumber(info.get(key));
            }
            if (key.equals("address")) {
                validateAddress(info.get(key));
            }
    	}
    }
    
    public boolean validatePhoneNumber(String phoneNumber) {
    	// TODO: your work
    	if(phoneNumber.length() != 10) return false;

    	if (!phoneNumber.startsWith("0")) return false;
    	try {
    		Integer.parseInt(phoneNumber);
    	} catch(NumberFormatException e) {
    		return false;
    	}
    	return true;
    }
    
    public boolean validateName(String name) {
    	// TODO: your work
        if(name.equals("null") || name.isEmpty()) return false;

        return name.matches("^[A-Za-z]+(?:\\s[a-zA-Z]+)*$");
    }
    
    public boolean validateAddress(String address) {
    	// TODO: your work
        //check empty
        if(address.equals("null") || address.isEmpty()) return false;

        return address.matches("^[A-Za-z0-9,]+(?:\\s[a-zA-Z0-9,]+)*$");
    }

    public boolean validatePhoneNumber(String phoneNumber, Label lb, String errorMessage) {
        boolean b = true;
        String msg = null;
        if(validatePhoneNumber(phoneNumber) == false) {
            b = false;
            msg = errorMessage;
        }
        lb.setText(msg);
        return b;
    }

    public boolean validateName(String name, Label lb, String errorMessage) {
        boolean b = true;
        String msg = null;
        if(validateName(name) == false) {
            b = false;
            msg = errorMessage;
        }
        lb.setText(msg);
        return b;
    }

    public boolean validateAddress(String address, Label lb, String errorMessage) {
        boolean b = true;
        String msg = null;
        if(validateAddress(address) == false) {
            b = false;
            msg = errorMessage;
        }
        lb.setText(msg);
        return b;
    }

    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
