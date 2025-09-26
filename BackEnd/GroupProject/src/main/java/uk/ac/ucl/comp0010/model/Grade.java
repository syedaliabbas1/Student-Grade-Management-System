package uk.ac.ucl.comp0010.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a Grade for a student in a module.
 * <p>
 * This entity stores the information about the grade a student received in a particular module. The
 * grade includes a score, a reference to the student, and a reference to the module.
 * </p>
 */
@Entity
public class Grade {

  /**
   * The unique identifier for the grade. This is the primary key for the grade entity.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The score the student received in the module. The score can be set to null if not provided
   * during object creation.
   */
  private Integer score;

  /**
   * The module associated with this grade. The grade is associated with a specific module, which
   * represents the course or subject.
   */
  @ManyToOne
  @JoinColumn(name = "module_id")
  private Module module;

  /**
   * The student associated with this grade. This is a foreign key that refers to the student who
   * received the grade.
   */
  @ManyToOne
  @JoinColumn(name = "student_id")
  private Student student;

  /**
   * Default constructor for the Grade class. Initializes the score to null.
   */
  public Grade() {
    this.score = null;
  }

  /**
   * Constructor for creating a Grade with a score and a module.
   *
   * @param score The score the student received in the module.
   * @param module The module associated with this grade.
   */
  public Grade(Integer score, Module module) {
    this.score = score;
    this.module = module;
  }

  /**
   * Constructor for creating a Grade with a score, student, and module.
   *
   * @param score The score the student received in the module.
   * @param student The student associated with this grade.
   * @param module The module associated with this grade.
   */
  public Grade(Integer score, Student student, Module module) {
    this.score = score;
    this.student = student;
    this.module = module;
  }

  /**
   * Gets the unique identifier of the grade.
   *
   * @return The ID of the grade.
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier for the grade.
   *
   * @param id The new ID for the grade.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the score of the grade.
   *
   * @return The score of the grade, or null if not set.
   */
  public Integer getScore() {
    return score;
  }

  /**
   * Sets the score of the grade.
   *
   * @param score The score to set for this grade.
   */
  public void setScore(Integer score) {
    this.score = score;
  }

  /**
   * Gets the module associated with this grade.
   *
   * @return The module associated with the grade.
   */
  public Module getModule() {
    return module;
  }

  /**
   * Sets the module associated with this grade.
   *
   * @param module The module to associate with this grade.
   */
  public void setModule(Module module) {
    this.module = module;
  }

  /**
   * Gets the student associated with this grade.
   *
   * @return The student associated with the grade.
   */
  public Student getStudent() {
    return student;
  }

  /**
   * Sets the student associated with this grade.
   *
   * @param student The student to associate with this grade.
   */
  public void setStudent(Student student) {
    this.student = student;
  }
}
