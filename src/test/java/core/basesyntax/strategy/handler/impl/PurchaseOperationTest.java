package core.basesyntax.strategy.handler.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.strategy.handler.OperationHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseOperationTest {

    private OperationHandler purchaseOperationHandler;

    @BeforeEach
    void setUp() {
        purchaseOperationHandler = new PurchaseOperation();
    }

    @AfterEach
    void tearDown() {
        purchaseOperationHandler = null;
    }

    @Test
    void apply_validInputs_ok() {
        BigDecimal initialQuantity = new BigDecimal("15");
        BigDecimal quantity = new BigDecimal("5");
        String expected = "10";
        String actual = purchaseOperationHandler.apply(initialQuantity, quantity);
        assertEquals(expected, actual);
    }
  
    @Test
    void apply_nullInitialQuantity_notOk() {
        BigDecimal quantity = new BigDecimal("5");
        assertThrows(IllegalArgumentException.class,
                () -> purchaseOperationHandler.apply(null, quantity));
    }
  
    @Test
    void apply_nullQuantity_notOk() {
        BigDecimal initialQuantity = new BigDecimal("10");
        assertThrows(IllegalArgumentException.class,
                () -> purchaseOperationHandler.apply(initialQuantity, null));
    }
  
    @Test
    void apply_negativeQuantity_notOk() {
        BigDecimal initialQuantity = new BigDecimal("10");
        BigDecimal quantity = new BigDecimal("-5");
        assertThrows(IllegalArgumentException.class,
                () -> purchaseOperationHandler.apply(initialQuantity, quantity));
    }

    @Test
    void apply_quantityBiggerThanInitialQuantity_notOk() {
        BigDecimal initialQuantity = new BigDecimal("5");
        BigDecimal quantity = new BigDecimal("10");
        assertThrows(IllegalArgumentException.class,
                () -> purchaseOperationHandler.apply(initialQuantity, quantity));
    }
}
