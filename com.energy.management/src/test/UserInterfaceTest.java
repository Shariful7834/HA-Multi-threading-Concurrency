package test;

import org.junit.jupiter.api.Test;

import com.energy.management.LogManager;
import com.energy.management.UserInterface;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;

class UserInterfaceTest {

    @Test
    void testSearchAndDisplayLogsByStationName() {
        LogManager logManager = new LogManager();
        UserInterface ui = new UserInterface(logManager);
        
        // Mock search by station name (No real files involved)
        assertDoesNotThrow(() -> ui.searchAndDisplayLogs("station1"));
    }

    @Test
    void testSearchAndDisplayLogsByDate() {
        LogManager logManager = new LogManager();
        UserInterface ui = new UserInterface(logManager);
        
        // Mock search by date (No real files involved)
        assertDoesNotThrow(() -> ui.searchAndDisplayLogs("2024-10-17"));
    }

    @Test
    void testDisplayFileContent() {
        LogManager logManager = new LogManager();
        UserInterface ui = new UserInterface(logManager);
        
        // Create a mock Path object (No real files involved)
        Path logFile = Paths.get("logs/charging_stations/station1_2024-10-17.log");
        
        // Test the displayFileContent method with a mock path
        assertDoesNotThrow(() -> ui.displayFileContent(logFile));
    }

    @Test
    void testStartInterface() {
        LogManager logManager = new LogManager();
        UserInterface ui = new UserInterface(logManager);
        
        // Simulate user input for "exit" to break the loop immediately
        ByteArrayInputStream in = new ByteArrayInputStream("exit\n".getBytes());
        System.setIn(in);
        
        // Test the start() method and ensure it doesn't throw an exception
        assertDoesNotThrow(() -> ui.start());
    }

    @Test
    void testUserExitCommand() {
        LogManager logManager = new LogManager();
        UserInterface ui = new UserInterface(logManager);
        
        // Simulate user input for "exit" and test for proper exit behavior
        ByteArrayInputStream in = new ByteArrayInputStream("exit\n".getBytes());
        System.setIn(in);
        
        assertDoesNotThrow(() -> ui.start());
    }
}


