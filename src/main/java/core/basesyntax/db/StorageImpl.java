package core.basesyntax.db;

import java.util.HashMap;
import java.util.Map;

public class StorageImpl implements Storage {
    private final Map<String, String> storage = new HashMap<>();

    @Override
    public void update(String fruit, String quantity) {
        storage.put(fruit, quantity);
    }

    @Override
    public Map<String, String> snapshot() {
        return Map.copyOf(storage);
    }

    @Override
    public String get(String key) {
        return storage.get(key);
    }
}
