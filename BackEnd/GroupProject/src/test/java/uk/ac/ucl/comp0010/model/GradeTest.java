package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test class for Grade entity.
 */
public class GradeTest {

  @Test
  void testConstructorAndGetters() {
    // Arrange
    Module module = new Module("COMP0010", "Software Engineering", true);
    int score = 85;

    // Act
    Grade grade = new Grade(score, module);

    // Assert
    assertEquals(score, grade.getScore(), "Score should be 85");
    assertEquals(module, grade.getModule(), "Module should be COMP0010");
  }

  @Test
  void testSetter() {
    // Arrange
    Module module = new Module("COMP0010", "Software Engineering", true);
    Module mathModule = new Module("MATH0010", "Mathematics", true);
    Grade grade = new Grade(30, module);

    // Act
    grade.setScore(90);
    grade.setModule(mathModule);

    // Assert
    assertEquals(90, grade.getScore(), "Score should be set to 90");
    assertEquals(mathModule, grade.getModule(), "Module should be set to MATH0010");
  }

  @Test
  void testDefaultConstructor() {
    // Act
    Grade grade = new Grade();

    // Assert
    assertNull(grade.getId(), "ID should be null by default");
    assertNull(grade.getScore(), "Score should be null by default");
    assertNull(grade.getModule(), "Module should be null by default");
    assertNull(grade.getStudent(), "Student should be null by default");
  }

  @Test
  void testSetId() {
    // Arrange
    Module module = new Module("COMP0010", "Software Engineering", true);
    Grade grade = new Grade(75, module);

    // Act
    grade.setId(100L);

    // Assert
    assertEquals(100L, grade.getId(), "ID should be set to 100");
  }

  @Test
  void testEdgeCaseForScore() {
    // Arrange
    Module module = new Module("COMP0010", "Software Engineering", true);
    Grade grade = new Grade(100, module);

    // Act
    grade.setScore(0);

    // Assert
    assertEquals(0, grade.getScore(), "Score should be updated to 0");
  }

  @Test
  void testEdgeCaseForModule() {
    // Arrange
    Grade grade = new Grade(85, null);

    // Act and Assert
    assertNull(grade.getModule(), "Module should be null when not set explicitly");
  }

  @Test
  void testScoreAndModuleWithNullValues() {
    // Arrange
    Grade grade = new Grade();

    // Act
    grade.setScore(95);
    grade.setModule(null);

    // Assert
    assertEquals(95, grade.getScore(), "Score should be 95 after setting");
    assertNull(grade.getModule(), "Module should be null after setting");
  }

  @Test
  void testConstructorWithScoreStudentAndModule() {
    // Arrange
    Integer score = 75;
    Module module = new Module("COMP0020", "Data Structures", false);
    Student student = new Student(1L, "John", "Doe", "johndoe", "johndoe@example.com");

    // Act
    Grade grade = new Grade(score, student, module);

    // Assert
    assertNull(grade.getId(), "ID should be null when not set");
    assertEquals(score, grade.getScore(), "Score should match the constructor value");
    assertEquals(module, grade.getModule(), "Module should match the constructor value");
    assertEquals(student, grade.getStudent(), "Student should match the constructor value");
  }

  @Test
  void testUpdateFields() {
    // Arrange
    Grade grade = new Grade(50, null, null);
    Module module = new Module("COMP0030", "Algorithms", true);
    Student student = new Student(2L, "Jane", "Smith", "janesmith", "janesmith@example.com");

    // Act
    grade.setScore(88);
    grade.setModule(module);
    grade.setStudent(student);

    // Assert
    assertEquals(88, grade.getScore(), "Score should be updated to 88");
    assertEquals(module, grade.getModule(), "Module should be updated to the new value");
    assertEquals(student, grade.getStudent(), "Student should be updated to the new value");
  }

  @Test
  void testNullFields() {
    // Arrange
    Grade grade = new Grade();
    grade.setScore(null);
    grade.setModule(null);
    grade.setStudent(null);

    // Assert
    assertNull(grade.getScore(), "Score should be null when explicitly set to null");
    assertNull(grade.getModule(), "Module should be null when explicitly set to null");
    assertNull(grade.getStudent(), "Student should be null when explicitly set to null");
  }
}
