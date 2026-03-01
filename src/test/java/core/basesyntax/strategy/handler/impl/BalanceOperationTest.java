package core.basesyntax.strategy.handler.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.strategy.handler.OperationHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BalanceOperationTest {

    private OperationHandler balanceOperationHandler;

    @BeforeEach
    void setUp() {
        balanceOperationHandler = new BalanceOperation();
    }

    @AfterEach
    void tearDown() {
        balanceOperationHandler = null;
    }

    @Test
    void apply_validInputs_ok() {
        BigDecimal initialQuantity = new BigDecimal("10");
        BigDecimal quantity = new BigDecimal("5");
        String expected = "5";
        String actual = balanceOperationHandler.apply(initialQuantity, quantity);
        assertEquals(expected, actual);
    }

    @Test
    void apply_nullInitialQuantity_notOk() {
        BigDecimal quantity = new BigDecimal("5");
        assertThrows(IllegalArgumentException.class,
                () -> balanceOperationHandler.apply(null, quantity));
    }

    @Test
    void apply_nullQuantity_notOk() {
        BigDecimal initialQuantity = new BigDecimal("10");
        assertThrows(IllegalArgumentException.class,
                () -> balanceOperationHandler.apply(initialQuantity, null));
    }

    @Test
    void apply_negativeQuantity_notOk() {
        BigDecimal initialQuantity = new BigDecimal("10");
        BigDecimal quantity = new BigDecimal("-5");
        assertThrows(IllegalArgumentException.class,
                () -> balanceOperationHandler.apply(initialQuantity, quantity));
    }
}
