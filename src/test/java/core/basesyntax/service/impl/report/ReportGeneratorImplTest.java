package core.basesyntax.service.impl.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.dao.FruitDao;
import core.basesyntax.dao.FruitDaoImpl;
import core.basesyntax.db.StorageImpl;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private ReportGeneratorImpl reportGenerator;
    private StorageImpl storage;
    private FruitDao fruitDao;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGeneratorImpl();
        storage = new StorageImpl();
        fruitDao = new FruitDaoImpl(storage);
    }

    @AfterEach
    void tearDown() {
        reportGenerator = null;
        storage = null;
        fruitDao = null;
    }

    @Test
    void getReport_nullFruitDao_notOk() {
        assertThrows(IllegalArgumentException.class, () -> reportGenerator.getReport(null));
    }

    @Test
    void getReport_emptyStorage_Ok() {
        String result = reportGenerator.getReport(fruitDao);
        assertEquals("fruit,quantity\n", result);
    }

    @Test
    void getReport_nonEmptyStorage_Ok() {
        storage.update("banana", "10");
        storage.update("apple", "20");

        String result = reportGenerator.getReport(fruitDao);

        assertTrue(result.startsWith("fruit,quantity\n"));

        List<String> lines = List.of(result.split("\\n"));
        assertEquals(3, lines.size());
        assertEquals("fruit,quantity", lines.get(0));
        assertTrue(lines.contains("banana,10"));
        assertTrue(lines.contains("apple,20"));
    }

    @Test
    void getReport_nonEmptyStorageSorted_Ok() {
        storage.update("banana", "100");
        storage.update("apple", "20");
        storage.update("orange", "15");
        storage.update("grapes", "5");
        storage.update("kiwi", "50");

        String result = reportGenerator.getReport(fruitDao);

        assertEquals("fruit,quantity\ngrapes,5\norange,15\napple,20\nkiwi,50\nbanana,100", result);
    }
}
