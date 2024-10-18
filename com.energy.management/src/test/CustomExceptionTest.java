package test;

import org.junit.jupiter.api.*;

import com.energy.management.CustomException;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionTest {

    @Test
    void testCustomException_WithMessage() {
        String message = "Test exception message";
        CustomException exception = new CustomException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testCustomException_WithMessageAndCause() {
        String message = "Test exception with cause";
        Exception cause = new Exception("Underlying cause");
        CustomException exception = new CustomException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testCustomException_NullMessage() {
        CustomException exception = new CustomException(null);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testCustomException_NullCause() {
        String message = "Test exception with null cause";
        CustomException exception = new CustomException(message, null);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testCustomException_ToString() {
        String message = "Test exception message";
        CustomException exception = new CustomException(message);

        assertTrue(exception.toString().contains(message));
    }
}
