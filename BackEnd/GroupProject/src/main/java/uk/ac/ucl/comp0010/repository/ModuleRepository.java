package uk.ac.ucl.comp0010.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import uk.ac.ucl.comp0010.model.Module;

/**
 * A repository manages Module.
 */
public interface ModuleRepository extends CrudRepository<Module, Long> {
  Optional<Module> findByCode(String code);

}
