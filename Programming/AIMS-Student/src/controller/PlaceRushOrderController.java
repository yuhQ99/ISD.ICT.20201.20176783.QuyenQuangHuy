package controller;

//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Random;
//import java.util.logging.Logger;
//
//import entity.cart.Cart;
//import entity.cart.CartMedia;
//import common.exception.InvalidDeliveryInfoException;
//import entity.invoice.Invoice;
//import entity.order.Order;
//import entity.order.OrderMedia;
//import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place rush order use case in our AIMS project
 * @author huyqq
 */
public class PlaceRushOrderController extends BaseController {

    /**
     * Check address for rush order (address must in Hanoi to rush order)
     * @param address the address that user provides
     * @return result
     */
    public boolean validateAddressForRushOrder(String address) {
        //check empty
        String str1 = "ha noi";
        String str2 = "hanoi";
        if(address.equals("null") || address.isEmpty()) return false;

        if(!address.matches("^[A-Za-z0-9]+(?:\\s[a-zA-Z0-9]+)*$")) return false;

        if (address.toUpperCase().contains((str1.toUpperCase()))) return true;

        return address.toUpperCase().contains((str2.toUpperCase()));
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

}
