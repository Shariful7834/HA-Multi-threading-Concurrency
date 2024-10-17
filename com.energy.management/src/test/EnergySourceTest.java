package test;
import org.junit.jupiter.api.Test;

import com.energy.management.EnergySource;

import static org.junit.jupiter.api.Assertions.*;

class EnergySourceTest {

    @Test
    void testEnergySourceName() {
        EnergySource source = new EnergySource("source1");
        assertEquals("source1", source.getName());
    }

    @Test
    void testEnergySourceNotNull() {
        EnergySource source = new EnergySource("source1");
        assertNotNull(source.getName());
    }

    @Test
    void testEnergySourceEquality() {
        EnergySource source1 = new EnergySource("source1");
        EnergySource source2 = new EnergySource("source1");
        assertEquals(source1.getName(), source2.getName());
    }

    @Test
    void testEnergySourceNewInstance() {
        // Instead of reassigning, create a new instance
        EnergySource newSource = new EnergySource("source2");
        assertEquals("source2", newSource.getName());
    }

    @Test
    void testEnergySourceNameLength() {
        EnergySource source = new EnergySource("source1");
        assertTrue(source.getName().length() > 0);
    }
}

