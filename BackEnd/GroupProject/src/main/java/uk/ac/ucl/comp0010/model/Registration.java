package uk.ac.ucl.comp0010.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * A class representing a registration of a student for a module.
 */
@Entity
@Table(name = "registration")
public class Registration {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "student_id")
  private Student student;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "module_id", unique = true)
  private Module module;

  /**
   * Default constructor required by JPA.
   */
  public Registration() {}

  /**
   * Constructs a new Registration with the given student and module.
   *
   * @param student the student
   * @param module the module
   */
  public Registration(Student student, Module module) {
    this.student = student;
    this.module = module;
  }

  /**
   * Gets the ID of the registration.
   *
   * @return the ID of the registration
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the ID of the registration.
   *
   * @param id the new ID of the registration
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the student associated with the registration.
   *
   * @return the student
   */
  public Student getStudent() {
    return student;
  }

  /**
   * Sets the student associated with the registration.
   *
   * @param student the new student
   */
  public void setStudent(Student student) {
    this.student = student;
  }

  /**
   * Gets the module associated with the registration.
   *
   * @return the module
   */
  public Module getModule() {
    return module;
  }

  /**
   * Sets the module associated with the registration.
   *
   * @param module the new module
   */
  public void setModule(Module module) {
    this.module = module;
  }
}
