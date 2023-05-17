// (c) Copyright 2021 Cloudera, Inc. All rights reserved.
package com.cloudera.nifi.cdp.yarn.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


public class YarnApp implements Serializable {
    private static final long serialVersionUID = 2142301016370252401L;
    public static class YarnAppAttempt implements Serializable {
        private static final long serialVersionUID = 8899073478059991907L;

        private String id;
        private String nodeId;
        private String nodeHttpAddress;
        private String logsLink;
        private String containerId;
        private String appAttemptId;
        private long startTime;
        private final Map<String, Object> unmappedProperties =
                Maps.newHashMap();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getNodeHttpAddress() {
            return nodeHttpAddress;
        }

        public void setNodeHttpAddress(String nodeHttpAddress) {
            this.nodeHttpAddress = nodeHttpAddress;
        }

        public String getLogsLink() {
            return logsLink;
        }

        public void setLogsLink(String logsLink) {
            this.logsLink = logsLink;
        }

        public String getContainerId() {
            return containerId;
        }

        public void setContainerId(String containerId) {
            this.containerId = containerId;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getAppAttemptId() {
            return appAttemptId;
        }

        public void setAppAttemptId(String appAttemptId) {
            this.appAttemptId = appAttemptId;
        }

        @JsonAnyGetter
        public Map<String, Object> getUnmappedProperties() {
            return unmappedProperties;
        }

        @JsonAnySetter
        public void setUnmappedProperties(String name, Object value) {
            unmappedProperties.put(name, value);
        }

        @Override public String toString() {
            return "YarnAppAttempt{" +
                    "id='" + id + '\'' +
                    ", nodeId='" + nodeId + '\'' +
                    ", nodeHttpAddress='" + nodeHttpAddress + '\'' +
                    ", logsLink='" + logsLink + '\'' +
                    ", containerId='" + containerId + '\'' +
                    ", appAttemptId='" + appAttemptId + '\'' +
                    ", startTime=" + startTime +
                    ", unmappedProperties=" + unmappedProperties +
                    '}';
        }
    }

    private String id;
    private String user;
    private String name;
    private String queue;
    private String state;
    private String finalStatus;
    private float progress;
    private String trackingUI;
    private String trackingUrl;
    private String diagnostics;
    private long clusterId;
    private String applicationType;
    private String applicationTags;
    private String priority;
    private long startedTime;
    private long launchTime;
    private long finishedTime;
    private long elapsedTime;
    private String amContainerLogs;
    private String amHostHttpAddress;
    private String amRPCAddress;
    private int allocatedMB;
    private int allocatedVCores;
    private int runningContainers;
    private long memorySeconds;
    private long vcoreSeconds;
    private float queueUsagePercentage;
    private float clusterUsagePercentage;
    private long preemptedResourcesMB;
    private long preemptedResourceVCores;
    private int numNonAMContainerPreempted;
    private int numAmContainerPreempted;
    private String logAggregationStatus;
    private boolean unmanagedApplication;
    private String appNodeLabelExpression;
    private String amNodeLabelExpression;
    private List<YarnAppAttempt> yarnAppAttemptList;
    private final Map<String, Object> unmappedProperties =
            Maps.newHashMap();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getTrackingUI() { return trackingUI; }

    public void setTrackingUI(String trackingUI) { this.trackingUI = trackingUI; }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }


    public String getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationTags() {
        return applicationTags;
    }

