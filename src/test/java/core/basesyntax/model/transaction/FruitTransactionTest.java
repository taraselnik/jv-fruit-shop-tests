package core.basesyntax.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FruitTransactionTest {

    private FruitTransaction fruitTransaction;

    @BeforeEach
    void setUp() {
        String fruit = "apple";
        int quantity = 10;
        FruitTransaction.Operation operationType = FruitTransaction.Operation.SUPPLY;
        fruitTransaction = new FruitTransaction(operationType, fruit, quantity);
    }

    @AfterEach
    void tearDown() {
        fruitTransaction = null;
    }

    @Test
    void constructor_WithValidData_Ok() {
        assertEquals(FruitTransaction.Operation.SUPPLY, fruitTransaction.getOperation());
        assertEquals("apple", fruitTransaction.getFruit());
        assertEquals(10, fruitTransaction.getQuantity());
    }

    @Test
    void constructor_WithNullFruit_ShouldThrowException_notOk() {
        assertThrows(IllegalArgumentException.class, () ->
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, null, 10));
    }

    @Test
    void constructor_WithNullOperation_ShouldThrowException_notOk() {
        assertThrows(IllegalArgumentException.class, () ->
                new FruitTransaction(null, "apple", 10));
    }

    @Test
    void constructor_WithNegativeQuantity_ShouldThrowException_notOk() {
        assertThrows(IllegalArgumentException.class, () ->
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, "apple", -10));
    }

    @Test
    void constructor_equalitySameTransaction_Ok() {
        FruitTransaction fruitTransaction2 = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, "apple", 10);
        assertEquals(fruitTransaction, fruitTransaction2);
    }

    @Test
    void hashCode_equalityHashCode_Ok() {
        FruitTransaction fruitTransaction2 = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, "apple", 10);
        assertEquals(fruitTransaction.hashCode(), fruitTransaction2.hashCode());
    }

    @Test
    void constructor_equalityAgainstOtherObject_Ok() {
        assertNotEquals(new Object(), fruitTransaction);
    }

    @SuppressWarnings({"SimplifiableAssertion", "ConstantValue"})
    @Test
    void equals_equalityAgainstNull_Ok() {
        assertFalse(fruitTransaction.equals(null));
    }

    @Test
    void toString_equalityToString_Ok() {
        String expected = String.format(
                "FruitTransaction{operation=%s, fruit='%s', quantity=%d}",
                FruitTransaction.Operation.SUPPLY,
                "apple",
                10
        );
        assertEquals(expected, fruitTransaction.toString());
    }

    @Test
    void getCode_validOperationEnumCode_Ok() {
        assertEquals("s", fruitTransaction.getOperation().getCode());
    }
}
