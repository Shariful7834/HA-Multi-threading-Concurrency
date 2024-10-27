package com.energy.management;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {
    private Path logsDir;
    private Path systemLogDir;
    private Path stationLogDir;
    private Path sourceLogDir;
    private Path systemLogFile;

    public LogManager() {
        // Base directory for all logs
        logsDir = Paths.get("logs");

        // Sub-directories for different types of logs
        systemLogDir = logsDir.resolve("system_logs");
        stationLogDir = logsDir.resolve("charging_stations");
        sourceLogDir = logsDir.resolve("energy_sources");

        // System log file with current date
        systemLogFile = systemLogDir.resolve("system_log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log");
    }

    public void initializeLogs() throws CustomException {
        try {
            // Create directories if they don't exist
            if (!Files.exists(systemLogDir)) {
                Files.createDirectories(systemLogDir);
            }
            if (!Files.exists(stationLogDir)) {
                Files.createDirectories(stationLogDir);
            }
            if (!Files.exists(sourceLogDir)) {
                Files.createDirectories(sourceLogDir);
            }

            // Create system log file if it doesn't exist
            if (!Files.exists(systemLogFile)) {
                Files.createFile(systemLogFile);
            }
        } catch (IOException e) {
            throw new CustomException("Failed to initialize log files", e);
        }
    }

    public synchronized void writeLog(Path logFile, String message) throws CustomException {
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (AccessDeniedException e) {
            throw new CustomException("Access denied when writing to log file: " + logFile, e);
        } catch (NoSuchFileException e) {
            throw new CustomException("Log file not found: " + logFile, e);
        } catch (IOException e) {
            throw new CustomException("Failed to write to log file: " + logFile, e);
        }
    }


    public Path getSystemLogFile() {
        return systemLogFile;
    }

    public Path getStationLogFile(String stationName) throws CustomException {
        // Create a log file for the station with the current date
        Path stationLogFile = stationLogDir.resolve(stationName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log");
        try {
            // Create the station log file if it doesn't exist
            if (!Files.exists(stationLogFile)) {
                Files.createFile(stationLogFile);
            }
        } catch (IOException e) {
            throw new CustomException("Failed to create station log file: " + stationLogFile, e);
        }
        return stationLogFile;
    }

    public Path getSourceLogFile(String sourceName) throws CustomException {
        // Create a log file for the source with the current date
        Path sourceLogFile = sourceLogDir.resolve(sourceName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log");
        try {
            // Create the source log file if it doesn't exist
            if (!Files.exists(sourceLogFile)) {
                Files.createFile(sourceLogFile);
            }
        } catch (IOException e) {
            throw new CustomException("Failed to create source log file: " + sourceLogFile, e);
        }
        return sourceLogFile;
    }

    public Path getLogsDir() {
        return logsDir;
    }
}
