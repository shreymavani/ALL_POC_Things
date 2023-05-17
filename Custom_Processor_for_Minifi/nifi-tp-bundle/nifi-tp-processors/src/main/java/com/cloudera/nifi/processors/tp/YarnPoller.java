package com.cloudera.nifi.processors.tp;


import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.spark.history.ISparkHistoryClient;
import com.cloudera.nifi.cdp.spark.history.ISparkHistoryStateManager;
import com.cloudera.nifi.cdp.spark.history.SparkHistoryStateManager;
import com.cloudera.nifi.cdp.spark.poller.SparkLogFile;
import com.cloudera.nifi.cdp.yarn.model.YarnAppList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import com.sun.jndi.toolkit.url.Uri;
//import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tags({"YarnLivePoller"})
@CapabilityDescription("Custom processor to poll Yarn Live server")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute = "", description = "")})
@WritesAttributes({@WritesAttribute(attribute = "", description = "")})

public class YarnPoller extends AbstractProcessor{
    private static final Logger LOGGER = LoggerFactory.getLogger(YarnPoller.class);

    private static final String FIELD_SUCCESS = "success";
    private static final String FIELD_FAILURE = "failure";

    private static final PropertyDescriptor Yarn_SERVER_ENDPOINT = new PropertyDescriptor.Builder().name("HistoryServerEndpoint")   //History_server_endpoints
            .displayName("Yarn server endpoint")
            .description("Yarn server endpoint")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final Relationship REL_SUCCESS = new Relationship.Builder().name(FIELD_SUCCESS).description("Success relationship").build();

    private static final Relationship REL_FAILURE = new Relationship.Builder().name(FIELD_FAILURE).description("Failure relationship").build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    private Client client;

    @Override protected void init(final ProcessorInitializationContext context) {
        LOGGER.info("YarnPollerPoller initializing");
        try {
            client = new ClientFactory().create();
            final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
            descriptors.add(Yarn_SERVER_ENDPOINT);
            this.descriptors = Collections.unmodifiableList(descriptors);

            final Set<Relationship> relationships = new HashSet<Relationship>();
            relationships.add(REL_SUCCESS);
            relationships.add(REL_FAILURE);
            this.relationships = Collections.unmodifiableSet(relationships);
        } catch (Throwable e) {
            LOGGER.error("Error while initializing SparkHistoryPoller : ", e);
            throw new RuntimeException(e);
        }

        LOGGER.info("YarnPoller initialized");
    }

    @Override public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }
    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {

        FlowFile flowFile = session.create();
        final URI requestUri = URI.create(context.getProperty(Yarn_SERVER_ENDPOINT).getValue() + "/ws/v1/cluster/apps").normalize();

        if(null == flowFile) {
            LOGGER.info("Null flow file returning");
        }

        try {

            YarnAppList yarnAppList = client.target(requestUri).request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<YarnAppList>(){});
            LOGGER.info("Response yarnAppList = {}", yarnAppList);

            YarnAppList finalYarnAppList = yarnAppList;
            flowFile = session.write(flowFile, out-> out.write(finalYarnAppList.toString().getBytes(StandardCharsets.UTF_8)));

            if(null == yarnAppList && null == yarnAppList.appList && yarnAppList.appList.isEmpty()) {
                LOGGER.warn("Yarn application list is null or empty");
//                return yarnAppList;
            }

//            YarnAppList finalYarnAppList1 = yarnAppList;
//            flowFile = session.append(flowFile, (out)-> {
//                            finalYarnAppList1.appList.stream().forEach(app -> {
////                    if(app.getState().equals("RUNNING")){
//                                LOGGER.info("Tracking url of running app {} ", app.getTrackingUrl());
////                                FlowFile flowFile1 = session.create();
//                                final URI requestUriRunningApp = URI.create(app.getTrackingUrl() + "/api/v1/applications").normalize();
//                                final Response response = client.target(requestUriRunningApp).request(MediaType.APPLICATION_JSON_TYPE).get();
//                                LOGGER.info("Response status " + response.getStatus());
//                                LOGGER.info("Response tracking URL {} ", response.readEntity(String.class));
//
////                                flowFile1 = session.write(flowFile1, out -> out.write(app.toString().getBytes(StandardCharsets.UTF_8)));
//                                String appdata = app.toString();
////                                yarnString.concat(appdata);
////                                LOGGER.info("Data of app {}", appdata);
////                                session.transfer(flowFile1, REL_SUCCESS);
////                    }
//                                LOGGER.info("String Output for Tracking URL's = \n"+ appdata);
//                                try {
//                                    out.write(appdata.getBytes());
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            });
//                        });
        } catch (Exception e) {
            LOGGER.error("Error : ", e);
            flowFile = session.write(flowFile,out-> out.write("Error...".getBytes()));
            session.transfer(flowFile, REL_FAILURE);
            session.rollback();
            throw new RuntimeException(e);
        }
        LOGGER.info("Runned Successfully and writing into the file... and changed");
        session.transfer(flowFile, REL_SUCCESS);
    }
}
