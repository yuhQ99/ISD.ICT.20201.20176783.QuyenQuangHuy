package controller;

//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Random;
//import java.util.logging.Logger;


//import common.exception.InvalidDeliveryInfoException;
//import entity.invoice.Invoice;

//import views.screen.popup.PopupScreen;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.order.Order;
import entity.order.OrderMedia;

import java.sql.SQLException;
import java.util.Random;

/**
 * This class controls the flow of place rush order use case in our AIMS project
 * @author huyqq
 */
public class PlaceRushOrderController extends PlaceOrderController {

//    /**
//     * This method checks the availability of product when user click PlaceRushOrder button
//     * @throws SQLException
//     */
//    public void placeRushOrder() throws SQLException {
//        Cart.getCart().checkAvailabilityOfProduct();
//    }
//
//    /**
//     * This method creates the new Order based on the Cart
//     * @return Order
//     * @throws SQLException
//     */
//    public Order createOrder() throws SQLException {
//        Order order = new Order();
//        for (Object object : Cart.getCart().getListMedia()) {
//            CartMedia cartMedia = (CartMedia) object;
//            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
//                    cartMedia.getQuantity(),
//                    cartMedia.getPrice());
//            order.getlstOrderMedia().add(orderMedia);
//        }
//
//        return order;
//    }

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
     * Check address for rush order (address must in Hanoi to rush order)
     * @param address the address that user provides
     * @return result
     */
    public boolean validateAddressForRushOrder(String address) {
        //check empty
        String str1 = "ha noi";
        String str2 = "hanoi";
        String tmp = address.trim().replaceAll(" +", " ");
        if(tmp.equals("null") || address.isEmpty()) return false;

        if(!tmp.matches("^[A-Za-z0-9]+(?:\\s[a-zA-Z0-9]+)*$")) return false;

        if (tmp.toUpperCase().contains((str1.toUpperCase()))) return true;

        return tmp.toUpperCase().contains((str2.toUpperCase()));
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

    /**
     * This method calculate fee for rush order
     * @param order Order of media
     * @return fees fee that user has to pay for rush order
     */
    @Override
    public int calculateShippingFee(Order order) {
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        for(Object object : order.getlstOrderMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            if(cartMedia.getMedia().getIsRushOrder())
                fees += 10000;
        }
        return fees;
    }

}
