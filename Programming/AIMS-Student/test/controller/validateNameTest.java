package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class validateNameTest {

    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() {
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "Quyen Quang Huy, true" ,
            "QuyenQuangHuy, false",
            "Quyen,Quang,Huy, false",
            "1234567890, false"
    })


    void test(String name, boolean expected){
        boolean isValided = placeOrderController.validateName(name);
        assertEquals(expected, isValided);
    }
}