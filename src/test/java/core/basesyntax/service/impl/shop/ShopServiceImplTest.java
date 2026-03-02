package core.basesyntax.service.impl.shop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.dao.FruitDao;
import core.basesyntax.dao.FruitDaoImpl;
import core.basesyntax.db.StorageImpl;
import core.basesyntax.model.transaction.FruitTransaction;
import core.basesyntax.strategy.handler.OperationHandler;
import core.basesyntax.strategy.handler.impl.BalanceOperation;
import core.basesyntax.strategy.handler.impl.PurchaseOperation;
import core.basesyntax.strategy.handler.impl.ReturnOperation;
import core.basesyntax.strategy.handler.impl.SupplyOperation;
import core.basesyntax.strategy.impl.OperationStrategyImpl;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopServiceImplTest {
    private ShopServiceImpl shopService;
    private OperationStrategyImpl operationStrategy;
    private List<FruitTransaction> fruitTransactions;
    private FruitDao fruitDao;

    @BeforeEach
    void setUp() {
        HashMap<FruitTransaction.Operation, OperationHandler> operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        operationHandlers.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy);
        StorageImpl storage = new StorageImpl();
        storage.update("banana", "10");
        storage.update("apple", "20");
        fruitDao = new FruitDaoImpl(storage);
        fruitTransactions = List.of(
            new FruitTransaction(FruitTransaction.Operation.BALANCE,"banana", 10),
            new FruitTransaction(FruitTransaction.Operation.PURCHASE,"banana", 5),
            new FruitTransaction(FruitTransaction.Operation.SUPPLY,"banana", 15),
            new FruitTransaction(FruitTransaction.Operation.RETURN,"banana", 1),
            new FruitTransaction(FruitTransaction.Operation.PURCHASE,"apple", 5),
            new FruitTransaction(FruitTransaction.Operation.SUPPLY,"apple", 15),
            new FruitTransaction(FruitTransaction.Operation.RETURN,"apple", 1),
            new FruitTransaction(FruitTransaction.Operation.BALANCE,"apple", 10)
        );
    }

    @AfterEach
    void tearDown() {
        shopService = null;
        operationStrategy = null;
        fruitTransactions = null;
        fruitDao = null;
    }

    @Test
    void constructor_validOperationStrategy_Ok() {
        assertDoesNotThrow(() -> new ShopServiceImpl(operationStrategy));
    }

    @Test
    void constructor_nullOperationStrategy_notOk() {
        assertThrows(IllegalArgumentException.class, () -> new ShopServiceImpl(null));
    }

    @Test
    void process_validTransactions_Ok() {
        assertDoesNotThrow(() -> shopService.process(fruitTransactions, fruitDao));
    }

    @Test
    void process_nullTransactions_notOk() {
        assertThrows(IllegalArgumentException.class, () -> shopService.process(null, fruitDao));
    }

    @Test
    void process_nullFruitDao_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> shopService.process(fruitTransactions, null));
    }

    @Test
    void process_notEnoughStoredQuantity_wrapsExceptionWithDetails_notOk() {
        List<FruitTransaction> invalidTransactions = List.of(
            new FruitTransaction(FruitTransaction.Operation.BALANCE, "banana", 10),
            new FruitTransaction(FruitTransaction.Operation.PURCHASE, "banana", 11)
        );

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> shopService.process(invalidTransactions, fruitDao));

        assertTrue(exception.getMessage()
                .contains("Failed to process transaction: operation=PURCHASE"));
        assertTrue(exception.getMessage().contains(", fruit=banana"));
        assertTrue(exception.getMessage().contains(", quantity=11"));
        assertTrue(exception.getMessage().contains(", handler=PurchaseOperation"));
        assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
    }

    @Test
    void process_fruitNotInStorage_usesZeroStoredQuantityAndWrapsException_notOk() {
        List<FruitTransaction> invalidTransactions = List.of(
            new FruitTransaction(FruitTransaction.Operation.PURCHASE, "kiwi", 1)
        );

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> shopService.process(invalidTransactions, fruitDao));

        assertTrue(exception.getMessage()
                .contains("Failed to process transaction: operation=PURCHASE"));
        assertTrue(exception.getMessage().contains(", fruit=kiwi"));
        assertTrue(exception.getMessage().contains(", quantity=1"));
        assertTrue(exception.getMessage().contains(", storedQuantity=0"));
        assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
    }

    @Test
    void process_invalidStoredQuantityFormat_throwsRuntimeException_notOk() {
        StorageImpl storage = new StorageImpl();
        storage.update("banana", "notANumber");
        FruitDao invalidFruitDao = new FruitDaoImpl(storage);
        List<FruitTransaction> transactions = List.of(
            new FruitTransaction(FruitTransaction.Operation.BALANCE, "banana", 10)
        );

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> shopService.process(transactions, invalidFruitDao));

        assertTrue(exception.getMessage().contains("Invalid quantity: notANumber"));
        assertEquals(NumberFormatException.class, exception.getCause().getClass());
    }
}
