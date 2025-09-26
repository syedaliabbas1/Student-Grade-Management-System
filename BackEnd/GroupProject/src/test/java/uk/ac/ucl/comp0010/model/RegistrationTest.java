package uk.ac.ucl.comp0010.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class RegistrationTest {
  private Registration registration;
  private Student student;
  private Module module;

  @BeforeEach
  void setUp() {
    // Mock Student and Module objects
    student = new Student();
    student.setId(1L);
    student.setFirstName("Jasmine");
    student.setLastName("Jane");
    student.setUsername("Jasne");
    student.setEmail("jj@example.com");


    module = new Module();
    module.setCode("CS101");
    module.setName("Introduction to Computer Science");
    module.setMnc(false);

    // Initialize a Registration object
    registration = new Registration(student, module);
  }

  @Test
  void testDefaultConstructor() {
    Registration defaultRegistration = new Registration();
    assertNull(defaultRegistration.getId());
    assertNull(defaultRegistration.getStudent());
    assertNull(defaultRegistration.getModule());
  }

  @Test
  void testParameterizedConstructor() {
    assertEquals(student, registration.getStudent());
    assertEquals(module, registration.getModule());
  }

  @Test
  void testGetId() {
    registration.setId(100L);
    assertEquals(100L, registration.getId());
  }

  @Test
  void testSetId() {
    registration.setId(200L);
    assertEquals(200L, registration.getId());
  }

  @Test
  void testGetStudent() {
    assertEquals(student, registration.getStudent());
  }

  @Test
  void testSetStudent() {
    Student newStudent = new Student();
    newStudent.setId(2L);
    newStudent.setFirstName("Jane");
    newStudent.setLastName("Smith");
    newStudent.setUsername("jsmith");
    newStudent.setEmail("jsmith@example.com");

    registration.setStudent(newStudent);
    assertEquals(newStudent, registration.getStudent());
  }

  @Test
  void testGetModule() {
    assertEquals(module, registration.getModule());
  }

  @Test
  void testSetModule() {
    Module newModule = new Module();
    newModule.setCode("CS102");
    newModule.setName("Advanced Programming");
    newModule.setMnc(true);

    registration.setModule(newModule);
    assertEquals(newModule, registration.getModule());
  }
}
