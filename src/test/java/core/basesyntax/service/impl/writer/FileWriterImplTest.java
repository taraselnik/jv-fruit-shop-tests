package core.basesyntax.service.impl.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.dao.FruitDao;
import core.basesyntax.dao.FruitDaoImpl;
import core.basesyntax.db.StorageImpl;
import core.basesyntax.service.impl.report.ReportGeneratorImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileWriterImplTest {
    private static final Path ROOT_PATH = Path.of("src", "test", "java", "resources", "output");
    private static final Path REPORT_PATH = ROOT_PATH.resolve("finalReport.csv");

    private FileWriterImpl fileWriter;
    private ReportGeneratorImpl reportGenerator;
    private StorageImpl storage;
    private FruitDao fruitDao;

    @BeforeEach
    void setUp() {
        fileWriter = new FileWriterImpl();
        reportGenerator = new ReportGeneratorImpl();
        storage = new StorageImpl();
        fruitDao = new FruitDaoImpl(storage);
        try {
            Files.deleteIfExists(REPORT_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Can't delete file: " + REPORT_PATH, e);
        }
    }

    @AfterEach
    void tearDown() {
        fileWriter = null;
        reportGenerator = null;
        storage = null;
        fruitDao = null;
    }

    @Test
    void write_nullData_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write(null, "filePath"));
    }

    @Test
    void write_nullFilePath_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write("data", null));
    }

    @Test
    void write_emptyFilePath_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write("data", ""));
    }

    @Test
    void write_validDataFileExists_Ok() {
        fileWriter.write("data", REPORT_PATH.toString());
        assertTrue(Files.exists(Path.of(REPORT_PATH.toString())));
    }

    @Test
    void write_checkValidDataRecorded_Ok() {
        storage.update("banana", "10");
        storage.update("apple", "20");

        String result = reportGenerator.getReport(fruitDao);
        fileWriter.write(result, REPORT_PATH.toString());
        assertTrue(Files.exists(Path.of(REPORT_PATH.toString())));
        try {
            assertEquals("fruit,quantity\nbanana,10\napple,20", Files.readString(REPORT_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Can't read file: " + REPORT_PATH, e);
        }
    }
}
