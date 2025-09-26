package uk.ac.ucl.comp0010.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;

/**
 * Configuration class exposes ID's of objects shared over rest.
 * <p>
 * This RestConfiguration exposes ID's of entities Student, Module, and Grade via the REST API.
 * These are otherwise default set to hidden. Exposing them can be useful for clients referencing
 * ID's or entities in API requests.
 * </p>
 */
@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

  /**
   * Configures the behavior of the Spring Data REST repository.
   * <p>
   * In REST responses, IDs of the three entities Student, Module, and Grade are now exposed.
   * </p>
   *
   * @param config the RepositoryRestConfiguration instance used for customizing.
   * @param cors the CorsRegistry, to configure CORS policies.
   */
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
      CorsRegistry cors) {

    // Exposing IDs allows for linking entities and specifying specific objects.
    // Now clients can retrieve entity IDs.
    config.exposeIdsFor(Student.class);
    config.exposeIdsFor(Module.class);
    config.exposeIdsFor(Grade.class);
  }
}
