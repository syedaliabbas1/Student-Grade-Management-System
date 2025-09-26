package uk.ac.ucl.comp0010.exception;

/**
 * Thrown when a student is not registered for a module.
 */
public class NoRegistrationException extends Exception {
  /**
   * Constructs a new {@code NoRegistrationException} with the specified detail message.
   *
   * @param message the detail message
   */
  public NoRegistrationException(String message) {
    super(message);
  }
}
