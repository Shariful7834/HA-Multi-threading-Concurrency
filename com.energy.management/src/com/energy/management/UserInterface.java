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

    public void start() throws CustomException {
        System.out.println("Welcome to the Energy Management System.");
        System.out.println("Enter 'exit' to quit.");

        try (Scanner scanner = new Scanner(System.in)) { // Resource Management
            while (true) {
                System.out.println("\nEnter equipment name or date (YYYY-MM-DD) to view logs:");
                String userInput = scanner.nextLine();

                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }

                try {
                    searchAndDisplayLogs(userInput);
                } catch (CustomException e) {
                    System.err.println("An error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new CustomException("An unexpected error occurred in the user interface.", e); // Chaining Exceptions
        }
    }

    public void searchAndDisplayLogs(String userInput) throws CustomException {
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

        } catch (NoSuchFileException e) {
            throw new CustomException("Log directory not found.", e); // Chaining Exceptions
        } catch (AccessDeniedException e) {
            throw new CustomException("Access denied to log directory.", e); // Chaining Exceptions
        } catch (IOException e) {
            throw new CustomException("An I/O error occurred while searching logs.", e); // Chaining Exceptions
        }
    }

    public void displayFileContent(Path file) throws CustomException {
        System.out.println("Content of " + file.getFileName() + ":");
        try (BufferedReader reader = Files.newBufferedReader(file)) { // Resource Management
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
        } catch (NoSuchFileException e) {
            throw new CustomException("Log file not found: " + file.getFileName(), e); // Chaining Exceptions
        } catch (AccessDeniedException e) {
            throw new CustomException("Access denied to log file: " + file.getFileName(), e); // Chaining Exceptions
        } catch (IOException e) {
            throw new CustomException("An error occurred while reading the file: " + file.getFileName(), e); // Chaining Exceptions
        }
    }
}
