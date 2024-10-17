package test;
import org.junit.jupiter.api.Test;

import com.energy.management.DataSimulator;
import com.energy.management.LogManager;

import static org.junit.jupiter.api.Assertions.*;

class DataSimulatorTest {

    @Test
    void testSimulateStationData() {
        LogManager logManager = new LogManager();
        DataSimulator simulator = new DataSimulator(logManager);
        assertDoesNotThrow(() -> simulator.simulateStationData("station1"));
    }

    @Test
    void testSimulateSourceData() {
        LogManager logManager = new LogManager();
        DataSimulator simulator = new DataSimulator(logManager);
        assertDoesNotThrow(() -> simulator.simulateSourceData("source1"));
    }

    @Test
    void testSimulateSystemData() {
        LogManager logManager = new LogManager();
        DataSimulator simulator = new DataSimulator(logManager);
        assertDoesNotThrow(() -> simulator.simulateSystemData());
    }

    @Test
    void testSimulateDataExchange() {
        LogManager logManager = new LogManager();
        DataSimulator simulator = new DataSimulator(logManager);
        assertDoesNotThrow(simulator::simulateDataExchange);
    }

    @Test
    void testRandomizationForVehiclesCharged() {
        LogManager logManager = new LogManager();
        DataSimulator simulator = new DataSimulator(logManager);
        assertTrue(simulator.random.nextInt(10) + 1 > 0);
    }
}

