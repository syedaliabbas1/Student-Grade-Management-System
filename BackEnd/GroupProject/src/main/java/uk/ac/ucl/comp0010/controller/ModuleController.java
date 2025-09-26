package uk.ac.ucl.comp0010.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.repository.ModuleRepository;

/**
 * Controller for managing the {@link Module} object. This gives us an endpoint when performing
 * front end operations such as retrieval or creation of modules This allows us to seperate the data
 * and service layer
 */
@RestController
@RequestMapping("/modules")
public class ModuleController {

  private final ModuleRepository moduleRepository;

  /**
   * Initializes the controller with the module repository which acts as the basis for persistence
   * and retrieval of {@link Module} objects.
   *
   * @param moduleRepository This repository stores all of the module objects
   */

  // Repository injection
  public ModuleController(ModuleRepository moduleRepository) {
    this.moduleRepository = moduleRepository;
  }

  /**
   * Handles the creation of a new module. We use map for flexibility in the input from our front
   * end.
   *
   * @param params a map containing the following keys:
   *        <ul>
   *        <li><strong>code</strong>: the code of module</li>
   *        <li><strong>name</strong>: the name of the module</li>
   *        <li><strong>mnc</strong>: module non condonable status</li>
   *        </ul>
   * @return a {@link ResponseEntity} containing the saved {@link Module} object
   */
  // Post mapping because its sending a POST request
  @PostMapping("/addModule")
  public ResponseEntity<Module> addModule(@RequestBody Map<String, String> params) {
    Module module = new Module();
    String code = params.get("code");
    module = moduleRepository.findByCode(code).orElse(module);
    if (module.getCode() != null) {
      moduleRepository.delete(module);
    }
    String name = params.get("name");
    boolean mnc = Boolean.getBoolean(params.get("mnc"));
    module.setName(name);
    module.setMnc(mnc);
    module.setCode(code);
    module = moduleRepository.save(module);

    return ResponseEntity.ok(module);
  }

  /**
   * Retrieves all modules stored in the repository This simply fetches all data on the modules.
   *
   * @return a {@link ResponseEntity} containing the saved {@link Module} object
   */

  @GetMapping("/all")
  public ResponseEntity<List<Module>> getAllStudents() {
    List<Module> students = (List<Module>) moduleRepository.findAll();
    return ResponseEntity.ok(students);
  }

}
