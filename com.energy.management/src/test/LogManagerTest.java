package test;
import org.junit.jupiter.api.Test;

import com.energy.management.LogManager;

import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class LogManagerTest {

    @Test
    void testCreateDailyLogFiles() {
        LogManager logManager = new LogManager();
        assertDoesNotThrow(() -> logManager.createDailyLogFiles());
    }

    @Test
    void testGetStationLogFile() {
        LogManager logManager = new LogManager();
        Path logFile = logManager.getStationLogFile("station1");
        assertNotNull(logFile);
        assertTrue(logFile.toString().contains("station1"));
    }

    @Test
    void testWriteLog() {
        LogManager logManager = new LogManager();
        Path logFile = logManager.getStationLogFile("station1");
        assertDoesNotThrow(() -> logManager.writeLog(logFile, "Test log message"));
    }

    @Test
    void testGetSourceLogFile() {
        LogManager logManager = new LogManager();
        Path logFile = logManager.getSourceLogFile("source1");
        assertNotNull(logFile);
        assertTrue(logFile.toString().contains("source1"));
    }

    @Test
    void testGetSystemLogFile() {
        LogManager logManager = new LogManager();
        Path logFile = logManager.getSystemLogFile();
        assertNotNull(logFile);
        assertTrue(logFile.toString().contains("system"));
    }
}

