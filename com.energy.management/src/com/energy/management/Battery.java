package com.energy.management;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Battery {
    private final int capacity;
    private int currentCharge;
    private final LogManager logManager;

    public Battery(int capacity, LogManager logManager) {
        this.capacity = capacity;
        this.currentCharge = 0;
        this.logManager = logManager;
    }

    public synchronized void charge(int amount) throws InterruptedException, CustomException {

    	while (currentCharge + amount > capacity) {
            String message = Thread.currentThread().getName() + " waiting to charge. Battery full.";
            logManager.writeLog(logManager.getSystemLogFile(), message);
            wait();
        }

    	currentCharge += amount;
        String message = Thread.currentThread().getName() + " charged " + amount + " units. Current charge: " + currentCharge;
        logManager.writeLog(logManager.getSystemLogFile(), message);
        notifyAll();
    }

    public synchronized void discharge(int amount) throws InterruptedException, CustomException {
        while (currentCharge - amount < 0) {
            String message = Thread.currentThread().getName() + " waiting to discharge. Battery empty.";
            logManager.writeLog(logManager.getSystemLogFile(), message);
            wait();
        }
        currentCharge -= amount;
        String message = Thread.currentThread().getName() + " discharged " + amount + " units. Current charge: " + currentCharge;
        logManager.writeLog(logManager.getSystemLogFile(), message);
        notifyAll();
    }

    public synchronized int getCurrentCharge() {
        return currentCharge;
    }

    public int getCapacity() {
        return capacity;
    }
}
