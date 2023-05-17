package com.cloudera.nifi.processors.tp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
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
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tags({"hdfsProxyUser"})
@CapabilityDescription("Custom processor to read file from HDFS")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute = "", description = "")})
@WritesAttributes({@WritesAttribute(attribute = "", description = "")})
public class GetHDFSProxyUser extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetHDFSProxyUser.class);

    private static final String FIELD_SUCCESS = "success";
    private static final String FIELD_FAILURE = "failure";

    private static final PropertyDescriptor HDFS_HOST = new PropertyDescriptor.Builder().name("HdfsHost")
            .displayName("HDFS host")
            .description("HDFS host")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final PropertyDescriptor HDFS_PORT = new PropertyDescriptor.Builder().name("HdfsPort")
            .displayName("HDFS port")
            .description("HDFS port")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final PropertyDescriptor HDFS_USER = new PropertyDescriptor.Builder().name("HdfsUser")
            .displayName("HDFS user")
            .description("HDFS user")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final PropertyDescriptor HDFS_FILE_PATH = new PropertyDescriptor.Builder().name("HdfsFilePath")
            .displayName("HDFS file path")
            .description("HDFS file path")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final Relationship REL_SUCCESS = new Relationship.Builder().name(FIELD_SUCCESS).description("Success relationship").build();

    private static final Relationship REL_FAILURE = new Relationship.Builder().name(FIELD_FAILURE).description("Failure relationship").build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    protected void init(final ProcessorInitializationContext context) {
        LOGGER.info("GetHDFSProxyUser initializing");

        final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        descriptors.add(HDFS_HOST);
        descriptors.add(HDFS_PORT);
        descriptors.add(HDFS_USER);
        descriptors.add(HDFS_FILE_PATH);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE);
        this.relationships = Collections.unmodifiableSet(relationships);

        LOGGER.info("GetHDFSProxyUser initialized");
    }

    @Override public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @Override public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {
        final FlowFile flowFile = session.get();

        if(null == flowFile) {
//            return null;
        }

        final StringBuilder hdfsFilePathBuilder = new StringBuilder(StringUtils.stripEnd(context.getProperty(HDFS_FILE_PATH).getValue(), "/"));
        session.read(flowFile, new InputStreamCallback() {
            @Override
            public void process(InputStream inputStream) throws IOException {
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
                    hdfsFilePathBuilder.append("/");
                    hdfsFilePathBuilder.append(reader.readLine());
                }
            }
        });

        final String hdfsHost = context.getProperty(HDFS_HOST).getValue();
        final String hdfsPort = context.getProperty(HDFS_PORT).getValue();
        final String hdfsUser = context.getProperty(HDFS_USER).getValue();
        final String hdfsUrl = "hdfs://" + hdfsHost + ":" + hdfsPort;
        final String hdfsFilePath = hdfsFilePathBuilder.toString();

        LOGGER.info("HDFS file path = {}", hdfsFilePath);

        try {
            UserGroupInformation.createProxyUser(hdfsUser, UserGroupInformation.getCurrentUser()).doAs(new PrivilegedExceptionAction<Boolean>() {
                @Override public Boolean run() throws IOException {
                    Configuration configuration = new Configuration();
                    configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
                    configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
                    configuration.set("fs.defaultFS", hdfsUrl);
                    FileSystem fileSystem = FileSystem.get(configuration);
                    final Path hdfsReadPath = new Path(hdfsFilePath);
                    FSDataInputStream inputStream = fileSystem.open(hdfsReadPath);
                    try {
                        session.putAttribute(flowFile, "applicationId", UUID.randomUUID().toString());
                        session.importFrom(inputStream, flowFile);
                        session.transfer(flowFile, REL_SUCCESS);
                        session.commit();
                        return Boolean.TRUE;
                    } finally {
                        inputStream.close();
                        fileSystem.close();
                    }
                }
            });
        } catch (Throwable throwable) {
            LOGGER.error("Error : ", throwable);
            session.transfer(flowFile, REL_FAILURE);
            session.rollback();
        }


//        return null;
    }
}
