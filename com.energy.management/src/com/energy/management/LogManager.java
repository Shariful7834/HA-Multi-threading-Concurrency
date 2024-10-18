package com.energy.management;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LogManager {

    public Path logsDir;
    public Path chargingStationsDir;
    public Path energySourcesDir;
    public Path systemLogsDir;
    public DateTimeFormatter dateFormatter;

    public LogManager() {
        logsDir = Paths.get("logs");
        chargingStationsDir = logsDir.resolve("charging_stations");
        energySourcesDir = logsDir.resolve("energy_sources");
        systemLogsDir = logsDir.resolve("system_logs");
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    // Initialize directories and log files
    public void initializeLogs() throws CustomException {
        try {
            // Create directories if they don't exist
            Files.createDirectories(chargingStationsDir);
            Files.createDirectories(energySourcesDir);
            Files.createDirectories(systemLogsDir);

            // Create today's log files
            createDailyLogFiles();

        } catch (IOException e) {
            throw new CustomException("Failed to initialize logs.", e); // Chaining Exceptions
        }
    }

    // Create log files for today
    public void createDailyLogFiles() throws CustomException {
        try {
            LocalDate currentDate = LocalDate.now();
            String dateString = currentDate.format(dateFormatter);

            // For demonstration, we'll create logs for one station and one source
            createLogFile(chargingStationsDir, "station1_" + dateString + ".log");
            createLogFile(energySourcesDir, "source1_" + dateString + ".log");
            createLogFile(systemLogsDir, "system_" + dateString + ".log");
        } catch (CustomException e) {
            throw new CustomException("Failed to create daily log files.", e); // Chaining Exceptions
        }
    }

    // Helper method to create a log file
    private void createLogFile(Path directory, String fileName) throws CustomException {
        Path logFile = directory.resolve(fileName);
        try {
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }
        } catch (IOException e) {
            throw new CustomException("Failed to create log file: " + logFile, e); // Chaining Exceptions
        }
    }

    // Write data to a log file ---- multiple handling 
    
    public void writeLog(Path logFile, String message) throws CustomException {
        // Resource Management using try-with-resources
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardOpenOption.APPEND)) {
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (AccessDeniedException e) {
            throw new CustomException("Access denied when writing to log file: " + logFile, e); // Chaining Exceptions
        } catch (NoSuchFileException e) {
            throw new CustomException("Log file not found: " + logFile, e); // Chaining Exceptions
        } catch (IOException e) {
            throw new CustomException("Failed to write to log file: " + logFile, e); // Chaining Exceptions
        }
    }

    // Get paths to today's log files
    public Path getStationLogFile(String stationName) {
        String dateString = LocalDate.now().format(dateFormatter);
        return chargingStationsDir.resolve(stationName + "_" + dateString + ".log");
    }

    public Path getSourceLogFile(String sourceName) {
        String dateString = LocalDate.now().format(dateFormatter);
        return energySourcesDir.resolve(sourceName + "_" + dateString + ".log");
    }

    public Path getSystemLogFile() {
        String dateString = LocalDate.now().format(dateFormatter);
        return systemLogsDir.resolve("system_" + dateString + ".log");
    }

    // Get the root logs directory
    public Path getLogsDir() {
        return logsDir;
    }
}
