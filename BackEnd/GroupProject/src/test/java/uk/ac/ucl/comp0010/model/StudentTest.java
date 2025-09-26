package uk.ac.ucl.comp0010.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;

class StudentTest {
  private Student student;
  private Module compSci1;
  private Module compSci2;
  private Module math1;
  private Module math2;
  private Module physics;
  private Grade gradeCompSci1;
  private Grade gradeCompSci2;
  private Grade gradeMath1;
  private Grade gradeMath2;
  private Grade gradePhysics;

  @BeforeEach
  void setUp() {
    // Initialize student
    student = new Student("John", "Doe", "johndoe", "john.doe@ucl.ac.uk");

    // Initialize modules
    compSci1 = new Module("COMP0010", "Software Engineering", true);
    compSci2 = new Module("COMP0008", "Algorithms", true);
    math1 = new Module("MATH0001", "Linear Algebra", false);
    math2 = new Module("MATH0002", "Calculus", false);
    physics = new Module("PHYS0001", "Mechanics", false);

    // Initialize grades
    gradeCompSci1 = new Grade();
    gradeCompSci1.setModule(compSci1);
    gradeCompSci1.setScore(85);

    gradeCompSci2 = new Grade();
    gradeCompSci2.setModule(compSci2);
    gradeCompSci2.setScore(92);

    gradeMath1 = new Grade();
    gradeMath1.setModule(math1);
    gradeMath1.setScore(78);

    gradeMath2 = new Grade();
    gradeMath2.setModule(math2);
    gradeMath2.setScore(88);

    gradePhysics = new Grade();
    gradePhysics.setModule(physics);
    gradePhysics.setScore(95);
  }

  @Nested
  @DisplayName("Constructor Tests")
  class ConstructorTests {
    @Test
    @DisplayName("Test Default Constructor")
    void testDefaultConstructor() {
      Student defaultStudent = new Student();
      Assertions.assertNotNull(defaultStudent);
      Assertions.assertNull(defaultStudent.getId());
      Assertions.assertNull(defaultStudent.getFirstName());
      Assertions.assertNull(defaultStudent.getLastName());
      Assertions.assertNull(defaultStudent.getUsername());
      Assertions.assertNull(defaultStudent.getEmail());
    }

    @Test
    @DisplayName("Test Parameterized Constructor")
    void testParameterizedConstructor() {
      Assertions.assertNotNull(student);
      Assertions.assertEquals("John", student.getFirstName());
      Assertions.assertEquals("Doe", student.getLastName());
      Assertions.assertEquals("johndoe", student.getUsername());
      Assertions.assertEquals("john.doe@ucl.ac.uk", student.getEmail());
    }

    @Test
    @DisplayName("Test Constructor with ID")
    void testConstructorWithId() {
      Student studentWithId = new Student(1L, "John", "Doe", "johndoe", "john.doe@ucl.ac.uk");
      Assertions.assertEquals(1L, studentWithId.getId());
      Assertions.assertEquals("John", studentWithId.getFirstName());
      Assertions.assertEquals("Doe", studentWithId.getLastName());
      Assertions.assertEquals("johndoe", studentWithId.getUsername());
      Assertions.assertEquals("john.doe@ucl.ac.uk", studentWithId.getEmail());
    }
  }

  @Nested
  @DisplayName("Registration Tests")
  class RegistrationTests {
    @Test
    @DisplayName("Test Single Module Registration")
    void testSingleModuleRegistration() {
      student.registerModule(compSci1);
      Assertions.assertTrue(student.isRegisteredFor(compSci1));
    }

    @Test
    @DisplayName("Test Multiple Module Registration")
    void testMultipleModuleRegistration() {
      student.registerModule(compSci1);
      student.registerModule(compSci2);
      student.registerModule(math1);

      Assertions.assertTrue(student.isRegisteredFor(compSci1));
      Assertions.assertTrue(student.isRegisteredFor(compSci2));
      Assertions.assertTrue(student.isRegisteredFor(math1));
    }

    @Test
    @DisplayName("Test Duplicate Module Registration")
    void testDuplicateModuleRegistration() {
      student.registerModule(compSci1);
      student.registerModule(compSci1);
      student.registerModule(compSci1);
      Assertions.assertTrue(student.isRegisteredFor(compSci1));
    }

    @Test
    @DisplayName("Test Get Registered Modules")
    void testGetRegisteredModules() {
      List<Module> expectedModules = Arrays.asList(compSci1, math1, physics);

      for (Module module : expectedModules) {
        student.registerModule(module);
      }

      List<Module> registeredModules = student.getRegisteredModules();
      Assertions.assertEquals(expectedModules.size(), registeredModules.size());
      Assertions.assertTrue(registeredModules.containsAll(expectedModules));
    }
  }

  @Test
  @DisplayName("Test Duplicate Module Registration")
  void testSetGrades() {
    // Arrange
    Grade grade1 = new Grade(85, new Module("CS101", "Intro to CS", true));
    Grade grade2 = new Grade(90, new Module("CS102", "Data Structures", false));
    List<Grade> grades = Arrays.asList(grade1, grade2);

    // Act
    student.setGrades(grades);
  }

  @Test
  void testSetRegistrations() {
    // Arrange
    Registration reg1 = new Registration(student, new Module("CS101", "Intro to CS", true));
    Registration reg2 = new Registration(student, new Module("CS102", "Data Structures", false));
    List<Registration> registrations = Arrays.asList(reg1, reg2);

    // Act
    student.setRegistrations(registrations);
  }

