package uk.ac.ucl.comp0010.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


/**
 * Represents a module with a code, name, and a boolean indicating whether it is mandatory.
 */
@Entity
public class Module {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 10)
  private String code;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false)
  private boolean mnc = false;

  /**
   * Constructs a new Module.
   */
  public Module() {
    // Default constructor required by JPA
  }

  /**
   * Constructs a new Module.
   *
   * @param code the module code
   * @param name the module name
   * @param mnc indicates if the module is mandatory (true) or optional (false)
   */
  public Module(String code, String name, boolean mnc) {
    this.code = code;
    this.name = name;
    this.mnc = mnc;
  }

  /**
   * Gets the ID of the module.
   *
   * @return the module ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the ID of the module.
   *
   * @param id the new module ID
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the module code.
   *
   * @return the module code
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets the module code.
   *
   * @param code the new module code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Gets the module name.
   *
   * @return the module name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the module name.
   *
   * @param name the new module name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets whether the module is mandatory.
   *
   * @return true if the module is mandatory; false if it is optional
   */
  public boolean getMnc() {
    return mnc;
  }

  /**
   * Sets whether the module is mandatory.
   *
   * @param mnc true if the module should be mandatory; false if it should be optional
   */
  public void setMnc(Boolean mnc) {
    this.mnc = mnc;
  }
}
