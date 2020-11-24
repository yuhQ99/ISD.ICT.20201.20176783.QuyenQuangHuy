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
import views.screen.popup.PopupScreen;

public class PlaceRushOrderController extends BaseController {

    public boolean validateAddressForRushOrder(String address) {
        //check empty
        String str1 = "ha noi";
        String str2 = "hanoi";
        if(address.equals("null") || address.isEmpty()) return false;

        if(!address.matches("^[A-Za-z0-9]+(?:\\s[a-zA-Z0-9]+)*$")) return false;
      //  if(!address.matches("^[A-Za-z0-9]+$")) return false;

        if (address.toUpperCase().contains((str1.toUpperCase())) || address.toUpperCase().contains((str2.toUpperCase()))) return true;

        return false;
    }

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