    public void setApplicationTags(String applicationTags) {
        this.applicationTags = applicationTags;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getStartedTime() { return startedTime; }

    public void setStartedTime(Long startedTime) { this.startedTime = startedTime; }

    public Long getLaunchTime() { return launchTime; }

    public void setLaunchTime(Long startedTime) { this.launchTime = launchTime; }

    public Long getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Long finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getAmContainerLogs() {
        return amContainerLogs;
    }

    public void setAmContainerLogs(String amContainerLogs) {
        this.amContainerLogs = amContainerLogs;
    }

    public String getAmHostHttpAddress() {
        return amHostHttpAddress;
    }

    public void setAmHostHttpAddress(String amHostHttpAddress) {
        this.amHostHttpAddress = amHostHttpAddress;
    }

    public String getAmRPCAddress() {
        return amRPCAddress;
    }

    public void setAmRPCAddress(String amRPCAddress) {
        this.amRPCAddress = amRPCAddress;
    }

    public int getAllocatedMB() {
        return allocatedMB;
    }

    public void setAllocatedMB(int allocatedMB) {
        this.allocatedMB = allocatedMB;
    }

    public int getAllocatedVCores() {
        return allocatedVCores;
    }

    public void setAllocatedVCores(int allocatedVCores) {
        this.allocatedVCores = allocatedVCores;
    }

    public int getRunningContainers() {
        return runningContainers;
    }

    public void setRunningContainers(int runningContainers) {
        this.runningContainers = runningContainers;
    }

    public long getMemorySeconds() {
        return memorySeconds;
    }

    public void setMemorySeconds(long memorySeconds) {
        this.memorySeconds = memorySeconds;
    }

    public long getVcoreSeconds() {
        return vcoreSeconds;
    }

    public void setVcoreSeconds(long vcoreSeconds) {
        this.vcoreSeconds = vcoreSeconds;
    }

    public float getQueueUsagePercentage() {
        return queueUsagePercentage;
    }

    public void setQueueUsagePercentage(float queueUsagePercentage) {
        this.queueUsagePercentage = queueUsagePercentage;
    }

    public float getClusterUsagePercentage() {
        return clusterUsagePercentage;
    }

    public void setClusterUsagePercentage(float clusterUsagePercentage) {
        this.clusterUsagePercentage = clusterUsagePercentage;
    }

    public long getPreemptedResourceMB() {
        return preemptedResourcesMB;
    }

    public void setPreemptedResourceMB(long preemptedResourcesMB) {
        this.preemptedResourcesMB = preemptedResourcesMB;
    }

    public long getPreemptedResourceVCores() {
        return preemptedResourceVCores;
    }

    public void setPreemptedResourceVCores(long preemptedResourceVCores) {
        this.preemptedResourceVCores = preemptedResourceVCores;
    }

    public int getNumNonAMContainerPreempted() {
        return numNonAMContainerPreempted;
    }

    public void setNumNonAMContainerPreempted(int numNonAMContainerPreempted) {
        this.numNonAMContainerPreempted = numNonAMContainerPreempted;
    }

    public int getNumAMContainerPreempted() {
        return numAmContainerPreempted;
    }

    public void setNumAMContainerPreempted(int numAMContainerPreempted) {
        this.numAmContainerPreempted = numAMContainerPreempted;
    }

    public String getLogAggregationStatus() {
        return logAggregationStatus;
    }

    public void setLogAggregationStatus(String logAggregationStatus) {
        this.logAggregationStatus = logAggregationStatus;
    }

    public boolean getUnmanagedApplication() {
        return unmanagedApplication;
    }

    public void setUnmanagedApplication(boolean unmanagedApplication) {
        this.unmanagedApplication = unmanagedApplication;
    }

    public String getAppNodeLabelExpression() {
        return appNodeLabelExpression;
    }

    public void setAppNodeLabelExpression(String appNodeLabelExpression) {
        this.appNodeLabelExpression = appNodeLabelExpression;
    }

    public String getAmNodeLabelExpression() {
        return amNodeLabelExpression;
    }

    public void setAmNodeLabelExpression(String amNodeLabelExpression) {
        this.amNodeLabelExpression = amNodeLabelExpression;
    }

    public List<YarnAppAttempt> getYarnAppAttemptList() {
        return yarnAppAttemptList;
    }

    public void setYarnAppAttemptList(List<YarnAppAttempt> yarnAppAttemptList) {
        this.yarnAppAttemptList = yarnAppAttemptList;
    }

    @JsonAnyGetter
    public Map<String, Object> getUnmappedProperties() {
        return unmappedProperties;
    }

    @JsonAnySetter
    public void setUnmappedProperties(String name, Object value) {
        unmappedProperties.put(name, value);
    }

    public Map<String, Object> getFieldValues() throws IllegalAccessException {
        Map<String, Object> objectAsMap = Maps.newHashMap();
        Field[] allFields = YarnApp.class.getDeclaredFields();
        for (Field field : allFields) {
            Object value = field.get(this);
            objectAsMap.put(field.getName(), value);
        }
        objectAsMap.putAll(unmappedProperties);
        return objectAsMap;
    }

    @Override public String toString() {
        return "YarnApp{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", name='" + name + '\'' +
                ", queue='" + queue + '\'' +
                ", state='" + state + '\'' +
                ", finalStatus='" + finalStatus + '\'' +
                ", progress=" + progress +
                ", trackingUI='" + trackingUI + '\'' +
                ", trackingUrl='" + trackingUrl + '\'' +
                ", diagnostics='" + diagnostics + '\'' +
                ", clusterId=" + clusterId +
                ", applicationType='" + applicationType + '\'' +
                ", applicationTags='" + applicationTags + '\'' +
                ", priority='" + priority + '\'' +
                ", startedTime=" + startedTime +
                ", launchTime=" + launchTime +
                ", finishedTime=" + finishedTime +
                ", elapsedTime=" + elapsedTime +
                ", amContainerLogs='" + amContainerLogs + '\'' +
                ", amHostHttpAddress='" + amHostHttpAddress + '\'' +
                ", amRPCAddress='" + amRPCAddress + '\'' +
                ", allocatedMB=" + allocatedMB +
                ", allocatedVCores=" + allocatedVCores +
                ", runningContainers=" + runningContainers +
                ", memorySeconds=" + memorySeconds +
                ", vcoreSeconds=" + vcoreSeconds +
                ", queueUsagePercentage=" + queueUsagePercentage +
                ", clusterUsagePercentage=" + clusterUsagePercentage +
                ", preemptedResourcesMB=" + preemptedResourcesMB +
                ", preemptedResourceVCores=" + preemptedResourceVCores +
                ", numNonAMContainerPreempted=" + numNonAMContainerPreempted +
                ", numAmContainerPreempted=" + numAmContainerPreempted +
                ", logAggregationStatus='" + logAggregationStatus + '\'' +
                ", unmanagedApplication=" + unmanagedApplication +
                ", appNodeLabelExpression='" + appNodeLabelExpression + '\'' +
                ", amNodeLabelExpression='" + amNodeLabelExpression + '\'' +
                ", yarnAppAttemptList=" + yarnAppAttemptList +
                ", unmappedProperties=" + unmappedProperties +
                "}\n";
    }
}