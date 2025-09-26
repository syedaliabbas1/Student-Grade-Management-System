package uk.ac.ucl.comp0010.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Registration;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GradeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private ModuleRepository moduleRepository;

  @Autowired
  private GradeRepository gradeRepository;

  @Autowired
  private RegistrationRepository registrationRepository;

  Student student;
  Module module;


  @BeforeEach
  public void setUp() {
    registrationRepository.deleteAll();
    gradeRepository.deleteAll();
    studentRepository.deleteAll();
    moduleRepository.deleteAll();

    module = new Module();
    module.setCode("COMP00010");
    module.setName("Mathematics"); // Added name field
    module.setMnc(true);
    moduleRepository.save(module);
    // Set up test data
    student = new Student();
    // no hardcoding of Id values as Auto generator doesnt doest reset to 1 if objects deleted
    student.setFirstName("John"); // Added name field
    student.setLastName("Doe"); // Added name fiel
    student.setUsername("john.doe"); // Added username field
    student.setEmail("john.doe@example.com"); // Added email field
    student.registerModule(module);
    student = studentRepository.save(student);

    assertThat(studentRepository.findById(student.getId())).isPresent();
    // check if size of studentRepository is 1
    assertEquals(studentRepository.count(), 1);


  }

  @Test
  public void testAddGrade() throws JsonProcessingException, Exception {
    // Prepare request parameters
    Map<String, String> params = new HashMap<String, String>();
    params.put("student_id", String.valueOf(student.getId()));
    params.put("module_code", "COMP00010");
    params.put("score", "90");

    // Perform the request
    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.post("/grades/addGrade").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(params)))
        .andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    // Verify the response
    String responseContent = result.getResponse().getContentAsString();
    Grade grade = objectMapper.readValue(responseContent, Grade.class);

    assertThat(grade).isNotNull();
    assertNotNull(grade.getStudent(), "Student should not be null");
    assertEquals(student.getId(), grade.getStudent().getId());
    assertEquals(student.getFirstName(), grade.getStudent().getFirstName());
    assertThat(grade).isNotNull();
    assertThat(grade.getScore()).isEqualTo(90);
    assertThat(grade.getModule().getId()).isEqualTo(module.getId());
    Long test_id = grade.getId();
    Grade test_grade = gradeRepository.findById(test_id).get();
    assertNotNull(test_grade);
    assertEquals(test_grade.getScore(), grade.getScore());
    assertEquals(test_grade.getModule().getCode(), grade.getModule().getCode());
    assertEquals(test_grade.getStudent().getFirstName(), "John");
    Optional<Student> test_student = studentRepository.findById(student.getId());
    assertThat(test_student).isPresent();
    assertEquals(test_student.get().getGrade(grade.getModule()).getScore(), grade.getScore());
  }

  @Test
  public void testGetAllGrades() throws Exception {
    // Ensure unique student data
    Student student1 = new Student("Abc", "Def", "ad", "abc@gmail.com");
    Student student2 = new Student("Pqr", "Stu", "ps", "pqr@gmail.com");

    // Save students to the database
    studentRepository.saveAll(Arrays.asList(student1, student2));

    // Create grades with the saved students
    Grade grade1 = new Grade(90, student1, module);
    Grade grade2 = new Grade(80, student2, module);
    // gradeRepository.deleteAll()
    gradeRepository.saveAll(Arrays.asList(grade1, grade2)); // Save grades to database before test

    // When (Perform the GET request)
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get("/grades").contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    // Then (Verify the response)
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status); // Check for successful response

    // Convert response content to a List of Grade objects
    String responseContent = result.getResponse().getContentAsString();
    Map<String, Object> responseMap =
        objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});

    // Verify the structure of the response
    assertTrue(responseMap.containsKey("_embedded"));
    Map<String, Object> embedded = (Map<String, Object>) responseMap.get("_embedded");
    assertTrue(embedded.containsKey("grades"));
    List<Map<String, Object>> grades = (List<Map<String, Object>>) embedded.get("grades");

    // Assert that the retrieved grades are not null
    assertNotNull(grades);

    // Assert that the number of retrieved grades matches the expected number
    assertEquals(2, grades.size()); // Adjust based on number of saved grades

    // Assert the values of the retrieved grades
    assertEquals(90, (int) grades.get(0).get("score"));
    assertEquals(80, (int) grades.get(1).get("score"));
  }

  @Test
  public void testGetGradeById() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId())
        .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Convert response content to a Grade object
    String responseContent = result.getResponse().getContentAsString();
    Grade actualGrade = objectMapper.readValue(responseContent, Grade.class);

    // Assert that the retrieved grade is not null
    assertNotNull(actualGrade);

    // Assert that the retrieved grade matches the saved grade
    assertEquals(grade.getId(), actualGrade.getId());
    assertEquals(grade.getScore(), actualGrade.getScore());
    assertEquals(grade.getStudent().getId(), actualGrade.getStudent().getId());
    assertEquals(grade.getModule().getCode(), actualGrade.getModule().getCode());
  }

  @Test
  public void testUpdateGrade() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Prepare request parameters for updating the grade
    Map<String, String> params = new HashMap<>();
    params.put("student_id", student.getId().toString());
    params.put("module_code", module.getCode());
    params.put("score", "95");

    // Perform the PUT request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/grades/" + grade.getId())
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(params)))
        .andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Convert response content to a Grade object
    String responseContent = result.getResponse().getContentAsString();
    Grade updatedGrade = objectMapper.readValue(responseContent, Grade.class);

    // Assert that the updated grade is not null
    assertNotNull(updatedGrade);

    // Assert that the updated grade matches the expected values
    assertEquals(grade.getId(), updatedGrade.getId());
    assertEquals(95, updatedGrade.getScore());
    assertEquals(student.getId(), updatedGrade.getStudent().getId());
    assertEquals(module.getCode(), updatedGrade.getModule().getCode());
  }

  @Test
  public void testUpdateGradeWithInvalidId() throws Exception {
    // Prepare request parameters for updating the grade
    Map<String, String> params = new HashMap<>();
    params.put("student_id", student.getId().toString());
    params.put("module_code", module.getCode());
    params.put("score", "95");

    // Perform the PUT request with an invalid grade ID
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/grades/999")
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(params)))
        .andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
  }

  @Test
  public void testDeleteGrade() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the DELETE request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/grades/" + grade.getId())
        .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.NO_CONTENT.value(), status);

    // Verify that the grade has been deleted
    Optional<Grade> deletedGrade = gradeRepository.findById(grade.getId());
    assertThat(deletedGrade).isNotPresent();
  }

  @Test
  public void testDeleteGradeWithInvalidId() throws Exception {
    // Perform the DELETE request with an invalid grade ID
    MvcResult result = mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/grades/999").contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
  }

  @Test
  public void testUpdateGradeScore() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Prepare the new score
    Integer newScore = 95;

    // Perform the PATCH request
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/grades/" + grade.getId())
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newScore)))
        .andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Convert response content to a Grade object
    String responseContent = result.getResponse().getContentAsString();
    Grade updatedGrade = objectMapper.readValue(responseContent, Grade.class);

    // Assert that the updated grade is not null
    assertNotNull(updatedGrade);

    // Assert that the updated grade matches the expected values
    assertEquals(grade.getId(), updatedGrade.getId());
    assertEquals(newScore, updatedGrade.getScore());
    assertEquals(student.getId(), updatedGrade.getStudent().getId());
    assertEquals(module.getCode(), updatedGrade.getModule().getCode());
  }

  @Test
  public void testGetModuleByGradeId2() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/module")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Convert response content to a Module object
    String responseContent = result.getResponse().getContentAsString();
    Module actualModule = objectMapper.readValue(responseContent, Module.class);

    // Assert that the retrieved module is not null
    assertNotNull(actualModule);

    // Assert that the retrieved module matches the saved module
    assertEquals(module.getId(), actualModule.getId());
    assertEquals(module.getCode(), actualModule.getCode());
    assertEquals(module.getName(), actualModule.getName());
  }

  /*
   * @Test public void testGetStudentByGradeId() throws Exception { // Create and save a grade Grade
   * grade = new Grade(85, student, module); gradeRepository.save(grade);
   * 
   * // Perform the GET request MvcResult result =
   * mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/student")
   * .contentType(MediaType.APPLICATION_JSON)).andReturn();
   * 
   * // Verify the response status int status = result.getResponse().getStatus();
   * assertEquals(HttpStatus.OK.value(), status);
   * 
   * // Convert response content to a Student object String responseContent =
   * result.getResponse().getContentAsString(); Student actualStudent =
   * objectMapper.readValue(responseContent, Student.class);
   * 
   * // Assert that the retrieved student is not null assertNotNull(actualStudent);
   * 
   * // Assert that the retrieved student matches the saved student assertEquals(student.getId(),
   * actualStudent.getId()); assertEquals(student.getFirstName(), actualStudent.getFirstName());
   * assertEquals(student.getLastName(), actualStudent.getLastName());
   * assertEquals(student.getUsername(), actualStudent.getUsername());
   * assertEquals(student.getEmail(), actualStudent.getEmail()); }
   */

  @Test
  public void testGetModulePropertyByGradeId_Code() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request for the "code" property
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/module/code")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Verify the response content
    String responseContent = result.getResponse().getContentAsString();
    assertEquals(module.getCode(), responseContent);
  }

  @Test
  public void testGetModulePropertyByGradeId_Name() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request for the "name" property
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/module/name")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Verify the response content
    String responseContent = result.getResponse().getContentAsString();
    assertEquals(module.getName(), responseContent);
  }

  @Test
  public void testGetModulePropertyByGradeId_Mnc() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request for the "mnc" property
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/module/mnc")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Verify the response content
    String responseContent = result.getResponse().getContentAsString();
    assertEquals(String.valueOf(module.getMnc()), responseContent);
  }

  @Test
  public void testGetStudentPropertyByGradeId_FirstName() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request for the "first" property
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/student/first")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Verify the response content
    String responseContent = result.getResponse().getContentAsString();
    assertEquals(student.getFirstName(), responseContent);
  }

  @Test
  public void testGetStudentPropertyByGradeId_LastName() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request for the "last" property
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/student/last")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Verify the response content
    String responseContent = result.getResponse().getContentAsString();
    assertEquals(student.getLastName(), responseContent);
  }

  @Test
  public void testGetStudentPropertyByGradeId_Email() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request for the "email" property
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/student/email")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Verify the response content
    String responseContent = result.getResponse().getContentAsString();
    assertEquals(student.getEmail(), responseContent);
  }

  @Test
  public void testGetStudentPropertyByGradeId_Username() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request for the "username" property
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/student/username")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Verify the response content
    String responseContent = result.getResponse().getContentAsString();
    assertEquals(student.getUsername(), responseContent);
  }

  @Test
  public void testGetStudentPropertyByGradeId_Id() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request for the "id" property
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/student/id")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Verify the response content
    String responseContent = result.getResponse().getContentAsString();
    assertEquals(String.valueOf(student.getId()), responseContent);
  }

  @Test
  public void testGetModuleByGradeId() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/modules")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Convert response content to a Map
    String responseContent = result.getResponse().getContentAsString();
    Map<String, Object> responseMap =
        objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});

    // Assert that the retrieved module data is not null
    assertNotNull(responseMap);

    // Extract the module data from the response
    Map<String, Object> moduleMap = (Map<String, Object>) responseMap.get("module");

    // Assert that the retrieved module matches the saved module
    assertEquals(module.getCode(), responseMap.get("code"));
    assertEquals(module.getName(), responseMap.get("name"));
  }

  @Test
  public void testGetModuleByGradeId_NotFound() throws Exception {
    // Perform the GET request with an invalid grade ID
    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.get("/grades/999/modules").contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
  }

  @Test
  public void testGetStudentByGradeId() throws Exception {
    // Create and save a grade
    Grade grade = new Grade(85, student, module);
    gradeRepository.save(grade);

    // Perform the GET request
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/students")
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), status);

    // Convert response content to a Map
    String responseContent = result.getResponse().getContentAsString();
    Map<String, Object> responseMap =
        objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});

    // Assert that the retrieved student data is not null
    assertNotNull(responseMap);

    // Extract the student data from the response
    Map<String, Object> studentMap = (Map<String, Object>) responseMap.get("student");

    // Assert that the retrieved student matches the saved student
    assertEquals(student.getId(), ((Number) responseMap.get("id")).longValue());
    assertEquals(student.getFirstName(), responseMap.get("firstName"));
    assertEquals(student.getLastName(), responseMap.get("lastName"));
  }

  @Test
  public void testGetStudentByGradeId_NotFound() throws Exception {
    // Perform the GET request with an invalid grade ID
    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.get("/grades/999/students").contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    // Verify the response status
    int status = result.getResponse().getStatus();
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
  }
  /*
   * @Test public void testGetModuleByGradeId() throws Exception { // Create and save a grade Grade
   * grade = new Grade(85, student, module); gradeRepository.save(grade);
   * 
   * // Perform the GET request MvcResult result =
   * mockMvc.perform(MockMvcRequestBuilders.get("/grades/" + grade.getId() + "/modules")
   * .contentType(MediaType.APPLICATION_JSON)).andReturn();
   * 
   * // Verify the response status int status = result.getResponse().getStatus();
   * assertEquals(HttpStatus.OK.value(), status);
   * 
   * // Convert response content to a Map String responseContent =
   * result.getResponse().getContentAsString(); Map<String, Object> responseMap =
   * objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});
   * 
   * // Assert that the retrieved module data is not null assertNotNull(responseMap);
   * 
   * // Extract the module data from the response Map<String, Object> dataMap = (Map<String,
   * Object>) responseMap.get("data"); Map<String, Object> moduleMap = (Map<String, Object>)
   * dataMap.get("module");
   * 
   * // Assert that the retrieved module matches the saved module assertEquals(module.getCode(),
   * moduleMap.get("code")); assertEquals(module.getName(), moduleMap.get("name"));
   * assertEquals(module.getMnc(), moduleMap.get("mnc")); }
   */
}
