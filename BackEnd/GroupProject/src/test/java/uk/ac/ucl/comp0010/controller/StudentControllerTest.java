package uk.ac.ucl.comp0010.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private Student student;

  @BeforeEach
  public void setUp() {
    studentRepository.deleteAll();
    // Otherwise there would be duplicates inside
    student = new Student();
    student.setFirstName("John");
    student.setLastName("Doe");
    student.setUsername("johndoe");
    student.setEmail("john.doe@example.com");
    student = studentRepository.save(student);
  }

  @Test
  public void testAddStudent() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("firstName", "Jane");
    params.put("lastName", "Smith");
    params.put("username", "janesmith");
    params.put("email", "jane.smith@example.com");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/students/addStudent")
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(params)))
        .andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    Student savedStudent =
        new ObjectMapper().readValue(result.getResponse().getContentAsString(), Student.class);
    assertThat(savedStudent).isNotNull();
    assertEquals("Jane", savedStudent.getFirstName());
    assertEquals("Smith", savedStudent.getLastName());
    assertEquals("janesmith", savedStudent.getUsername());
    assertEquals("jane.smith@example.com", savedStudent.getEmail());
  }

  @Test
  public void testGetAllStudents() throws Exception {
    MvcResult result = mockMvc
        .perform(
            MockMvcRequestBuilders.get("/students/all").contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    Student[] students =
        new ObjectMapper().readValue(result.getResponse().getContentAsString(), Student[].class);
    assertThat(students).isNotEmpty();
    assertEquals(1, students.length);
  }

  @Test
  public void testGetStudentById() throws Exception {
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/students/{student_id}", student.getId())
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    Student foundStudent =
        new ObjectMapper().readValue(result.getResponse().getContentAsString(), Student.class);
    assertThat(foundStudent).isNotNull();
    assertEquals(student.getId(), foundStudent.getId());
  }

  @Test
  public void testUpdateStudent() throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    params.put("firstName", "John Updated");
    params.put("lastName", "doe updated");
    params.put("username", "johndoeupdated");
    params.put("email", "john.doe.updated@example.com");

    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.put("/students/update/{student_id}", student.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(params))).andReturn();
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    Student student =
        objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
    Student updatedStudent =
        new ObjectMapper().readValue(result.getResponse().getContentAsString(), Student.class);
    assertThat(updatedStudent).isNotNull();
    assertEquals("John Updated", updatedStudent.getFirstName());
  }

  @Test
  public void testDeleteStudent() throws Exception {
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.delete("/students/delete/{studentId}", student.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    assertEquals("Student deleted successfully", result.getResponse().getContentAsString());
  }

  @Test
  public void testGetStudentByIdNotFound() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/students/{student_id}", 999L)
        .contentType(MediaType.APPLICATION_JSON)).andReturn();

    assertEquals(404, result.getResponse().getStatus());
    assertThat(result.getResponse().getContentAsString()).contains("Student not found");
  }

  @Test
  public void testUpdateStudentNotFound() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("firstName", "Nonexistent");
    params.put("lastName", "Student");
    params.put("username", "nonexistent");
    params.put("email", "nonexistent@example.com");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .put("/students/update/{student_id}", 999L).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(params))).andReturn();

    assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    assertThat(result.getResponse().getContentAsString()).isEmpty();
  }

  @Test
  public void testDeleteStudentNotFound() throws Exception {
    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/delete/{student_id}", 999L)
            .contentType(MediaType.APPLICATION_JSON)).andReturn();

    assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    assertEquals("Student not found", result.getResponse().getContentAsString());
  }
}
