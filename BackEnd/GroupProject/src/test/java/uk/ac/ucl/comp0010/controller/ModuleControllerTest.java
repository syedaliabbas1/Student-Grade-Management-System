package uk.ac.ucl.comp0010.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ModuleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ModuleRepository moduleRepository;

  @Autowired
  private GradeRepository gradeRepository;

  @Autowired
  private RegistrationRepository registrationRepository;

  @Autowired
  private StudentRepository studentRepository;

  @BeforeEach
  public void setUp() {
    registrationRepository.deleteAll();
    gradeRepository.deleteAll();
    studentRepository.deleteAll();
    moduleRepository.deleteAll();

    // Ensures there is atleast one module in repository before running test
    Module module = new Module();
    //module.setId(1L);
    module.setCode("1");
    module.setName("Mathematics");
    module.setMnc(true);
    moduleRepository.save(module);
  }


  @Test
  public void testAddModule() throws JsonProcessingException, Exception {
    // Prepare request parameters that represent input data
    Map<String, String> params = new HashMap<String, String>();
    params.put("code", "COMP0100");
    params.put("name", "Software Engineering");
    params.put("mnc", "True");

    // Perform POST request, simulating a mock API call returning what would be the result
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.post("/modules/addModule")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(params)))
        .andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    // assert status of response is 200, if so API call is successful
    String responseContent = result.getResponse().getContentAsString();
    ObjectMapper objectMapper = new ObjectMapper();
    Module module = objectMapper.readValue(responseContent, Module.class);
    Optional<Module> test_module = moduleRepository.findByCode(params.get("code"));
    // Check that the module is saved in the repository, to confirm its creation of the object
    assertThat(test_module).isPresent();
    assertThat(module).isNotNull();
    assertEquals(moduleRepository.findByCode(params.get("code")).get().getName(), module.getName());
    assertEquals(moduleRepository.findByCode(params.get("code")).get().getCode(), module.getCode());
  }

  @Test
  public void testGetAllModules() throws Exception {
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get("/modules/all").contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    Module[] modules =
        new ObjectMapper().readValue(result.getResponse().getContentAsString(), Module[].class);
    // Convert response content into an array of Modules
    assertThat(modules).isNotEmpty();
    assertEquals(1, modules.length);
  }


  @Test
  public void testAddModuleWithExistingCode() throws JsonProcessingException, Exception {
    // Prepare request parameters that represent input data
    Map<String, String> params = new HashMap<String, String>();
    params.put("code", "1"); // Existing code
    params.put("name", "Advanced Mathematics");
    params.put("mnc", "False");

    // Perform POST request, simulating a mock API call returning what would be the result
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.post("/modules/addModule")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(params)))
        .andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    // assert status of response is 200, if so API call is successful
    String responseContent = result.getResponse().getContentAsString();
    ObjectMapper objectMapper = new ObjectMapper();
    Module module = objectMapper.readValue(responseContent, Module.class);
    Optional<Module> test_module = moduleRepository.findByCode(params.get("code"));
    // Check that the module is saved in the repository, to confirm its creation of the object
    assertThat(test_module).isPresent();
    assertThat(module).isNotNull();
    assertEquals(moduleRepository.findByCode(params.get("code")).get().getName(), module.getName());
    assertEquals(moduleRepository.findByCode(params.get("code")).get().getCode(), module.getCode());
    assertEquals("Advanced Mathematics", module.getName());
    assertEquals(false, module.getMnc());
  }
}