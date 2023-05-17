package com.cloudera.nifi.cdp.spark.history;

import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.spark.application.SparkApplication;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparkHistoryClient implements ISparkHistoryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkHistoryClient.class);

    private final String endpoint;

    private final URI requestUri;

    private final Client client;

    public SparkHistoryClient(ClientFactory clientFactory, String endpoint) throws
            NoSuchAlgorithmException,
            KeyStoreException,
            KeyManagementException {
        this.endpoint = endpoint;
        this.client = clientFactory.create();
        this.requestUri = URI.create(endpoint + "/api/v1/applications").normalize();
    }

    @Override
    public List<SparkApplication> getApplication() {
        return client.target(requestUri).request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<SparkApplication>>(){});
    }

    public static void main(String... args) {
        try {
            final List<SparkApplication> applications = new SparkHistoryClient(new ClientFactory(), "http://smavani-3.smavani.root.hwx.site:18088").getApplication();

            LOGGER.info("No of applications {} {}", applications.size(), applications);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
