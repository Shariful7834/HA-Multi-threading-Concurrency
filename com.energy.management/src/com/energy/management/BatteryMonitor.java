package com.energy.management;

public class BatteryMonitor implements Runnable {
    private final Battery battery;
    private final int interval; // in milliseconds
    private final LogManager logManager;

    public BatteryMonitor(Battery battery, int interval, LogManager logManager) {
        this.battery = battery;
        this.interval = interval;
        this.logManager = logManager;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int charge = battery.getCurrentCharge();
                String message = "Battery Monitor: Current charge is " + charge + " units.";
                // Log the battery charge level
                logManager.writeLog(logManager.getSystemLogFile(), message);
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
}
