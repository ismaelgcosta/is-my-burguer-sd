package br.com.ismyburguer;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.transport.jersey3.EurekaIdentityHeaderFilter;
import com.netflix.discovery.shared.transport.jersey3.EurekaJersey3Client;
import com.netflix.discovery.shared.transport.jersey3.EurekaJersey3ClientImpl;
import com.netflix.eureka.EurekaServerConfig;
import com.netflix.eureka.EurekaServerIdentity;
import com.netflix.eureka.cluster.PeerEurekaNode;
import com.netflix.eureka.cluster.PeerEurekaNodes;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.netflix.eureka.resources.ServerCodecs;
import com.netflix.eureka.transport.Jersey3DynamicGZIPContentEncodingFilter;
import com.netflix.eureka.transport.Jersey3ReplicationClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.netflix.eureka.server.ReplicationClientAdditionalFilters;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Set;

@Component
public class RefreshablePeerEurekaNodes extends PeerEurekaNodes implements ApplicationListener<EnvironmentChangeEvent> {

    private ReplicationClientAdditionalFilters replicationClientAdditionalFilters;


    @Value("${server.ssl.jks-key-store}")
    private String keyStoreFile;


    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;

    RefreshablePeerEurekaNodes(final PeerAwareInstanceRegistry registry, final EurekaServerConfig serverConfig,
                               final EurekaClientConfig clientConfig, final ServerCodecs serverCodecs,
                               final ApplicationInfoManager applicationInfoManager,
                               final ReplicationClientAdditionalFilters replicationClientAdditionalFilters) {
        super(registry, serverConfig, clientConfig, serverCodecs, applicationInfoManager);
        this.replicationClientAdditionalFilters = replicationClientAdditionalFilters;
    }

    @Override
    protected PeerEurekaNode createPeerEurekaNode(String peerEurekaNodeUrl) {
        Jersey3ReplicationClient replicationClient = createReplicationClient(serverConfig, serverCodecs,
                peerEurekaNodeUrl, this.replicationClientAdditionalFilters.getFilters());

        String targetHost = hostFromUrl(peerEurekaNodeUrl);
        if (targetHost == null) {
            targetHost = "host";
        }
        return new PeerEurekaNode(registry, targetHost, peerEurekaNodeUrl, replicationClient, serverConfig);
    }

    // FIXME: 4.0 update Jersey3ReplicationClient.createReplicationClient to handle
    // additional filters
    private Jersey3ReplicationClient createReplicationClient(EurekaServerConfig config,
                                                             ServerCodecs serverCodecs, String serviceUrl, Collection<ClientRequestFilter> additionalFilters) {
        String name = Jersey3ReplicationClient.class.getSimpleName() + ": " + serviceUrl + "apps/: ";

        EurekaJersey3Client jerseyClient;
        try {
            String hostname;
            try {
                hostname = new URL(serviceUrl).getHost();
            } catch (MalformedURLException e) {
                hostname = serviceUrl;
            }

            File file = new ClassPathResource("springboot.jks").getFile();
            File springboot = File.createTempFile("springboot", ".jks");
            FileCopyUtils.copy(file, springboot);

            String jerseyClientName = "Discovery-PeerNodeClient-" + hostname;
            EurekaJersey3ClientImpl.EurekaJersey3ClientBuilder clientBuilder = new EurekaJersey3ClientImpl.EurekaJersey3ClientBuilder()
                    .withClientName(jerseyClientName).withUserAgent("Java-EurekaClient-Replication")
                    .withEncoderWrapper(serverCodecs.getFullJsonCodec())
                    .withDecoderWrapper(serverCodecs.getFullJsonCodec())
                    .withConnectionTimeout(config.getPeerNodeConnectTimeoutMs())
                    .withReadTimeout(config.getPeerNodeReadTimeoutMs())
                    .withMaxConnectionsPerHost(config.getPeerNodeTotalConnectionsPerHost())
                    .withMaxTotalConnections(config.getPeerNodeTotalConnections())
                    .withConnectionIdleTimeout(config.getPeerNodeConnectionIdleTimeoutSeconds())
                    .withTrustStoreFile(springboot.getPath(), keyStorePassword);
            if (serviceUrl.startsWith("https://") && "true"
                    .equals(System.getProperty("com.netflix.eureka.shouldSSLConnectionsUseSystemSocketFactory"))) {
                clientBuilder.withSystemSSLConfiguration();
            }
            jerseyClient = clientBuilder.build();
        } catch (Throwable e) {
            throw new RuntimeException("Cannot Create new Replica Node :" + name, e);
        }

        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }

        Client jerseyApacheClient = jerseyClient.getClient();
        jerseyApacheClient.register(new Jersey3DynamicGZIPContentEncodingFilter(config));

        for (ClientRequestFilter filter : additionalFilters) {
            jerseyApacheClient.register(filter);
        }

        EurekaServerIdentity identity = new EurekaServerIdentity(ip);
        jerseyApacheClient.register(new EurekaIdentityHeaderFilter(identity));

        return new Jersey3ReplicationClient(jerseyClient, serviceUrl);
    }

    @Override
    public void onApplicationEvent(final EnvironmentChangeEvent event) {
        if (shouldUpdate(event.getKeys())) {
            updatePeerEurekaNodes(resolvePeerUrls());
        }
    }

    /*
     * Check whether specific properties have changed.
     */
    protected boolean shouldUpdate(final Set<String> changedKeys) {
        assert changedKeys != null;

        // if eureka.client.use-dns-for-fetching-service-urls is true, then
        // service-url will not be fetched from environment.
        if (this.clientConfig.shouldUseDnsForFetchingServiceUrls()) {
            return false;
        }

        if (changedKeys.contains("eureka.client.region")) {
            return true;
        }

        for (final String key : changedKeys) {
            // property keys are not expected to be null.
            if (key.startsWith("eureka.client.service-url.")
                    || key.startsWith("eureka.client.availability-zones.")) {
                return true;
            }
        }
        return false;
    }

}

