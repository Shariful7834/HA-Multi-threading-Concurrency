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
//                    displayFileContent(file);
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

    // Rest of the class...
}
