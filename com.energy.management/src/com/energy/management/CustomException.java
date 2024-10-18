package com.energy.management;

/**
 * CustomException serves as a generic exception for the Energy Management System.
 */
public class CustomException extends Exception {
    /**
     * Constructs a new CustomException with the specified detail message.
     *
     * @param message The detail message.
     */
    public CustomException(String message) {
        super(message);
    }

    /**
     * Constructs a new CustomException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
