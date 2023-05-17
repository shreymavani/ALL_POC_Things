package com.cloudera.nifi.processors.tp;

import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.spark.history.ISparkHistoryClient;
import com.cloudera.nifi.cdp.spark.history.ISparkHistoryStateManager;
import com.cloudera.nifi.cdp.spark.history.SparkHistoryStateManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.ws.rs.client.Client;
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

@Tags({"sparkHistoryPoller"})
@CapabilityDescription("Custom processor to poll spark history server")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute = "", description = "")})
@WritesAttributes({@WritesAttribute(attribute = "", description = "")})
public class SparkHistoryPoller extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkHistoryPoller.class);

    private static final String FIELD_SUCCESS = "success";
    private static final String FIELD_FAILURE = "failure";

    private static final PropertyDescriptor HISTORY_SERVER_ENDPOINT = new PropertyDescriptor.Builder().name("HistoryServerEndpoint")
            .displayName("Spark history server endpoint")
            .description("Spark history server endpoint")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();


    private static final Relationship REL_SUCCESS = new Relationship.Builder().name(FIELD_SUCCESS).description("Success relationship").build();

    private static final Relationship REL_FAILURE = new Relationship.Builder().name(FIELD_FAILURE).description("Failure relationship").build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    private Client client ;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        LOGGER.info("SparkHistoryPoller initializing");
        try {
            client = new ClientFactory().create();
            final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
            descriptors.add(HISTORY_SERVER_ENDPOINT);
            this.descriptors = Collections.unmodifiableList(descriptors);

            final Set<Relationship> relationships = new HashSet<Relationship>();
            relationships.add(REL_SUCCESS);
            relationships.add(REL_FAILURE);
            this.relationships = Collections.unmodifiableSet(relationships);
        } catch (Throwable e) {
            LOGGER.error("Error while initializing SparkHistoryPoller : ", e);
            throw new RuntimeException(e);
        }

        LOGGER.info("SparkHistoryPoller initialized");
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {

        final FlowFile flowFile = session.create();

        if (null == flowFile) {
            LOGGER.info("Null flow file returning");
        }

        try {
            final ISparkHistoryStateManager sparkHistoryStateManager = SparkHistoryStateManager
                    .getInstance(context.getProperty(HISTORY_SERVER_ENDPOINT).getValue());
            final List<String> fileNames = sparkHistoryStateManager.getFileNames();
            if (fileNames.isEmpty()) {
                LOGGER.info("No new log files for processing");
            }
            else
                LOGGER.info("No of applications {} {}", fileNames.size(), fileNames);
            session.write(flowFile, out -> out.write("Successful Done".getBytes()));
            session.transfer(flowFile,REL_SUCCESS);
        } catch (Exception exception) {
            LOGGER.error("Unable to instantiate the SparkHistoryPoller instance");
        }
        session.write(flowFile,out->out.write("Unable to go inside and create instance of SparkHistoryPoller".getBytes()));
        session.transfer(flowFile, REL_SUCCESS);
    }
}
