package com.energy.management;

public class EnergyManagementSystem {
    public static void main(String[] args) {
        LogManager logManager = new LogManager();
        try {
            logManager.initializeLogs();
        } catch (CustomException e) {
            System.err.println("Failed to initialize logs: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Battery battery = new Battery(1000, logManager); // Battery capacity of 1000 units

        // Create energy sources
        EnergySource source1 = new EnergySource(battery, 50, 500);  // Charges 50 units every 500ms
        EnergySource source2 = new EnergySource(battery, 75, 700);  // Charges 75 units every 700ms

        Thread sourceThread1 = new Thread(source1, "Source-1");
        Thread sourceThread2 = new Thread(source2, "Source-2");

        // Create energy consumers
        EnergyConsumer consumer1 = new EnergyConsumer(battery, 60, 600); // Consumes 60 units every 600ms
        EnergyConsumer consumer2 = new EnergyConsumer(battery, 80, 800); // Consumes 80 units every 800ms

        Thread consumerThread1 = new Thread(consumer1, "Consumer-1");
        Thread consumerThread2 = new Thread(consumer2, "Consumer-2");

        // Create and start the battery monitor
        BatteryMonitor batteryMonitor = new BatteryMonitor(battery, 500, logManager); // Logs battery status every 500ms
        Thread monitorThread = new Thread(batteryMonitor, "Battery-Monitor");

        // Start threads
        sourceThread1.start();
        sourceThread2.start();
        consumerThread1.start();
        consumerThread2.start();
        monitorThread.start();

        try {
            Thread.sleep(10000); // Run the simulation for 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrupt threads to stop the simulation
        sourceThread1.interrupt();
        sourceThread2.interrupt();
        consumerThread1.interrupt();
        consumerThread2.interrupt();
        monitorThread.interrupt();

        // Wait for threads to finish
        try {
            sourceThread1.join();
            sourceThread2.join();
            consumerThread1.join();
            consumerThread2.join();
            monitorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation completed Successfully \n Check System_log file.");
    }
}
