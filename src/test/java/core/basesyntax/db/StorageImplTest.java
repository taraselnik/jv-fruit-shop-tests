package core.basesyntax.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageImplTest {

    private StorageImpl storage;

    @BeforeEach
    void setUp() {
        storage = new StorageImpl();
    }

    @AfterEach
    void tearDown() {
        storage = null;
    }

    @Test
    void update_validInput_ok() {
        storage.update("banana", "10");
        assertEquals("10", storage.get("banana"));
    }

    @Test
    void update_nullFruit_Ok() {
        storage.update(null, "10");
        assertEquals("10", storage.get(null));
    }

    @Test
    void update_nullQuantity_Ok() {
        storage.update("banana", null);
        assertNull(storage.get("banana"));
    }

    @Test
    void update_emptyQuantity_Ok() {
        storage.update("banana", "");
        assertEquals("", storage.get("banana"));
    }

    @Test
    void update_storageNotEmpty_ok() {
        storage.update("banana", "10");
        assertEquals("10", storage.get("banana"));
        storage.update("banana", "20");
        assertEquals("20", storage.get("banana"));
    }

    @Test
    void snapshot_immutable_ok() {
        storage.update("banana", "10");
        assertEquals("10", storage.get("banana"));
        assertEquals("10", storage.snapshot().get("banana"));
        Map<String, String> snapshot = storage.snapshot();
        storage.update("banana", "20");
        assertEquals("20", storage.get("banana"));
        assertEquals("10", snapshot.get("banana"));
    }

    @Test
    void snapshot_emptyStorage_ok() {
        assertTrue(storage.snapshot().isEmpty());
    }

    @Test
    void snapshot_unmodifiable_ok() {
        storage.update("banana", "10");
        Map<String, String> snapshot = storage.snapshot();
        assertThrows(UnsupportedOperationException.class, () -> snapshot.put("apple", "5"));
    }

    @Test
    void get_absentKey_ok() {
        assertNull(storage.get("banana"));
    }

    @Test
    void update_differentKeys_ok() {
        storage.update("banana", "10");
        storage.update("apple", "7");
        assertEquals("10", storage.get("banana"));
        assertEquals("7", storage.get("apple"));
    }

    @Test
    void get_nullInput_ok() {
        storage.update("banana", "10");
        assertNull(storage.get(null));
    }

    @Test
    void get_emptyInput_ok() {
        storage.update("", "10");
        assertEquals("10", storage.get(""));
    }

}
