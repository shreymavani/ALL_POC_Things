package com.cloudera.nifi.cdp.spark.history;

import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.spark.application.SparkApplication;
import com.google.common.collect.Lists;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparkHistoryStateManager implements ISparkHistoryStateManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkHistoryStateManager.class);

    private static SparkHistoryStateManager INSTANCE;

    private final ISparkHistoryClient historyClient;

    private final ConcurrentMap<String, Boolean> prevUploadedFiles;

    private SparkHistoryStateManager(ISparkHistoryClient historyClient) {
        this.historyClient = historyClient;
        prevUploadedFiles = new ConcurrentHashMap<>();
    }

    public synchronized static SparkHistoryStateManager getInstance(String endPoint) throws
            NoSuchAlgorithmException,
            KeyStoreException,
            KeyManagementException {
        if(null != INSTANCE) {
            return INSTANCE;
        }

        INSTANCE = new SparkHistoryStateManager(new SparkHistoryClient(new ClientFactory(), endPoint));
        return INSTANCE;
    }

    @Override
    public synchronized List<String> getFileNames() {
        final List<SparkApplication> applications = historyClient.getApplication();

        if(null == applications || applications.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> newFilesToUpload = Lists.newArrayList();

        applications.forEach(application -> {
            application.attempts.forEach(attempt -> {
                final String fileName = application.id+"_"+attempt.attemptId;
                if(!prevUploadedFiles.getOrDefault(fileName,false)) {
                    newFilesToUpload.add(fileName);
                }
            });
        });

        return newFilesToUpload;
    }

    @Override
    public synchronized void storeUploadedFiles(String fileName) {
        prevUploadedFiles.put(fileName, true);
    }

    @Override
    public synchronized void removeUploadedFile(String fileName) {
        prevUploadedFiles.remove(fileName);
    }
}
