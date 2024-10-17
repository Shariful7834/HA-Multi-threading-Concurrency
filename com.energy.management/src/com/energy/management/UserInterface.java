package com.energy.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class UserInterface {

    private LogManager logManager;

    public UserInterface(LogManager logManager) {
        this.logManager = logManager;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Energy Management System.");
        System.out.println("Enter 'exit' to quit.");

        while (true) {
            System.out.println("\nEnter equipment name or date (YYYY-MM-DD) to view logs:");
            String userInput = scanner.nextLine();

            if ("exit".equalsIgnoreCase(userInput)) {
                break;
            }

            searchAndDisplayLogs(userInput);
        }

        scanner.close();
    }

    public void searchAndDisplayLogs(String userInput) {
        boolean isDate = userInput.matches("\\d{4}-\\d{2}-\\d{2}");
        String patternString;

        if (isDate) {
            patternString = ".*_" + userInput + "\\.log";
        } else {
            patternString = userInput + "_.*\\.log";
        }

        Pattern pattern = Pattern.compile(patternString);
        Path logsDir = logManager.getLogsDir();

        try {
            List<Path> matchingFiles = new ArrayList<>();

            Files.walk(logsDir)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    Matcher matcher = pattern.matcher(file.getFileName().toString());
                    if (matcher.matches()) {
                        matchingFiles.add(file);
                    }
                });

            if (matchingFiles.isEmpty()) {
                System.out.println("No log files found matching your input.");
            } else {
                for (Path file : matchingFiles) {
                    System.out.println("\nFound log file: " + file);
                    displayFileContent(file);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayFileContent(Path file) {
        System.out.println("Content of " + file.getFileName() + ":");
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            double totalEnergy = 0;
            int totalVehicles = 0;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                // Parse the line to extract data
                if (file.getParent().getFileName().toString().equals("charging_stations")) {
                    Pattern energyPattern = Pattern.compile("Total Energy Consumed: ([\\d\\.]+) kWh");
                    Matcher energyMatcher = energyPattern.matcher(line);
                    if (energyMatcher.find()) {
                        totalEnergy += Double.parseDouble(energyMatcher.group(1));
                    }
                    Pattern vehiclesPattern = Pattern.compile("Vehicles Charged: (\\d+)");
                    Matcher vehiclesMatcher = vehiclesPattern.matcher(line);
                    if (vehiclesMatcher.find()) {
                        totalVehicles += Integer.parseInt(vehiclesMatcher.group(1));
                    }
                }
                lineCount++;
            }

            // Display summary
            if (file.getParent().getFileName().toString().equals("charging_stations") && lineCount > 0) {
                System.out.printf("Summary: Total Vehicles Charged: %d, Total Energy Consumed: %.2f kWh%n",
                        totalVehicles, totalEnergy);
            }

            System.out.println(); // Add an empty line after the content
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
