package com.cloudera.nifi.cdp.hive;

import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.hive.model.HiveAppList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HiveClient {


    private static final Logger LOGGER = LoggerFactory.getLogger(HiveClient.class);

    private final Client client;

    private final String endpoint;

    private final URI requestUri;

    public HiveClient(ClientFactory clientFactory, String endpoint) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.client = clientFactory.create();
        this.endpoint = endpoint;
        this.requestUri = URI.create(endpoint + "api/v1/queries/active").normalize();
    }

    public static void main(String... args) {
        try {
            new HiveClient(new ClientFactory(), "").getApps();
        } catch (Exception e) {
            LOGGER.error("Error",e);
        }
    }

    public HiveAppList getApps() {
        HiveAppList hiveAppList = client.target(requestUri).request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<HiveAppList>(){});
        LOGGER.info("Response HiveAppList = {}", hiveAppList);

        if(null == hiveAppList || null == hiveAppList.appList || hiveAppList.appList.isEmpty()) {
            LOGGER.warn("Hive application list is null or empty");
            return hiveAppList;
        }

        hiveAppList = client.target(requestUri).request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<HiveAppList>(){});
        hiveAppList.appList.stream().forEach(app -> {
//                if(app.getExecuting().equals("true")){

            LOGGER.info("Execution Engine  = {} ", app.getExecutionEngine());
            LOGGER.info("State = " + app.getState());
            LOGGER.info("Query Display =  "+ app.getQueryDisplay());
//                }
        });

        return hiveAppList;
    }
}
