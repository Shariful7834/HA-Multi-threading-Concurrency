package test;

import org.junit.jupiter.api.*;

import com.energy.management.CustomException;
import com.energy.management.LogManager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

class LogManagerTest {

    private LogManager logManager;
    private Path logsDir;
    private DateTimeFormatter dateFormatter;

    @BeforeEach
    void setUp() throws IOException {
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        logsDir = Paths.get("test_logs");
        Files.createDirectories(logsDir);
        logManager = new LogManager();

        // Set the logsDir to the test directory
        logManager.logsDir = logsDir;
        logManager.chargingStationsDir = logsDir.resolve("charging_stations");
        logManager.energySourcesDir = logsDir.resolve("energy_sources");
        logManager.systemLogsDir = logsDir.resolve("system_logs");
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
    void testInitializeLogs_Success() {
        assertDoesNotThrow(() -> logManager.initializeLogs());
        assertTrue(Files.exists(logManager.getLogsDir()));
    }

    @Test
    void testCreateDailyLogFiles_Success() throws CustomException {
        logManager.initializeLogs();
        String dateString = LocalDate.now().format(dateFormatter);

        Path stationLog = logManager.getStationLogFile("station1");
        Path sourceLog = logManager.getSourceLogFile("source1");
        Path systemLog = logManager.getSystemLogFile();

        assertTrue(Files.exists(stationLog));
        assertTrue(Files.exists(sourceLog));
        assertTrue(Files.exists(systemLog));
    }

    @Test
    void testWriteLog_Success() throws CustomException, IOException {
        logManager.initializeLogs();
        Path testLogFile = logManager.getSystemLogFile();
        String testMessage = "Test log message";

        assertDoesNotThrow(() -> logManager.writeLog(testLogFile, testMessage));

        // Verify the log file contains the message
        String content = Files.readString(testLogFile);
        assertTrue(content.contains(testMessage));
    }

    @Test
    void testWriteLog_AccessDeniedException() throws CustomException, IOException {
        logManager.initializeLogs();
        Path testLogFile = logManager.getSystemLogFile();

        // Set the file to read-only to induce AccessDeniedException
        testLogFile.toFile().setReadOnly();

        CustomException exception = assertThrows(CustomException.class, () -> {
            logManager.writeLog(testLogFile, "This should fail");
        });

        assertTrue(exception.getCause() instanceof AccessDeniedException);

        // Reset file permissions
        testLogFile.toFile().setWritable(true);
    }

    @Test
    void testWriteLog_NoSuchFileException() {
        Path nonExistentLogFile = logsDir.resolve("nonexistent.log");

        CustomException exception = assertThrows(CustomException.class, () -> {
            logManager.writeLog(nonExistentLogFile, "This should fail");
        });

        assertTrue(exception.getCause() instanceof NoSuchFileException);
    }
}

