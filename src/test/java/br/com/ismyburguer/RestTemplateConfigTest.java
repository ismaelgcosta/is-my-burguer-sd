package br.com.ismyburguer;

import com.netflix.discovery.shared.transport.EurekaHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.configuration.TlsProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.RestTemplateDiscoveryClientOptionalArgs;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

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
    public void deveCriarRestTemplateDiscoveryClientOptionalArgsComSucesso() throws GeneralSecurityException {
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
        RestTemplateDiscoveryClientOptionalArgs args = restTemplateConfig.restTemplateDiscoveryClientOptionalArgs(eurekaClientHttpRequestFactorySupplier);

        // Assert
        assertNotNull(args);
    }

    @Test
    public void deveCriarDefaultRestClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
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

        // Act
        RestClient restClient = restTemplateConfig.defaultRestClient();

        // Assert
        assertNotNull(restClient);
    }

    @Test
    public void deveCriarClientHttpRequestFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
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

        // Act
        ClientHttpRequestFactory requestFactory = restTemplateConfig.clientHttpRequestFactory();

        // Assert
        assertNotNull(requestFactory);
    }

    @Test
    public void deveCriarEurekaInstanceConfig() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
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

        // Act
        InetUtils inetUtils = mock(InetUtils.class);
        InetUtils.HostInfo hostInfo = mock(InetUtils.HostInfo.class);
        when(hostInfo.getIpAddress()).thenReturn("localhost");
        when(inetUtils.findFirstNonLoopbackHostInfo()).thenReturn(hostInfo);
        EurekaInstanceConfigBean instanceConfig = restTemplateConfig.eurekaInstanceConfig(inetUtils);

        // Assert
        assertNotNull(instanceConfig);
    }


}
