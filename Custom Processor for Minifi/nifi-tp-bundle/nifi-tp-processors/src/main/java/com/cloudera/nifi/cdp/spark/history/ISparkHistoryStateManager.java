package com.cloudera.nifi.cdp.spark.history;

import com.cloudera.nifi.cdp.spark.application.SparkApplication;
import java.util.List;

public interface ISparkHistoryStateManager {
    List<String> getFileNames();

    void storeUploadedFiles(String file);

    void removeUploadedFile(String fileName);
}
