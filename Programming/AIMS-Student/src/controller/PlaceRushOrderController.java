package controller;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Logger;
import javafx.scene.control.Label;

/**
 * This class controls the flow of place rush order use case in our AIMS project
 * @author huyqq
 */
public class PlaceRushOrderController extends BaseController {

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());

    /**
     * This method checks the availability of product when user click PlaceRushOrder button
     * @throws SQLException
     */
    public void placeRushOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException {
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
     * This method split Order into normal order based on the Cart
     * @return normalOrder Order contains only media is not for rush order
     * @throws SQLException
     */
    public Order createNormalOrder() throws SQLException{
        Order normalOrder = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            if(!cartMedia.getMedia().getIsRushOrder()) {
                OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                        cartMedia.getQuantity(),
                        cartMedia.getPrice());
                normalOrder.getlstOrderMedia().add(orderMedia);
            }
        }

        return normalOrder;
    }

    /**
     * This method split Order into rush order based on the Cart
     * @return rushOrder Order contains only rush order media
     * @throws SQLException
     */
    public Order createRushOrder() throws SQLException{
        Order rushOrder = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            if(cartMedia.getMedia().getIsRushOrder()) {
                OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                        cartMedia.getQuantity(),
                        cartMedia.getPrice());
                rushOrder.getlstOrderMedia().add(orderMedia);
            }
        }

        return rushOrder;
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
     * Check address for rush order (address must in Hanoi to rush order)
     * @param province the address that user provides
     * @return result return true if province is Ha Noi, false otherwise
     */
    public boolean validateAddressForRushOrder(String province) {
        //check empty
        return(province.equals("Hà Nội"));
    }

    /**
     * Check time expected interval delivery of place rush order
     * @param hour number of hours to deliver products
     * @return true or false
     */
    public boolean validateTimeExpectedInterval(String hour) {
        try {
            int numberOfHour = Integer.parseInt(hour);
            if(numberOfHour <= 0 || numberOfHour > 24) return false;
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean validateTimeExpectedInterval(String hour, Label lb, String errorMessage) {
        boolean b = true;
        String msg = null;
        if(validateTimeExpectedInterval(hour) == false) {
            b = false;
            msg = errorMessage;
        }
        lb.setText(msg);
        return b;
    }
    /**
     * This method calculate fee for rush order
     * @param order Order of media
     * @return fees fee that user has to pay for rush order
     */
    public int calculateShippingFee(Order order) {
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        for(Object object : order.getlstOrderMedia()) {
            OrderMedia om = (OrderMedia) object;
            if(om.getMedia().getIsRushOrder())
                fees += 10000;
        }
        return fees;
    }

}
