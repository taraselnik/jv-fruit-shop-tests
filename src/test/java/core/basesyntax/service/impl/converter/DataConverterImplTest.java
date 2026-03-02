package core.basesyntax.service.impl.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.model.transaction.FruitTransaction;
import core.basesyntax.service.impl.reader.FileReaderImpl;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataConverterImplTest {
    private static final Path ROOT_PATH = Path.of("src", "test", "java", "resources", "input");
    private static final Path INPUT_VALID_PATH = ROOT_PATH.resolve("reportToRead.csv");
    private static final Path INPUT_INVALID_COLUMN_AMOUNT_PATH = ROOT_PATH.resolve(
            "invalidColumnAmountReportToRead.csv");
    private static final Path INPUT_INVALID_QUANTITY_PATH = ROOT_PATH.resolve(
            "invalidQuantityReportToRead.csv");
    private static final Path INPUT_EMPTY_PATH = ROOT_PATH.resolve("emptyReportToRead.csv");
    private DataConverterImpl dataConverter;
    private FileReaderImpl fileReader;

    @BeforeEach
    void setUp() {
        dataConverter = new DataConverterImpl();
        fileReader = new FileReaderImpl();
    }

    @AfterEach
    void tearDown() {
        dataConverter = null;
        fileReader = null;
    }

    @Test
    void convertToTransaction_validFileAndDataPath_Ok() {
        List<String> inputReport = fileReader.read(INPUT_VALID_PATH.toString());
        List<FruitTransaction> result = dataConverter.convertToTransaction(inputReport);
        assertEquals(8, result.size());
    }

    @Test
    void convertToTransaction_nullDataPath_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(null));
    }

    @Test
    void convertToTransaction_invalidAmountOfColumns_notOk() {
        List<String> inputReport = fileReader.read(INPUT_INVALID_QUANTITY_PATH.toString());
        assertThrows(RuntimeException.class, () -> dataConverter.convertToTransaction(inputReport));
    }

    @Test
    void convertToTransaction_invalidQuantity_notOk() {
        List<String> inputReport = fileReader.read(INPUT_INVALID_COLUMN_AMOUNT_PATH.toString());
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(inputReport));
    }

    @Test
    void convertToTransaction_emptyFile_Ok() {
        List<String> inputReport = fileReader.read(INPUT_EMPTY_PATH.toString());
        List<FruitTransaction> result = dataConverter.convertToTransaction(inputReport);
        assertEquals(0, result.size());
    }

    @Test
    void parseOperation_UnknownCode_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.parseOperation("unknownCode"));
    }
}
