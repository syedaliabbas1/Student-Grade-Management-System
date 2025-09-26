package uk.ac.ucl.comp0010.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.ac.ucl.comp0010.exception.NoRegistrationException;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * A controller that manages grade objects and provides endpoints for handling grade-related
 * operations.
 */
@RestController
@RequestMapping
public class GradeController {

  private final StudentRepository studentRepository;
  private final GradeRepository gradeRepository;
  private final ModuleRepository moduleRepository;
  private final RegistrationRepository registrationRepository;

  /**
   * Constructs a GradeController with the specified repositories.
   *
   * @param studentRepository the repository used to manage student data
   * @param gradeRepository the repository used to manage grade data
   * @param moduleRepository the repository used to manage module data
   */
  public GradeController(StudentRepository studentRepository, GradeRepository gradeRepository,
      ModuleRepository moduleRepository, RegistrationRepository registrationRepository) {
    this.studentRepository = studentRepository;
    this.gradeRepository = gradeRepository;
    this.moduleRepository = moduleRepository;
    this.registrationRepository = registrationRepository;
  }

  /**
   * Handles the creation of a new grade for a student.
   *
   * @param params a map containing the following keys:
   *        <ul>
   *        <li><strong>student_id</strong>: the ID of the student</li>
   *        <li><strong>module_code</strong>: the code of the module</li>
   *        <li><strong>score</strong>: the score to assign</li>
   *        </ul>
   * @return a {@link ResponseEntity} containing the saved {@link Grade} object
   * @throws NoRegistrationException if no registration for the student or module is found
   */
  @PostMapping(value = "/grades/addGrade")
  public ResponseEntity<Grade> addGrade(@RequestBody Map<String, String> params)
      throws NoRegistrationException {
    // Find the module by using the module_code
    Module module = moduleRepository.findByCode(params.get("module_code")).orElseThrow();
    // Create a Grade object and set its values
    Integer score = Integer.parseInt(params.get("score"));
    // initialize grade object
    Grade grade = new Grade();
    // Find the student by using student_id
    Long studentId = Long.valueOf(params.get("student_id"));
    // check if grade already exists
    grade = gradeRepository.findByStudentIdAndModuleCode(studentId, module.getCode()).orElse(grade);
    // delete from repository if exists
    if (grade.getId() != null) {
      gradeRepository.deleteById(grade.getId());
    }
    // regardless of if exits or not, update the grade
    Student student = studentRepository.findById(studentId).orElseThrow();
    grade.setScore(score);
    grade.setModule(module);
    grade.setStudent(student);
    // Save the Grade object
    grade = gradeRepository.save(grade);
    student.addGrade(grade);

    // Return the saved Grade object
    return ResponseEntity.ok(grade);
  }

