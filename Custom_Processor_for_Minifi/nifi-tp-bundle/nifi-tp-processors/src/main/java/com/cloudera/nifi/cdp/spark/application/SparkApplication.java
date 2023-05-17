package com.cloudera.nifi.cdp.spark.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkApplication {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SparkApplicationAttempt {
    public String attemptId;
    public String sparkUser;
    public String startTime;
    public String endTime;
    public boolean completed;

    @Override public String toString() {
      return "SparkApplicationAttempt{" +
              "attemptId='" + attemptId + '\'' +
              ", sparkUser='" + sparkUser + '\'' +
              ", startTime='" + startTime + '\'' +
              ", endTime='" + endTime + '\'' +
              ", completed=" + completed +
              '}';
    }
  }
  public String id;
  public String name;
  public List<SparkApplicationAttempt> attempts;

  @Override public String toString() {
    return "SparkApplication{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", attempts=" + attempts +
            '}';
  }
}
