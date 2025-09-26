package uk.ac.ucl.comp0010.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for exception classes {@link NoGradeAvailableException} and
 * {@link NoRegistrationException}.
 */
class ExceptionTest {

  /**
   * Tests the constructor and message handling for NoGradeAvailableException.
   */
  @Test
  void testNoGradeAvailableException() {
    String errorMessage = "No grades available for student ID: 12345";
    NoGradeAvailableException exception = new NoGradeAvailableException(errorMessage);

    // Validate exception message
    assertEquals(errorMessage, exception.getMessage());
  }

  /**
   * Tests the constructor and message handling for NoRegistrationException.
   */
  @Test
  void testNoRegistrationException() {
    String errorMessage = "Student ID: 12345 is not registered for the module COMP0010";
    NoRegistrationException exception = new NoRegistrationException(errorMessage);

    // Validate exception message
    assertEquals(errorMessage, exception.getMessage());
  }

  /**
   * Tests the behavior when a null message is passed to NoGradeAvailableException.
   */
  @Test
  void testNoGradeAvailableExceptionWithNullMessage() {
    NoGradeAvailableException exception = new NoGradeAvailableException(null);

    // Validate exception message
    assertNull(exception.getMessage());
  }

  /**
   * Tests the behavior when a null message is passed to NoRegistrationException.
   */
  @Test
  void testNoRegistrationExceptionWithNullMessage() {
    NoRegistrationException exception = new NoRegistrationException(null);

    // Validate exception message
    assertNull(exception.getMessage());
  }
}
