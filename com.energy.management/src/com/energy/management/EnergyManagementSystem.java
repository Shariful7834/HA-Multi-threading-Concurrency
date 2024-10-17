package com.energy.management;

public class EnergyManagementSystem {

    public static void main(String[] args) {
        LogManager logManager = new LogManager();
        DataSimulator dataSimulator = new DataSimulator(logManager);
        UserInterface userInterface = new UserInterface(logManager);

        // Initialize logs
        logManager.initializeLogs();

        // Simulate data exchange
        dataSimulator.simulateDataExchange();

        // User interaction
        userInterface.start();
    }
}
