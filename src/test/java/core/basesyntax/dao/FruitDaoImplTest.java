package core.basesyntax.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.StorageImpl;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FruitDaoImplTest {

    private StorageImpl storage;
    private FruitDaoImpl fruitDao;

    @BeforeEach
    void setUp() {
        storage = new StorageImpl();
        storage.update("banana", "10");
        storage.update("apple", "20");
        fruitDao = new FruitDaoImpl(storage);
    }

    @AfterEach
    void tearDown() {
        storage = null;
        fruitDao = null;
    }

    @Test
    void update_validInput_ok() {
        fruitDao.update("banana", "10");
        assertEquals("10", fruitDao.getQuantity("banana"));
    }

    @Test
    void update_nullFruit_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fruitDao.update(null, "10"));
    }

    @Test
    void update_nullQuantity_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fruitDao.update("banana", null));
    }

    @Test
    void fruitDaoImpl_nullStorage_notOk() {
        assertThrows(IllegalArgumentException.class, () -> new FruitDaoImpl(null));
    }

    @Test
    void getAll_storageNotEmpty_ok() {
        assertEquals(storage.snapshot(), fruitDao.getAll());
    }

    @Test
    void getAll_emptyStorage_ok() {
        FruitDaoImpl fruitDaoEmpty = new FruitDaoImpl(new StorageImpl());
        assertEquals(Map.of(), fruitDaoEmpty.getAll());
    }

    @Test
    void getQuantity_existingKey_ok() {
        assertEquals("10", fruitDao.getQuantity("banana"));
    }

    @Test
    void getQuantity_missingKey_ok() {
        assertNull(fruitDao.getQuantity("orange"));
    }

    @Test
    void getQuantity_nullKey_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fruitDao.getQuantity(null));
    }

    @Test
    void getAll_modifyReturnedMap_notOk() {
        Map<String, String> all = fruitDao.getAll();
        assertThrows(UnsupportedOperationException.class, () -> all.put("orange", "1"));
    }

    @Test
    void getAll_afterUpdate_previousSnapshotUnchanged_ok() {
        Map<String, String> allBeforeUpdate = fruitDao.getAll();
        fruitDao.update("banana", "999");
        assertEquals("10", allBeforeUpdate.get("banana"));
    }
}
