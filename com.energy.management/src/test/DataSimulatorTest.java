package test;

import org.junit.jupiter.api.*;

import com.energy.management.CustomException;
import com.energy.management.DataSimulator;
import com.energy.management.LogManager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

class DataSimulatorTest {

    private LogManager logManager;
    private DataSimulator dataSimulator;
    private Path logsDir;
    private DateTimeFormatter dateFormatter;

    @BeforeEach
    void setUp() throws CustomException, IOException {
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        logsDir = Paths.get("test_logs");
        Files.createDirectories(logsDir);
        logManager = new LogManager();

        // Set the logsDir to the test directory
        logManager.logsDir = logsDir;
        logManager.chargingStationsDir = logsDir.resolve("charging_stations");
        logManager.energySourcesDir = logsDir.resolve("energy_sources");
        logManager.systemLogsDir = logsDir.resolve("system_logs");

        logManager.initializeLogs();
        dataSimulator = new DataSimulator(logManager);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete test logs directory
        if (Files.exists(logsDir)) {
            Files.walk(logsDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
    }

    @Test
    void testSimulateDataExchange_Success() {
        assertDoesNotThrow(() -> dataSimulator.simulateDataExchange());
    }

    @Test
    void testSimulateStationData_Success() {
        assertDoesNotThrow(() -> dataSimulator.simulateStationData("station1"));
    }

    @Test
    void testSimulateSourceData_Success() {
        assertDoesNotThrow(() -> dataSimulator.simulateSourceData("source1"));
    }

    @Test
    void testSimulateSystemData_Success() {
        assertDoesNotThrow(() -> dataSimulator.simulateSystemData());
    }

    @Test
    void testSimulateStationData_AccessDeniedException() throws IOException {
        Path stationLog = logManager.getStationLogFile("station1");
        // Set the file to read-only to induce AccessDeniedException
        stationLog.toFile().setReadOnly();

        CustomException exception = assertThrows(CustomException.class, () -> {
            dataSimulator.simulateStationData("station1");
        });

        assertTrue(exception.getCause().getCause() instanceof AccessDeniedException);

        // Reset file permissions
        stationLog.toFile().setWritable(true);
    }
}

