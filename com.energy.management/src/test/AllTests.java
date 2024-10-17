package test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    LogManagerTest.class, 
    DataSimulatorTest.class, 
    UserInterfaceTest.class, 
//    EnergySourceTest.class, 
//    ChargingStationTest.class
})
public class AllTests {
    // No need to define anything inside, JUnit 5 will handle running all selected test classes.
}
