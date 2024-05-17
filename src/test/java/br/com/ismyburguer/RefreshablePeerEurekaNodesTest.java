package br.com.ismyburguer;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.eureka.EurekaServerConfig;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.netflix.eureka.resources.ServerCodecs;
import jakarta.validation.constraints.AssertTrue;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.netflix.eureka.server.ReplicationClientAdditionalFilters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshablePeerEurekaNodesTest {

    @Mock
    private PeerAwareInstanceRegistry registry;

    @Mock
    private EurekaServerConfig serverConfig;

    @Mock
    private EurekaClientConfig clientConfig;

    @Mock
    private ServerCodecs serverCodecs;

    @Mock
    private ApplicationInfoManager applicationInfoManager;

    @Mock
    private ReplicationClientAdditionalFilters replicationClientAdditionalFilters;

    @InjectMocks
    @Spy
    private RefreshablePeerEurekaNodes refreshablePeerEurekaNodes;

    @BeforeEach
    void setUp() {
        System.setProperty("com.netflix.eureka.shouldSSLConnectionsUseSystemSocketFactory", "false");
        refreshablePeerEurekaNodes.setKeyStorePassword("password");
        lenient().when(serverConfig.getPeerNodeTotalConnectionsPerHost()).thenReturn(1);
        lenient().when(serverConfig.getMaxThreadsForPeerReplication()).thenReturn(1);
        lenient().when(serverConfig.getMaxTimeForReplication()).thenReturn(1);
        lenient().when(serverConfig.getPeerNodeTotalConnections()).thenReturn(1);
        lenient().when(replicationClientAdditionalFilters.getFilters()).thenReturn(List.of(mock(ClientRequestFilter.class)));
    }

    @Test
    void aoOcorrerEventoAmbiente_DeveAtualizarNosPeerEureka_QuandoOcorrerEvento() {
        // Arrange
        Set<String> changedKeys = new HashSet<>();
        changedKeys.add("eureka.client.service-url.my-region.defaultZone");
        EnvironmentChangeEvent event = new EnvironmentChangeEvent(changedKeys);
        lenient().doReturn(List.of("http://localhost:/")).when(refreshablePeerEurekaNodes).resolvePeerUrls();

        // Act
        refreshablePeerEurekaNodes.onApplicationEvent(event);

        // Assert
        verify(refreshablePeerEurekaNodes, times(1)).updatePeerEurekaNodes(any());
    }

    @Test
    void createPeerEurekaNode() {
        // Arrange
        Set<String> changedKeys = new HashSet<>();
        changedKeys.add("eureka.client.region");
        lenient().doReturn(List.of("http://localhost:/")).when(refreshablePeerEurekaNodes).resolvePeerUrls();

        // Act
        refreshablePeerEurekaNodes.createPeerEurekaNode("http://localhost");

        // Assert
        verify(refreshablePeerEurekaNodes, never()).updatePeerEurekaNodes(any());
    }

    @Test
    void createPeerEurekaNodeSsl() {
        System.setProperty("com.netflix.eureka.shouldSSLConnectionsUseSystemSocketFactory", "true");

        // Arrange
        Set<String> changedKeys = new HashSet<>();
        changedKeys.add("eureka.client.region");
        lenient().doReturn(List.of("http://localhost:/")).when(refreshablePeerEurekaNodes).resolvePeerUrls();

        // Act
        refreshablePeerEurekaNodes.createPeerEurekaNode("http://localhost");

        // Assert
        verify(refreshablePeerEurekaNodes, never()).updatePeerEurekaNodes(any());
    }

    @Test
    void aoOcorrerEventoAmbiente_NaoDeveAtualizarNosPeerEureka_QuandoUsandoDnsParaBuscarUrlsServico() {
        // Arrange
        Set<String> changedKeys = new HashSet<>();
        changedKeys.add("eureka.client.service-url.us-east-1.defaultZone");
        when(clientConfig.shouldUseDnsForFetchingServiceUrls()).thenReturn(true);
        EnvironmentChangeEvent event = new EnvironmentChangeEvent(changedKeys);

        // Act
        refreshablePeerEurekaNodes.onApplicationEvent(event);

        // Assert
        verify(refreshablePeerEurekaNodes, never()).updatePeerEurekaNodes(any());
    }

    @Test
    void shouldUpdate() {
        // Act + Assert
        assertTrue(refreshablePeerEurekaNodes.shouldUpdate(Set.of("eureka.client.region")));
    }
}
