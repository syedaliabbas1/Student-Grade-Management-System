package uk.ac.ucl.comp0010.exception;

/**
 * Thrown when there are no grades available for a student.
 */
public class NoGradeAvailableException extends Exception {
  /**
   * Constructs a new {@code NoGradeAvailableException} with the specified detail message.
   *
   * @param message the detail message
   */
  public NoGradeAvailableException(String message) {
    super(message);
  }
}
