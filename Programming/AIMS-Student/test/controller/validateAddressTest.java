package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class validateAddressTest {

    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "Ha Noi, true" ,
            "123456, false",
            "49, ngo Kham Thien, Dong Da, Ha Noi, true",
            "Kham THien, true"
    })

    void test(String address, boolean expected) {
        boolean isValided = placeOrderController.validateAddress(address);
        assertEquals(expected, isValided);
    }
}