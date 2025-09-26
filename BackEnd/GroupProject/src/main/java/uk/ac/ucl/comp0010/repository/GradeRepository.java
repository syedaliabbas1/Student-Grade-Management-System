package uk.ac.ucl.comp0010.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import uk.ac.ucl.comp0010.model.Grade;

/**
 * A repository manages Grade.
 */
public interface GradeRepository extends CrudRepository<Grade, Long> {
  Optional<Grade> findByStudentIdAndModuleCode(Long studentId, String moduleCode);

}
