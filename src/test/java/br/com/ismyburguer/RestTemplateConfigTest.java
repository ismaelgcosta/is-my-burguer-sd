package br.com.ismyburguer;

import com.netflix.discovery.shared.transport.EurekaHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.configuration.TlsProperties;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.RestTemplateDiscoveryClientOptionalArgs;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestTemplateConfigTest {

    @Test
    public void deveCriarRestTemplateComSucesso() {
        // Arrange
        RestTemplateConfig restTemplateConfig = new RestTemplateConfig();
        restTemplateConfig.setServiceUrl("eureka.client.serviceUrl.defaultZone");

        // Act
        RestTemplate restTemplate = null;
        try {
            restTemplate = restTemplateConfig.restTemplate();
        } catch (Exception e) {
            fail("Erro ao criar RestTemplate: " + e.getMessage());
        }

        // Assert
        assertNotNull(restTemplate);
    }

    @Test
    public void deveCriarEurekaHttpClientComSucesso() {
        // Arrange
        RestTemplateConfig restTemplateConfig = new RestTemplateConfig();
        restTemplateConfig.setServiceUrl("value");

        // Act
        EurekaHttpClient eurekaHttpClient = null;
        try {
            eurekaHttpClient = restTemplateConfig.eurekaHttpClient();
        } catch (Exception e) {
            fail("Erro ao criar EurekaHttpClient: " + e.getMessage(), e);
        }

        // Assert
        assertNotNull(eurekaHttpClient);
    }

    @Test
    public void deveCriarRestTemplateDiscoveryClientOptionalArgsComSucesso() {
        // Arrange
        RestTemplateConfig restTemplateConfig = new RestTemplateConfig();
        restTemplateConfig.setServiceUrl("value");

        TlsProperties tlsProperties = mock(TlsProperties.class);
        when(tlsProperties.getKeyStore()).thenReturn(new ClassPathResource("springboot.p12"));
        when(tlsProperties.getKeyStorePassword()).thenReturn("password");
        when(tlsProperties.keyPassword()).thenReturn("password".toCharArray());
        when(tlsProperties.keyStorePassword()).thenReturn("password".toCharArray());
        when(tlsProperties.getKeyPassword()).thenReturn("password");
        when(tlsProperties.getKeyStoreType()).thenReturn("pkcs12");
        when(tlsProperties.getTrustStore()).thenReturn(new ClassPathResource("springboot.p12"));
        when(tlsProperties.getTrustStorePassword()).thenReturn("password");
        when(tlsProperties.getTrustStoreType()).thenReturn("pkcs12");

        EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier = mock(EurekaClientHttpRequestFactorySupplier.class);

        // Act
        RestTemplateDiscoveryClientOptionalArgs args = null;
        try {
            args = restTemplateConfig.restTemplateDiscoveryClientOptionalArgs(eurekaClientHttpRequestFactorySupplier);
        } catch (Exception e) {
            fail("Erro ao criar RestTemplateDiscoveryClientOptionalArgs: " + e.getMessage(), e);
        }

        // Assert
        assertNotNull(args);
    }


}
