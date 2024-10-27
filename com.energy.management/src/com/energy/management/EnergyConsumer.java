package com.energy.management;

public class EnergyConsumer implements Runnable {
    private final Battery battery;
    private final int dischargeAmount;
    private final int interval; // in milliseconds

    public EnergyConsumer(Battery battery, int dischargeAmount, int interval) {
        this.battery = battery;
        this.dischargeAmount = dischargeAmount;
        this.interval = interval;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Discharge the battery
                battery.discharge(dischargeAmount);
                // Sleep for the specified interval
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
}
