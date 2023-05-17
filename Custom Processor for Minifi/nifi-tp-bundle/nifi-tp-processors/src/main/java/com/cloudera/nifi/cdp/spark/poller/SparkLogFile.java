package com.cloudera.nifi.cdp.spark.poller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkLogFile {
    public String fileName;

    @Override public String toString() {
        return "SparkLogFile{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
