package com.cloudera.nifi.cdp.client;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.nifi.annotation.behavior.RequiresInstanceClassLoading;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
public class ClientFactory {

    public Client create() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new StdDateFormat());
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        JacksonJsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);

        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(jsonProvider);
        clientConfig.connectorProvider(new ApacheConnectorProvider());
        clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, createConnectionManager());

        clientConfig.property(
                ApacheClientProperties.KEEPALIVE_STRATEGY,
                DefaultConnectionKeepAliveStrategy.INSTANCE);
        clientConfig.property(
                ClientProperties.REQUEST_ENTITY_PROCESSING,
                RequestEntityProcessing.BUFFERED);

        final Client client = ClientBuilder.newBuilder()
                .withConfig(clientConfig)
                .build();
        client.property(ClientProperties.READ_TIMEOUT,60000);
        client.property(ClientProperties.CONNECT_TIMEOUT, 60000);
        return client;
    }

    private PoolingHttpClientConnectionManager createConnectionManager() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        final TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        final KeyStore ks = null;
        trustManagerFactory.init(ks);
        final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        final HostnameVerifier hostnameVerifier = new DefaultHostnameVerifier();

        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());

        final Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", new SSLConnectionSocketFactory(sslContext,
                                hostnameVerifier))
                        .build();

        final PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setValidateAfterInactivity(60000);
        connectionManager.setDefaultMaxPerRoute(5);
        connectionManager.setMaxTotal(10);
        return connectionManager;
    }
}
