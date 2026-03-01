package core.basesyntax.strategy.handler.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.strategy.handler.OperationHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReturnOperationTest {

    private OperationHandler returnOperationHandler;

    @BeforeEach
    void setUp() {
        returnOperationHandler = new ReturnOperation();
    }

    @AfterEach
    void tearDown() {
        returnOperationHandler = null;
    }

    @Test
    void apply_validInputs_ok() {
        BigDecimal initialQuantity = new BigDecimal("15");
        BigDecimal quantity = new BigDecimal("5");
        String expected = "20";
        String actual = returnOperationHandler.apply(initialQuantity, quantity);
        assertEquals(expected, actual);
    }

    @Test
    void apply_nullInitialQuantity_notOk() {
        BigDecimal quantity = new BigDecimal("5");
        assertThrows(IllegalArgumentException.class,
                () -> returnOperationHandler.apply(null, quantity));
    }

    @Test
    void apply_nullQuantity_notOk() {
        BigDecimal initialQuantity = new BigDecimal("10");
        assertThrows(IllegalArgumentException.class,
                () -> returnOperationHandler.apply(initialQuantity, null));
    }

    @Test
    void apply_negativeQuantity_notOk() {
        BigDecimal initialQuantity = new BigDecimal("10");
        BigDecimal quantity = new BigDecimal("-5");
        assertThrows(IllegalArgumentException.class,
                () -> returnOperationHandler.apply(initialQuantity, quantity));
    }

    @Test
    void apply_quantityBiggerThanInitialQuantity_Ok() {
        BigDecimal initialQuantity = new BigDecimal("5");
        BigDecimal quantity = new BigDecimal("10");
        String expected = "15";
        String actual = returnOperationHandler.apply(initialQuantity, quantity);
        assertEquals(expected, actual);
    }
}
