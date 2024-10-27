package com.energy.management;

public class EnergySource implements Runnable {
    private final Battery battery;
    private final int chargeAmount;
    private final int interval; // 

    public EnergySource(Battery battery, int chargeAmount, int interval) {
        this.battery = battery;
        this.chargeAmount = chargeAmount;
        this.interval = interval;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                battery.charge(chargeAmount);
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
}
