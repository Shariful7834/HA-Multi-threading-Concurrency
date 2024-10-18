package test;

import org.junit.jupiter.api.*;

import com.energy.management.CustomException;
import com.energy.management.LogManager;
import com.energy.management.UserInterface;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;

class UserInterfaceTest {

    private UserInterface userInterface;
    private LogManager logManager;
    private Path testLogsDir;

    @BeforeEach
    void setUp() throws Exception {
        // Set up a test logs directory
        testLogsDir = Paths.get("test_logs");
        Files.createDirectories(testLogsDir);

        // Initialize LogManager with test directories
        logManager = new LogManager();
        logManager.logsDir = testLogsDir;
        logManager.chargingStationsDir = testLogsDir.resolve("charging_stations");
        logManager.energySourcesDir = testLogsDir.resolve("energy_sources");
        logManager.systemLogsDir = testLogsDir.resolve("system_logs");

        // Initialize logs and create sample log files
        logManager.initializeLogs();
        userInterface = new UserInterface(logManager);
        createSampleLogFiles();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the test logs directory after each test
        if (Files.exists(testLogsDir)) {
            Files.walk(testLogsDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }

    // Helper method to create sample log files for testing
    private void createSampleLogFiles() throws IOException {
        Path stationLog = logManager.getStationLogFile("station1");
        Files.writeString(stationLog,
            "[2023-10-17 12:00:00] Vehicles Charged: 5, Total Energy Consumed: 100.0 kWh\n");

        Path sourceLog = logManager.getSourceLogFile("source1");
        Files.writeString(sourceLog,
            "[2023-10-17 12:00:00] Status: Online, Energy Produced: 150.0 kWh\n");

        Path systemLog = logManager.getSystemLogFile();
        Files.writeString(systemLog,
            "[2023-10-17 12:00:00] Total Energy Consumed: 500.0 kWh, Total Energy Produced: 600.0 kWh\n");
    }

    @Test
    void testSearchAndDisplayLogs_ByDate() {
        assertDoesNotThrow(() -> userInterface.searchAndDisplayLogs("2023-10-17"));
    }

    @Test
    void testSearchAndDisplayLogs_ByEquipmentName() {
        assertDoesNotThrow(() -> userInterface.searchAndDisplayLogs("station1"));
    }

    @Test
    void testSearchAndDisplayLogs_NoMatchingFiles() {
        // Redirect System.out to capture the output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            userInterface.searchAndDisplayLogs("nonexistent");
            String output = outContent.toString();
            assertTrue(output.contains("No log files found matching your input."));
        } catch (CustomException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            // Reset System.out to its original state
            System.setOut(originalOut);
        }
    }

    @Test
    void testDisplayFileContent_Success() {
        Path stationLog = logManager.getStationLogFile("station1");
        assertDoesNotThrow(() -> userInterface.displayFileContent(stationLog));
    }
    @Test
    void testModifyPermissionsAndReset() throws IOException {
        Path stationLog = logManager.getStationLogFile("station1");

        // Attempt to set file to read-only
        boolean permissionChanged = stationLog.toFile().setWritable(false);
        assumeTrue(permissionChanged, "Could not change file permissions; skipping test.");

        try {
            // Attempt to write to the log file
            logManager.writeLog(stationLog, "Test message");
            fail("Expected CustomException due to access denied.");
        } catch (CustomException e) {
            assertTrue(e.getCause() instanceof AccessDeniedException);
        } finally {
            // Reset permissions
            stationLog.toFile().setWritable(true);
        }
    }

    // Additional test methods can be added here
}



