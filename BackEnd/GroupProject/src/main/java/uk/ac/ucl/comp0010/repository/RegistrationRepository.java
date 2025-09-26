package uk.ac.ucl.comp0010.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ucl.comp0010.model.Registration;

/**
 * A repository manages registration.
 */
public interface RegistrationRepository extends CrudRepository<Registration, Long> {
  // method to find a registration by student id and module id
  Registration findByStudentIdAndModuleId(Long studentId, Long moduleId);
}
