package uk.ac.ucl.comp0010.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration class for setting up security policies such as CORS and CSRF in the
 * application. This class defines a {@link SecurityFilterChain} bean and a
 * {@link CorsConfigurationSource} bean to configure HTTP security settings and cross-origin
 * resource sharing (CORS) respectively.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configures the HTTP security settings. Disables CSRF (Cross-Site Request Forgery) protection
   * and enables CORS (Cross-Origin Resource Sharing) using default settings.
   *
   * @param http the {@link HttpSecurity} object used to configure security settings
   * @return a {@link SecurityFilterChain} object that defines the security filter chain
   * @throws Exception if an error occurs while configuring security
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf((csrf) -> csrf.disable()).cors(withDefaults());

    return http.build();
  }

  /**
   * Configures the CORS settings for the application. This configuration allows all origins,
   * headers, and methods for cross-origin requests. It also disables the use of credentials in CORS
   * requests.
   *
   * @return a {@link CorsConfigurationSource} object that provides the CORS configuration
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(Arrays.asList("*")); // Allows requests from all origins
    config.setAllowedHeaders(Arrays.asList("*")); // Allows all headers in requests
    config.setAllowedMethods(Arrays.asList("*")); // Allows all HTTP methods (GET, POST, etc.)
    config.setAllowCredentials(false); // Disables credentials in requests
    config.applyPermitDefaultValues(); // Applies default settings

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config); // Applies the CORS configuration to all paths

    return source;
  }
}
