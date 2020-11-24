package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class validateTimeExpectedIntervalTest {
    private PlaceRushOrderController placeRushOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeRushOrderController = new PlaceRushOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "hanoi, false",
            "-1, false",
            "25, false"
    })

    void test(String hour, boolean expected) {
        boolean isValided = placeRushOrderController.validateTimeExpectedInterval(hour);
        assertEquals(expected, isValided);
    }
}