package com.energy.management;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Random;

public class DataSimulator {

    private LogManager logManager;
    public Random random;

    public DataSimulator(LogManager logManager) {
        this.logManager = logManager;
        this.random = new Random();
    }

    public void simulateDataExchange() {
        simulateStationData("station1");
        simulateSourceData("source1");
        simulateSystemData();
    }

    public void simulateStationData(String stationName) {
        Path stationLog = logManager.getStationLogFile(stationName);

        // Simulate data generation
        int vehiclesCharged = random.nextInt(10) + 1; // 1 to 10 vehicles
        double energyConsumed = vehiclesCharged * (random.nextDouble() * 20 + 10); // 10 to 30 kWh per vehicle
        double averagePower = energyConsumed / (vehiclesCharged * (random.nextDouble() * 0.5 + 0.5)); // Average power per vehicle

        String logMessage = String.format(
            "Vehicles Charged: %d, Total Energy Consumed: %.2f kWh, Average Power: %.2f kW",
            vehiclesCharged, energyConsumed, averagePower
        );

        try {
            logManager.writeLog(stationLog, logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simulateSourceData(String sourceName) {
        Path sourceLog = logManager.getSourceLogFile(sourceName);

        // Simulate data generation
        double energyProduced = random.nextDouble() * 100 + 50; // 50 to 150 kWh
        String status = random.nextBoolean() ? "Online" : "Offline";
        double efficiency = random.nextDouble() * 20 + 80; // 80% to 100%

        String logMessage = String.format(
            "Status: %s, Energy Produced: %.2f kWh, Efficiency: %.2f%%",
            status, energyProduced, efficiency
        );

        try {
            logManager.writeLog(sourceLog, logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simulateSystemData() {
        Path systemLog = logManager.getSystemLogFile();

        // Simulate data aggregation
        double totalEnergyConsumed = random.nextDouble() * 1000 + 500; // 500 to 1500 kWh
        double totalEnergyProduced = random.nextDouble() * 1000 + 500; // 500 to 1500 kWh
        String peakDemandTime = LocalDateTime.now().minusHours(random.nextInt(24)).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logMessage = String.format(
            "Total Energy Consumed: %.2f kWh, Total Energy Produced: %.2f kWh, Peak Demand Time: %s",
            totalEnergyConsumed, totalEnergyProduced, peakDemandTime
        );

        try {
            logManager.writeLog(systemLog, logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
