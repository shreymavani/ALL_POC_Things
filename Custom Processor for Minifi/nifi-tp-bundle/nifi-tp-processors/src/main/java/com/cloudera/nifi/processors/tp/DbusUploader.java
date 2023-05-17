package com.cloudera.nifi.processors.tp;


import com.cloudera.nifi.cdp.dbus.DatabusUploader;
import com.cloudera.nifi.cdp.file.compress.FileCompressor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
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


@Tags({"dbusUploader"})
@CapabilityDescription("Custom processor to upload file to dbus")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute = "", description = "")})
@WritesAttributes({@WritesAttribute(attribute = "", description = "")})
public class DbusUploader extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbusUploader.class);

    private static final String FIELD_SUCCESS = "success";
    private static final String FIELD_FAILURE = "failure";

    private static final PropertyDescriptor LOCAL_DIR_PATH = new PropertyDescriptor.Builder().name("LocalDirPath")
            .displayName("Local directory path")
            .description("Local directory path")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final PropertyDescriptor DBUS_SERVICE_URL = new PropertyDescriptor.Builder().name("DbusServiceURL")
            .displayName("Dbus service URL")
            .description("Dbus service URL")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final PropertyDescriptor STREAM_NAME = new PropertyDescriptor.Builder().name("STREAM_NAME")
            .displayName("Stream name")
            .description("Stream name")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final Relationship REL_SUCCESS = new Relationship.Builder().name(FIELD_SUCCESS).description("Success relationship").build();

    private static final Relationship REL_FAILURE = new Relationship.Builder().name(FIELD_FAILURE).description("Failure relationship").build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    private FileCompressor fileCompressor;

    @Override protected void init(final ProcessorInitializationContext context) {

        LOGGER.info("DbusUploader initializing");

        final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        descriptors.add(LOCAL_DIR_PATH);
        descriptors.add(STREAM_NAME);
        descriptors.add(DBUS_SERVICE_URL);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE);
        this.relationships = Collections.unmodifiableSet(relationships);

        this.fileCompressor = new FileCompressor();

        LOGGER.info("DbusUploader initialized");
    }

    @Override public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled public void onScheduled(final ProcessContext context) {

    }

    @Override public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        final FlowFile flowFile = session.get();

        if(null == flowFile) {
            LOGGER.info("Null flow file returning");
//            return null;
        }

        final String localDirPath = context.getProperty(LOCAL_DIR_PATH).getValue();
        final String streamName = context.getProperty(STREAM_NAME).getValue();
        final DatabusUploader databusUploader = new DatabusUploader(context.getProperty(DBUS_SERVICE_URL).getValue());

        LOGGER.info("Flow file attributes {}", flowFile.getAttributes());

        try {
            final String[] fileNames = new File(localDirPath).list();
            final StringBuilder fileNameBuilder = new StringBuilder();
            Arrays.stream(fileNames).forEach(fileName -> {
                fileNameBuilder.append(fileName + ",");
            });

            LOGGER.info("Files to transfer {}", fileNameBuilder.toString());

            final Set<String> filePaths = Arrays.stream(fileNames)
                    .map(fileName -> localDirPath + File.separator + fileName)
                    .collect(Collectors.toSet());

            filePaths.forEach(filePath -> {
                LOGGER.info("Compress and upload files from directory {}", filePath);
                final File filesToCompress = new File(filePath);
                if(filesToCompress.isDirectory()) {
                    try {
                        final File compressedFiles = fileCompressor.compressAndDeleteDir(filesToCompress);
                        final String compressedFilePath = compressedFiles.getCanonicalPath();
                        LOGGER.info("Uploading file {}", compressedFilePath);
                        databusUploader.uploadFile(compressedFilePath, streamName);
                        LOGGER.info("Uploaded file {}", compressedFilePath);
                        compressedFiles.delete();
                        LOGGER.info("Deleted file {}", compressedFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    LOGGER.info("Ignoring as {} is not directory", filePath);
                }
            });
        } catch (Throwable throwable) {
            LOGGER.error("Error : ", throwable);
            session.transfer(flowFile, REL_FAILURE);
        }
        session.transfer(flowFile, REL_SUCCESS);
//        return null;
    }

    public static void main(String... args) {
        try {
            new FileCompressor().compressAndDeleteDir(new File("/Users/spandey/nifi-test-dir/filesToUpload"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