  /**
   * Retrieves a list of all grades.
   *
   * @return A list of all grades.
   */
  @GetMapping(value = "/grades")
  public ResponseEntity<Map<String, Object>> getAllGrades() {
    List<Grade> grades = (List<Grade>) gradeRepository.findAll();

    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();


    List<Map<String, Object>> gradeMaps = grades.stream().map(grade -> {
      Map<String, Object> gradeMap = new HashMap<>();
      gradeMap.put("id", grade.getId());
      gradeMap.put("score", grade.getScore());

      Map<String, Object> links = new HashMap<>();
      links.put("module", Map.of("href", baseUrl + "/grades/" + grade.getId() + "/modules"));
      links.put("student", Map.of("href", baseUrl + "/grades/" + grade.getId() + "/students"));
      gradeMap.put("_links", links);

      return gradeMap;
    }).collect(Collectors.toList());

    Map<String, Object> response = new HashMap<>();
    Map<String, List<Map<String, Object>>> embedded = new HashMap<>();
    embedded.put("grades", gradeMaps);
    response.put("_embedded", embedded);

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves the module associated with the specified grade ID.
   *
   * @param id the ID of the grade whose associated module is to be retrieved.
   * @return a {@link ResponseEntity} containing a map with the module's details, including its code
   *         and name.
   * @throws NoSuchElementException if no grade with the specified ID is found.
   */

  @GetMapping(value = "/grades/{id}/modules")
  public ResponseEntity<Map<String, Object>> getModuleByGradeId(@PathVariable Long id) {
    try {
      Grade grade = gradeRepository.findById(id)
          .orElseThrow(() -> new NoSuchElementException("Grade not found"));
      Module module = grade.getModule();
      Map<String, Object> response = new HashMap<>();
      Map<String, Object> moduleMap = new HashMap<>();
      moduleMap.put("code", module.getCode());
      moduleMap.put("name", module.getName());
      response.put("module", moduleMap);

      return ResponseEntity.ok(moduleMap);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  /**
   * Retrieves the student associated with the specified grade ID.
   *
   * @param id the ID of the grade whose associated student is to be retrieved.
   * @return a {@link ResponseEntity} containing a map with the student's details, including their
   *         ID, first name, and last name.
   * @throws NoSuchElementException if no grade with the specified ID exists.
   */
  @GetMapping(value = "/grades/{id}/students")
  public ResponseEntity<Map<String, Object>> getStudentByGradeId(@PathVariable Long id) {
    try {
      Grade grade = gradeRepository.findById(id)
          .orElseThrow(() -> new NoSuchElementException("Grade not found"));
      Student student = grade.getStudent();
      Map<String, Object> studentMap = new HashMap<>();
      studentMap.put("id", student.getId());
      studentMap.put("firstName", student.getFirstName());
      studentMap.put("lastName", student.getLastName());
      Map<String, Object> response = new HashMap<>();
      response.put("student", studentMap);

      return ResponseEntity.ok(studentMap);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  /**
   * Retrieves a grade by its ID.
   *
   * @param id the ID of the grade to retrieve
   * @return a ResponseEntity containing the grade if found, or an appropriate error response if not
   *         found
   */
  @GetMapping(value = "/grades/{id}")
  public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
    // Fetch the grade with the specified ID from your data source (e.g., database)
    Grade grade = gradeRepository.findById(id).orElseThrow();
    // Assuming you have a GradeRepository

    return ResponseEntity.ok(grade);
  }

  /**
   * Updates the grade with the specified ID.
   *
   * @param id the ID of the grade to update
   * @param params a map containing the parameters for the update: - "student_id": the ID of the
   *        student - "module_code": the code of the module - "score": the new score for the grade
   * @return a ResponseEntity containing the updated Grade object, or a 404 Not Found status if the
   *         grade is not found
   * @throws NoRegistrationException if there is an issue with the registration
   */
  @PutMapping(value = "/grades/{id}")
  public ResponseEntity<Grade> updateGrade(@PathVariable Long id,
      @RequestBody Map<String, String> params) throws NoRegistrationException {
    // Fetch the grade with the specified ID from your data source (e.g., database)
    Grade grade;
    try {
      grade = gradeRepository.findById(id)
          .orElseThrow(() -> new NoSuchElementException("Grade not found"));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    // Find the student by using student_id
    Student student =
        studentRepository.findById(Long.valueOf(params.get("student_id"))).orElseThrow();

    // Find the module by using the module_code
    Module module = moduleRepository.findByCode(params.get("module_code")).orElseThrow();

    // Update the grade object
    Integer score = Integer.parseInt(params.get("score"));
    grade.setScore(score);
    grade.setModule(module);
    grade.setStudent(student);

    // Save the updated Grade object
    grade = gradeRepository.save(grade);

    return ResponseEntity.ok(grade);
  }

  /**
   * Deletes the grade with the specified ID.
   *
   * @param id the ID of the grade to be deleted
   * @return a ResponseEntity with no content if the deletion is successful
   * @throws NoSuchElementException if the grade with the specified ID is not found
   */
  @DeleteMapping(value = "/grades/{id}")
  public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
    try {
      // Fetch the grade with the specified ID from your data source (e.g., database)
      Grade grade = gradeRepository.findById(id).orElseThrow();
      // Assuming you have a GradeRepository
      // registrationRepository.deleteById(tempRegistration.getId());
      // Delete the grade
      gradeRepository.deleteById(id);
      studentRepository.deleteById(grade.getStudent().getId());
      // registrationRepository.save(tempRegistration);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates the score of a grade with the specified ID.
   *
   * @param id the ID of the grade to be updated
   * @param score the new score to be set for the grade
   * @return ResponseEntity containing the updated Grade object
   * @throws NoSuchElementException if the grade with the specified ID is not found
   */
  @PatchMapping(value = "/grades/{id}")
  public ResponseEntity<Grade> updateGradeScore(@PathVariable Long id, @RequestBody Integer score) {
    // Fetch the grade with the specified ID from your data source (e.g., database)
    Grade grade = gradeRepository.findById(id).orElseThrow(); // Assuming you have a GradeRepository

    // Update the grade object
    grade.setScore(score);

    // Save the updated Grade object
    grade = gradeRepository.save(grade);

    return ResponseEntity.ok(grade);
  }


  /*
   * Retrieves the module associated with a specific grade ID.
   *
   * @param id the ID of the grade whose module is to be retrieved
   * 
   * @return ResponseEntity containing the module associated with the specified grade ID
   * 
   * @throws NoSuchElementException if no grade with the specified ID is found
   * 
   * @GetMapping(value = "/grades/{id}/module") public ResponseEntity<Module>
   * getModuleByGradeId(@PathVariable Long id) throws NoSuchElementException { // Fetch the grade
   * with the specified ID from your data source (e.g., database) Grade grade =
   * gradeRepository.findById(id).orElseThrow(); // Assuming you have a GradeRepository
   * 
   * return ResponseEntity.ok(grade.getModule()); }
   */

  /*
   * Retrieves the student associated with a specific grade ID.
   *
   * @param id the ID of the grade whose student is to be retrieved
   * 
   * @return ResponseEntity containing the student associated with the specified grade ID
   * 
   * @throws NoSuchElementException if no grade with the specified ID is found
   * 
   * @GetMapping(value = "/grades/{id}/student") public ResponseEntity<Student>
   * getStudentByGradeId(@PathVariable Long id) throws NoSuchElementException { // Fetch the grade
   * with the specified ID from your data source (e.g., database) Grade grade =
   * gradeRepository.findById(id).orElseThrow(); // Assuming you have a GradeRepository
   * 
   * return ResponseEntity.ok(grade.getStudent()); }
   * 
   */

  /**
   * Retrieves a specific property of a module associated with a grade by grade ID.
   *
   * @param id the ID of the grade
   * 
   * @param proprtyId the property of the module to retrieve (e.g., "code" or "name" or "mnc")
   * 
   * @return a ResponseEntity containing the requested module property as a string, or a BAD_REQUEST
   *         status if the property is invalid
   * 
   * @throws NoSuchElementException if no grade with the specified ID is found
   */
  @GetMapping(value = "/grades/{id}/module/{proprtyId}")
  public ResponseEntity<String> getModulePropertyByGradeId(@PathVariable Long id,
      @PathVariable String proprtyId) throws NoSuchElementException {
    // Fetch the grade with the specified ID from your data source (e.g., database)
    Grade grade = gradeRepository.findById(id).orElseThrow(); // Assuming you have a GradeRepository

    // Retrieve the module associated with the grade
    Module module = grade.getModule();

    // Retrieve the requested property of the module
    String property;
    switch (proprtyId) {
      case "code":
        property = module.getCode();
        break;
      case "name":
        property = module.getName();
        break;
      case "mnc":
        property = String.valueOf(module.getMnc());
        break;
      default:
        return ResponseEntity.badRequest().body("Invalid property ID");
    }

    return ResponseEntity.ok(property);
  }

  /**
   * Retrieves a specific property of a student associated with a grade by grade ID.
   *
   * @param id the ID of the grade
   * @param proprtyId the property of the student to retrieve (e.g., "name" or "email" or "first" or
   *        "last" or "username" or "id")
   * @return a ResponseEntity containing the requested student property as a string, or a
   *         BAD_REQUEST status if the property is invalid
   * @throws NoSuchElementException if no grade with the specified ID is found
   */
  @GetMapping(value = "/grades/{id}/student/{proprtyId}")
  public ResponseEntity<String> getStudentPropertyByGradeId(@PathVariable Long id,
      @PathVariable String proprtyId) throws NoSuchElementException {
    // Fetch the grade with the specified ID
    Grade grade = gradeRepository.findById(id).orElseThrow(); // Assuming you have a GradeRepository

    // Retrieve the student associated with the grade
    Student student = grade.getStudent();

    // Retrieve the requested property of the student
    String property;
    switch (proprtyId) {
      case "first":
        property = student.getFirstName();
        break;
      case "email":
        property = student.getEmail();
        break;
      case "last":
        property = student.getLastName();
        break;
      case "username":
        property = student.getUsername();
        break;
      case "id":
        property = String.valueOf(student.getId());
        break;
      default:
        return ResponseEntity.badRequest().body("Invalid property ID");
    }

    return ResponseEntity.ok(property);
  }
}
