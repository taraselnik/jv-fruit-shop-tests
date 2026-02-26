package core.basesyntax.strategy.impl;

import core.basesyntax.model.transaction.FruitTransaction;
import core.basesyntax.strategy.OperationStrategy;
import core.basesyntax.strategy.handler.OperationHandler;
import java.util.Map;

public class OperationStrategyImpl implements OperationStrategy {

    private final Map<FruitTransaction.Operation, OperationHandler> operationHandlers;

    public OperationStrategyImpl(
            Map<FruitTransaction.Operation, OperationHandler> operationHandlers) {
        if (operationHandlers == null || operationHandlers.isEmpty()) {
            throw new IllegalArgumentException("Operation handlers map can't be null or empty");
        }
        try {
            this.operationHandlers = Map.copyOf(operationHandlers);
        } catch (NullPointerException e) {
            throw new RuntimeException("Operation handlers cannot contain null keys or values", e);
        }
    }

    @Override
    public OperationHandler getOperationHandler(FruitTransaction.Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("Operation can't be null");
        }
        if (operationHandlers == null) {
            throw new RuntimeException("Operation handlers are empty");
        }
        OperationHandler handler = operationHandlers.get(operation);
        if (handler == null) {
            throw new IllegalArgumentException("No handler for operation: " + operation);
        }
        return handler;
    }
}
