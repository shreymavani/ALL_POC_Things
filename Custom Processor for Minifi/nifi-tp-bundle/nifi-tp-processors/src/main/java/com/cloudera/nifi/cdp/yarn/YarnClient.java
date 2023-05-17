package com.cloudera.nifi.cdp.yarn;

import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.spark.application.SparkApplication;
import com.cloudera.nifi.cdp.yarn.model.YarnAppList;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YarnClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(YarnClient.class);

    private final Client client;

    private final String endpoint;

    private final URI requestUri;

    public YarnClient(ClientFactory clientFactory, String endpoint) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.client = clientFactory.create();
        this.endpoint = endpoint;
        this.requestUri = URI.create(endpoint + "/ws/v1/cluster/apps").normalize();
    }

    public static void main(String... args) {
        try {
            new YarnClient(new ClientFactory(), "http://spandey-2.spandey.root.hwx.site:8088").getApps();
        } catch (Exception e) {
            LOGGER.error("Error",e);
        }
    }

    public YarnAppList getApps() {
        YarnAppList yarnAppList = client.target(requestUri).request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<YarnAppList>(){});
        LOGGER.info("Response yarnAppList = {}", yarnAppList);

        if(null == yarnAppList || null == yarnAppList.appList || yarnAppList.appList.isEmpty()) {
            LOGGER.warn("Yarn application list is null or empty");
            return yarnAppList;
        }



            yarnAppList = client.target(requestUri).request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<YarnAppList>(){});
            yarnAppList.appList.stream().forEach(app -> {
//                if(app.getState().equals("RUNNING")){
                    LOGGER.info("Tracking url of running app {} ", app.getTrackingUrl());
                    final URI requestUriRunningApp = URI.create(app.getTrackingUrl()+"/api/v1/applications").normalize();
                    final Response response = client.target(requestUriRunningApp).request(MediaType.APPLICATION_JSON_TYPE).get();
                    LOGGER.info("Response status "+ response.getStatus());

                    LOGGER.info("Response tracking URL {} ", response.readEntity(String.class));
//                }
            });


        return yarnAppList;
    }
}