package uk.ac.ucl.comp0010.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import uk.ac.ucl.comp0010.exception.NoGradeAvailableException;
import uk.ac.ucl.comp0010.exception.NoRegistrationException;

/**
 * A class representing a student.
 * <p>
 * This entity class is used to store and manage a student's information including their grades,
 * registration details, and other attributes such as name, username, and email.
 * </p>
 */
@Entity
@Table(name = "student")
public class Student {

  /**
   * The unique identifier for the student. This is the primary key for the student entity.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  /**
   * The first name of the student.
   */
  @Column(name = "firstName", nullable = false, length = 30)
  private String firstName;

  /**
   * The last name of the student.
   */
  @Column(name = "lastName", nullable = false, length = 30)
  private String lastName;

  /**
   * The username of the student. It must be unique.
   */
  @Column(name = "username", nullable = false, unique = true, length = 30)
  private String username;

  /**
   * The email address of the student. It must be unique.
   */
  @Column(name = "email", nullable = false, unique = true, length = 50)
  private String email;

  /**
   * The grades associated with this student. This represents the student's scores for modules.
   */
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Grade> grades = new ArrayList<>();

  /**
   * The registrations for modules that this student is enrolled in.
   */
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Registration> registrations = new ArrayList<>();


  /**
   * Default constructor for the Student class. Initializes the student without setting any fields.
   */
  public Student() {}

  /**
   * Constructs a new Student with the specified attributes.
   *
   * @param firstName the student's first name
   * @param lastName the student's last name
   * @param username the student's username
   * @param email the student's email address
   */
  public Student(String firstName, String lastName, String username, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
  }

  /**
   * Constructs a new Student with the specified attributes including ID.
   *
   * @param id the student's unique ID
   * @param firstName the student's first name
   * @param lastName the student's last name
   * @param username the student's username
   * @param email the student's email address
   */
  public Student(long id, String firstName, String lastName, String username, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
  }

  // Getters and Setters

  /**
   * Gets the unique identifier of the student.
   *
   * @return the ID of the student
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier for the student.
   *
   * @param id the ID to set for the student
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the first name of the student.
   *
   * @return the student's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of the student.
   *
   * @param firstName the first name to set for the student
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name of the student.
   *
   * @return the student's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of the student.
   *
   * @param lastName the last name to set for the student
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the username of the student.
   *
   * @return the student's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the student.
   *
   * @param username the username to set for the student
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the email address of the student.
   *
   * @return the student's email address
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email address of the student.
   *
   * @param email the email to set for the student
   */
  public void setEmail(String email) {
    this.email = email;
  }


  /**
   * Sets the grades for the student.
   *
   * @param grades the list of grades to set
   */
  public void setGrades(List<Grade> grades) {
    this.grades = grades;
  }

  /**
   * Sets the registrations for the student.
   *
   * @param registrations the list of registrations to set
   */
  public void setRegistrations(List<Registration> registrations) {
    this.registrations = registrations;
  }

  /**
   * Computes the average grade of the student.
   *
   * @return the average grade as a float
   * @throws NoGradeAvailableException if no grades are available for the student
   */
  public float computeAverage() throws NoGradeAvailableException {
    if (grades.isEmpty()) {
      throw new NoGradeAvailableException("No grades available for student: " + id);
    }

    float sum = 0;
    int count = 0;

    for (Grade grade : grades) {
      if (grade.getScore() != null) {
        sum += grade.getScore();
        count++;
      }
    }

    if (count == 0) {
      throw new NoGradeAvailableException("No valid grades available for student: " + id);
    }

    return sum / count;
  }

  /**
   * Adds a grade to the student's record.
   *
   * @param grade the grade to add for the student
   */
  public void addGrade(Grade grade) {
    /*
     * boolean isRegistered = registrations.stream().anyMatch( registration ->
     * registration.getModule().getCode().equals(grade.getModule().getCode()));
     * 
     * if (!isRegistered) { throw new NoRegistrationException( "Student " + id +
     * " is not registered for module " + grade.getModule().getCode()); }
     */
    grade.setStudent(this);

    Optional<Grade> existingGrade = grades.stream()
        .filter(g -> g.getModule().getCode().equals(grade.getModule().getCode())).findFirst();

    if (existingGrade.isPresent()) {
      existingGrade.get().setScore(grade.getScore());
    } else {
      grades.add(grade);
    }
  }

  /**
   * Retrieves the grade for a specific module.
   *
   * @param module the module to get the grade for
   * @return the grade for the module
   * @throws NoGradeAvailableException if no grade exists for the module
   */
  public Grade getGrade(Module module) throws NoGradeAvailableException {
    /*
     * boolean isRegistered = registrations.stream() .anyMatch(registration ->
     * registration.getModule().getCode().equals(module.getCode()));
     * 
     * if (!isRegistered) { throw new NoRegistrationException( "Student " + id +
     * " is not registered for module " + module.getCode()); }
     */

    return grades.stream().filter(grade -> grade.getModule().getCode().equals(module.getCode()))
        .findFirst().orElseThrow(() -> new NoGradeAvailableException(
            "No grade available for student " + id + " in module " + module.getCode()));
  }

  /**
   * Registers the student for a specific module.
   *
   * @param module the module to register for
   */
  public void registerModule(Module module) {
    boolean alreadyRegistered = registrations.stream()
        .anyMatch(registration -> registration.getModule().getCode().equals(module.getCode()));

    if (!alreadyRegistered) {
      Registration registration = new Registration();
      registration.setStudent(this);
      registration.setModule(module);
      registrations.add(registration);
    }
  }

  /**
   * Retrieves all the modules the student is registered for.
   *
   * @return a list of modules the student is registered for
   */
  public List<Module> getRegisteredModules() {
    List<Module> modules = new ArrayList<>();
    for (Registration registration : registrations) {
      modules.add(registration.getModule());
    }
    return modules;
  }

  /**
   * Checks if the student is registered for a specific module.
   *
   * @param module the module to check
   * @return true if the student is registered, false otherwise
   */
  public boolean isRegisteredFor(Module module) {
    return registrations.stream()
        .anyMatch(registration -> registration.getModule().getCode().equals(module.getCode()));
  }
}


