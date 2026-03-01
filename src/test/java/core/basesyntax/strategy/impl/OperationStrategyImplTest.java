package core.basesyntax.strategy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.model.transaction.FruitTransaction;
import core.basesyntax.strategy.handler.OperationHandler;
import core.basesyntax.strategy.handler.impl.BalanceOperation;
import core.basesyntax.strategy.handler.impl.PurchaseOperation;
import core.basesyntax.strategy.handler.impl.SupplyOperation;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationStrategyImplTest {

    private OperationStrategyImpl operationStrategyImpl;
    private Map<FruitTransaction.Operation, OperationHandler> operationHandlers;

    @BeforeEach
    void setUp() {
        operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());

        operationStrategyImpl = new OperationStrategyImpl(operationHandlers);
    }

    @AfterEach
    void tearDown() {
        operationStrategyImpl = null;
    }

    @Test
    void constructor_nullOperationHandlers_notOk() {
        assertThrows(IllegalArgumentException.class, () -> new OperationStrategyImpl(null));
    }

    @Test
    void constructor_emptyOperationHandlers_notOk() {
        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = Map.of();
        assertThrows(IllegalArgumentException.class,
                () -> new OperationStrategyImpl(operationHandlers));
    }

    @Test
    void getOperationHandler_validOperationHandler_Ok() {
        OperationHandler operationBalanceHandler =
                operationStrategyImpl.getOperationHandler(FruitTransaction.Operation.BALANCE);
        assertEquals(BalanceOperation.class, operationBalanceHandler.getClass());
    }

    @Test
    void getOperationHandler_handlerNotFound_notOk() {
        Map<FruitTransaction.Operation, OperationHandler> operationHandlers =
                Map.of(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        OperationStrategyImpl operationStrategyImpl = new OperationStrategyImpl(operationHandlers);
        assertThrows(IllegalArgumentException.class,
                () -> operationStrategyImpl.getOperationHandler(
                        FruitTransaction.Operation.PURCHASE));
    }

    @Test
    void getOperationHandler_immutable_Ok() {
        OperationHandler operationBalanceHandlerInitial =
                operationStrategyImpl.getOperationHandler(FruitTransaction.Operation.BALANCE);
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new SupplyOperation());
        OperationHandler operationBalanceHandlerFinal =
                operationStrategyImpl.getOperationHandler(FruitTransaction.Operation.BALANCE);
        assertEquals(BalanceOperation.class, operationBalanceHandlerInitial.getClass());
        assertEquals(BalanceOperation.class, operationBalanceHandlerFinal.getClass());
    }

    @Test
    void getOperationHandler_emptyOperation_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> operationStrategyImpl.getOperationHandler(null));
    }
}
