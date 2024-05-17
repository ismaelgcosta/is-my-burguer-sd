package br.com.ismyburguer.auth;

import br.com.ismyburguer.SwaggerWebConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SwaggerWebConfigurationTest {

    @Test
    public void testCorsFilterConfiguration() {
        // Arrange
        SwaggerWebConfiguration swaggerWebConfiguration = new SwaggerWebConfiguration();

        // Act
        CorsFilter corsFilter = swaggerWebConfiguration.corsFilter();

        // Assert
        assertNotNull(corsFilter);

        UrlBasedCorsConfigurationSource source = (UrlBasedCorsConfigurationSource) ReflectionTestUtils.getField(corsFilter, "configSource");
        assertNotNull(source);

        CorsConfiguration config = (CorsConfiguration) ((LinkedHashMap) ReflectionTestUtils.getField(source, "corsConfigurations")).get(new PathPatternParser().parse("/v3/api-docs"));
        assertNotNull(config);

        assertTrue(config.getAllowedOrigins().contains("*"));
        assertTrue(config.getAllowedHeaders().contains("*"));
        assertTrue(config.getAllowedMethods().contains("*"));
        assertTrue(config.getAllowCredentials());
    }
}
