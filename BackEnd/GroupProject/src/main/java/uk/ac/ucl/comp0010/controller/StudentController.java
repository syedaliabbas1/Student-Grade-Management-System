package uk.ac.ucl.comp0010.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * A controller managing student objects.
 */
@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentRepository studentRepository;

  public StudentController(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  /**
   * Creates a new student and saves it to the database.
   *
   * @param params a map containing student details (first name, last name, username, email)
   * @return the created student object in the response body
   */
  @PostMapping("/addStudent")
  public ResponseEntity<Student> addStudent(@RequestBody Map<String, String> params) {
    Student student = new Student();
    student.setFirstName(params.get("firstName"));
    student.setLastName(params.get("lastName"));
    student.setUsername(params.get("username"));
    student.setEmail(params.get("email"));
    student = studentRepository.save(student);
    return ResponseEntity.ok(student);
  }

  /**
   * Retrieves all students from the database.
   *
   * @return a list of all student objects
   */
  @GetMapping("/all")
  public ResponseEntity<List<Student>> getAllStudents() {
    List<Student> students = (List<Student>) studentRepository.findAll();
    return ResponseEntity.ok(students);
  }

  /**
   * Retrieves a student by their ID.
   *
   * @param studentId the ID of the student to retrieve
   * @return the student object corresponding to the provided ID
   */
  @GetMapping("/{student_id}")
  public ResponseEntity<?> getStudentById(@PathVariable("student_id") Long studentId) {
    try {
      Student student = studentRepository.findById(studentId)
          .orElseThrow(() -> new RuntimeException("Student not found"));
      return ResponseEntity.ok(student);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
    }
  }

  /**
   * Updates the details of an existing student.
   *
   * @param studentId the ID of the student to update
   * @param params a map containing the updated student details : first name, last name, username
   *        and email
   * @return the updated student object
   */
  @PutMapping("/update/{student_id}")
  public ResponseEntity<Student> updateStudent(@PathVariable("student_id") Long studentId,
      @RequestBody Map<String, String> params) {
    try {
      Student student = studentRepository.findById(studentId)
          .orElseThrow(() -> new RuntimeException("Student not found"));
      student.setFirstName(params.get("firstName"));
      student.setLastName(params.get("lastName"));
      student.setUsername(params.get("username"));
      student.setEmail(params.get("email"));
      student = studentRepository.save(student);
      return ResponseEntity.ok(student);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  /**
   * Deletes a student by their ID.
   *
   * @param studentId the ID of the student to delete
   * @return a message indicating the student was successfully deleted
   */
  @DeleteMapping("/delete/{student_id}")
  public ResponseEntity<String> deleteStudent(@PathVariable("student_id") Long studentId) {
    try {
      Student student = studentRepository.findById(studentId)
          .orElseThrow(() -> new RuntimeException("Student not found"));
      studentRepository.delete(student);
      return ResponseEntity.ok("Student deleted successfully");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
    }
  }
}
