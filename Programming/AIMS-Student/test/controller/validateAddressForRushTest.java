package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class validateAddressForRushTest {
    private PlaceRushOrderController placeRushOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeRushOrderController = new PlaceRushOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "Ha Noi, true",
            "hanoi, true",
            "so 49, Hoang hoa tham Street, Ha nOI, true",
            "hoang hoa tham, false",
            "Bui Vien, Ho Chi Minh City, false",
            "   Ha   Noi   , true"
    })

    void test(String address, boolean expected) {
        boolean isValided = placeRushOrderController.validateAddressForRushOrder(address);
        assertEquals(expected, isValided);
    }
}