  @Nested
  @DisplayName("Grade Management Tests")
  class GradeManagementTests {
    @Test
    @DisplayName("Test Add Grade After Registration")
    void testAddGradeAfterRegistration() {
      student.registerModule(compSci1);
      student.addGrade(gradeCompSci1);
    }

    @Test
    @DisplayName("Test Add Multiple Grades")
    void testAddMultipleGrades() {
      // Register modules
      student.registerModule(compSci1);
      student.registerModule(math1);
      student.registerModule(physics);

      // Add grades
      student.addGrade(gradeCompSci1);
      student.addGrade(gradeMath1);
      student.addGrade(gradePhysics);
    }

    @Test
    @DisplayName("Test Update Existing Grade")
    void testUpdateExistingGrade() {
      student.registerModule(compSci1);
      student.addGrade(gradeCompSci1);

      Grade updatedGrade = new Grade();
      updatedGrade.setModule(compSci1);
      updatedGrade.setScore(95);
      student.addGrade(updatedGrade);

    }

    @Test
    @DisplayName("Test Get Grade For Module")
    void testGetGradeForModule() throws NoGradeAvailableException {
      student.registerModule(compSci1);
      student.addGrade(gradeCompSci1);

      Grade retrievedGrade = student.getGrade(compSci1);
      Assertions.assertEquals(85, retrievedGrade.getScore());
    }

    @Test
    @DisplayName("Test Get Non-existent Grade")
    void testGetNonexistentGrade() {
      student.registerModule(compSci1);
      Assertions.assertThrows(NoGradeAvailableException.class, () -> student.getGrade(compSci1));
    }
  }

  @Nested
  @DisplayName("Grade Computation Tests")
  class GradeComputationTests {
    @Test
    @DisplayName("Test Compute Average With Multiple Grades")
    void testComputeAverageWithMultipleGrades() throws NoGradeAvailableException {
      // Register modules
      student.registerModule(compSci1);
      student.registerModule(compSci2);
      student.registerModule(math1);
      student.registerModule(math2);
      student.registerModule(physics);

      // Add grades
      student.addGrade(gradeCompSci1); // 85
      student.addGrade(gradeCompSci2); // 92
      student.addGrade(gradeMath1); // 78
      student.addGrade(gradeMath2); // 88
      student.addGrade(gradePhysics); // 95

      float expectedAverage = (85 + 92 + 78 + 88 + 95) / 5.0f;
      Assertions.assertEquals(expectedAverage, student.computeAverage(), 0.01);
    }

    @Test
    @DisplayName("Test Compute Average With Single Grade")
    void testComputeAverageWithSingleGrade() throws NoGradeAvailableException {
      student.registerModule(compSci1);
      student.addGrade(gradeCompSci1);

      Assertions.assertEquals(85.0f, student.computeAverage(), 0.01);
    }

    @Test
    @DisplayName("Test Compute Average With No Grades")
    void testComputeAverageWithNoGrades() {
      Assertions.assertThrows(NoGradeAvailableException.class, () -> student.computeAverage());
    }

    @Test
    @DisplayName("Test Compute Average With Some Null Grades")
    void testComputeAverageWithSomeNullGrades() throws NoGradeAvailableException {
      student.registerModule(compSci1);
      student.registerModule(math1);

      Grade nullGrade = new Grade();
      nullGrade.setModule(compSci1);
      nullGrade.setScore(null);

      student.addGrade(nullGrade);
      student.addGrade(gradeMath1);

      Assertions.assertEquals(78.0f, student.computeAverage(), 0.01);
    }

    @Test
    @DisplayName("Test Compute Average With All Null Grades")
    void testComputeAverageWithAllNullGrades() {
      student.registerModule(compSci1);

      Grade nullGrade = new Grade();
      nullGrade.setModule(compSci1);
      nullGrade.setScore(null);

      student.addGrade(nullGrade);

      Assertions.assertThrows(NoGradeAvailableException.class, () -> student.computeAverage());
    }
  }

  @Nested
  @DisplayName("Edge Cases and Boundary Tests")
  class EdgeCasesTests {
    @Test
    @DisplayName("Test Grade Boundaries")
    void testGradeBoundaries() throws NoGradeAvailableException {
      student.registerModule(compSci1);

      Grade maxGrade = new Grade();
      maxGrade.setModule(compSci1);
      maxGrade.setScore(100);
      student.addGrade(maxGrade);

      Assertions.assertEquals(100.0f, student.computeAverage(), 0.01);

      Grade minGrade = new Grade();
      minGrade.setModule(compSci1);
      minGrade.setScore(0);
      student.addGrade(minGrade);

      Assertions.assertEquals(0.0f, student.computeAverage(), 0.01);
    }

    @Test
    @DisplayName("Test Large Number of Registrations")
    void testLargeNumberOfRegistrations() {
      List<Module> modules = new ArrayList<>();
      for (int i = 0; i < 100; i++) {
        Module module = new Module("CODE" + i, "Module " + i, i % 2 == 0);
        modules.add(module);
        student.registerModule(module);
      }

      for (Module module : modules) {
        Assertions.assertTrue(student.isRegisteredFor(module));
      }
    }

    @Test
    @DisplayName("Test Registration After Grade")
    void testRegistrationAfterGrade() {
      Grade grade = new Grade();
      grade.setModule(compSci1);
      grade.setScore(90);


      student.registerModule(compSci1);
      Assertions.assertDoesNotThrow(() -> student.addGrade(grade));
    }
  }
}
