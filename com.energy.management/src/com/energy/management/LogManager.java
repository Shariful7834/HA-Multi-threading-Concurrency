package com.energy.management;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LogManager {

    private Path logsDir;
    private Path chargingStationsDir;
    private Path energySourcesDir;
    private Path systemLogsDir;
    private DateTimeFormatter dateFormatter;

    public LogManager() {
        logsDir = Paths.get("logs");
        chargingStationsDir = logsDir.resolve("charging_stations");
        energySourcesDir = logsDir.resolve("energy_sources");
        systemLogsDir = logsDir.resolve("system_logs");
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    // Initialize directories and log files
    public void initializeLogs() {
        try {
            // Create directories if they don't exist
            Files.createDirectories(chargingStationsDir);
            Files.createDirectories(energySourcesDir);
            Files.createDirectories(systemLogsDir);

            // Create today's log files
            createDailyLogFiles();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create log files for today
    public void createDailyLogFiles() throws IOException {
        LocalDate currentDate = LocalDate.now();
        String dateString = currentDate.format(dateFormatter);

        // For demonstration, we'll create logs for one station and one source
        createLogFile(chargingStationsDir, "station1_" + dateString + ".log");
        createLogFile(energySourcesDir, "source1_" + dateString + ".log");
        createLogFile(systemLogsDir, "system_" + dateString + ".log");
    }

    // Helper method to create a log file
    private void createLogFile(Path directory, String fileName) throws IOException {
        Path logFile = directory.resolve(fileName);
        if (!Files.exists(logFile)) {
            Files.createFile(logFile);
        }
    }

    // Write data to a log file
    public void writeLog(Path logFile, String message) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardOpenOption.APPEND)) {
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
            throw e;
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

    // Other methods (e.g., archiveLogFile) can be added if needed
}
