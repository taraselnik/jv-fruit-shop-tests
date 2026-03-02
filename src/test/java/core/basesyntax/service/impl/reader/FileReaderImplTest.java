package core.basesyntax.service.impl.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileReaderImplTest {
    private static final Path ROOT_PATH = Path.of("src", "test", "java", "resources", "input");
    private static final Path INPUT_VALID_PATH = ROOT_PATH.resolve("reportToRead.csv");
    private static final Path INPUT_EMPTY_PATH = ROOT_PATH.resolve("emptyReportToRead.csv");
    private FileReaderImpl fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderImpl();
    }

    @AfterEach
    void tearDown() {
        fileReader = null;
    }

    @Test
    void read_validFileAndDataPath_Ok() {
        List<String> result = fileReader.read(INPUT_VALID_PATH.toString());
        assertEquals(9, result.size());
    }

    @Test
    void read_nullDataPath_notOk() {
        assertThrows(IllegalArgumentException.class, () -> fileReader.read(null));
    }

    @Test
    void read_nonExistingFile_notOk() {
        assertThrows(RuntimeException.class, () -> fileReader.read("nonExistingFile.csv"));
    }

    @Test
    void read_emptyFile_Ok() {
        List<String> result = fileReader.read(INPUT_EMPTY_PATH.toString());
        assertEquals(0, result.size());
    }
}
