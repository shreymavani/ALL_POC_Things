package com.cloudera.nifi.processors.tp;

import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.impala.model.ImpalaApp;
import org.apache.nifi.processor.AbstractProcessor;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

//import com.sun.jndi.toolkit.url.Uri;
//import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImpalaPoller extends AbstractProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImpalaPoller.class);

    private static final String FIELD_SUCCESS = "success";
    private static final String FIELD_FAILURE = "failure";

    private static final PropertyDescriptor Impala_SERVER_ENDPOINT = new PropertyDescriptor.Builder().name("LiveServerEndpoint")   //History_server_endpoints
            .displayName("Impala server endpoint")
            .description("Impala server endpoint")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final Relationship REL_SUCCESS = new Relationship.Builder().name(FIELD_SUCCESS).description("Success relationship").build();

    private static final Relationship REL_FAILURE = new Relationship.Builder().name(FIELD_FAILURE).description("Failure relationship").build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    private Client client;

    @Override protected void init(final ProcessorInitializationContext context) {
        LOGGER.info("ImpalaPollerPoller initializing");
        try {
            client = new ClientFactory().create();
            final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
            descriptors.add(Impala_SERVER_ENDPOINT);
            this.descriptors = Collections.unmodifiableList(descriptors);

            final Set<Relationship> relationships = new HashSet<Relationship>();
            relationships.add(REL_SUCCESS);
            relationships.add(REL_FAILURE);
            this.relationships = Collections.unmodifiableSet(relationships);
        } catch (Throwable e) {
            LOGGER.error("Error while initializing SparkHistoryPoller : ", e);
            throw new RuntimeException(e);
        }

        LOGGER.info("ImpalaPoller initialized");
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
        final URI requestUri = URI.create(context.getProperty(Impala_SERVER_ENDPOINT).getValue() + "/queries?json").normalize();

        if(null == flowFile) {
            LOGGER.info("Null flow file returning");
        }

        try {

            ImpalaApp impalaApp = (client.target(requestUri).request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<ImpalaApp>(){}));
            LOGGER.info("Response ImpalaAppList = {}", impalaApp);

            ImpalaApp finalImpalaApp = impalaApp;
            flowFile = session.write(flowFile, out-> out.write(finalImpalaApp.toString().getBytes(StandardCharsets.UTF_8)));

            if(null == impalaApp && null == impalaApp.in_flight_queries && impalaApp.in_flight_queries.isEmpty()) {
                LOGGER.warn("Impala application list is null or empty");
//                return impalaAppList;
            }

            ImpalaApp finalImpalaAppList1 = impalaApp;
            flowFile = session.append(flowFile, (out)-> {
                            finalImpalaAppList1.in_flight_queries.stream().forEach(app -> {
//                if(app.getExecuting().equals("true")){

                                LOGGER.info("Query Id  = {} ", app.getQuery_id());
                                LOGGER.info("Stmt type = " + app.getStmt_type());
                                LOGGER.info("Stmt = " + app.getStmt());
//                }
                            });
                        });
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

