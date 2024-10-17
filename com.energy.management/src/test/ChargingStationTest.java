package test;
import com.energy.management.ChargingStation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ChargingStationTest {

    @Test
    void testChargingStationName() {
        ChargingStation station = new ChargingStation("station1");
        assertEquals("station1", station.getName());
    }

    @Test
    void testChargingStationNotNull() {
        ChargingStation station = new ChargingStation("station1");
        assertNotNull(station.getName());
    }

    @Test
    void testChargingStationEquality() {
        ChargingStation station1 = new ChargingStation("station1");
        ChargingStation station2 = new ChargingStation("station1");
        assertEquals(station1.getName(), station2.getName());
    }

    @Test
    void testChargingStationNewInstance() {
        // Instead of reassigning, create a new instance
        ChargingStation newStation = new ChargingStation("station2");
        assertEquals("station2", newStation.getName());
    }

    @Test
    void testChargingStationNameLength() {
        ChargingStation station = new ChargingStation("station1");
        assertTrue(station.getName().length() > 0);
    }
}
