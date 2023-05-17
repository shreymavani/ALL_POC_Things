package com.cloudera.nifi.cdp.spark.history;

import com.cloudera.nifi.cdp.spark.application.SparkApplication;
import java.util.List;

public interface ISparkHistoryClient {
    List<SparkApplication> getApplication();
}
