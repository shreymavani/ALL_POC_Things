package com.cloudera.nifi.cdp.impala;

import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.impala.model.ImpalaApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class ImpalaClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImpalaClient.class);

    private final Client client;

    private final String endpoint;

    private final URI requestUri;

    public ImpalaClient(ClientFactory clientFactory, String endpoint) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.client = clientFactory.create();
        this.endpoint = endpoint;
        this.requestUri = URI.create(endpoint + "/queries?json").normalize();
    }

    public static void main(String... args) {
        try {
            new ImpalaClient(new ClientFactory(), "http://spandey-1.spandey.root.hwx.site:25000").getApps();
        } catch (Exception e) {
            LOGGER.error("Error",e);
        }
    }

    public ImpalaApp getApps() {
        ImpalaApp ImpalaApp = (client.target(requestUri).request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<ImpalaApp>(){}));
        LOGGER.info("Response ImpalaAppList = {}", ImpalaApp);


        return ImpalaApp;
    }
}
