package com.cloudera.nifi.cdp.hive.model;


import com.cloudera.nifi.cdp.impala.model.ImpalaApp;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class HiveApp implements Serializable {
    private static final long serialVersionUID = 2142301016370252404L;
    public static class HiveAppAttempt implements Serializable {
        private static final long serialVersionUID = 8899073478059991910L;

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
            return "ImpalaAppAttempt{" +
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

    private String userName;
    private String executionEngine;
    private long beginTime;

    private String operationId;
    private long Runtime;
    private String endTime;
    private String state;
    private String queryDisplay;
    private String operationLogLocation;
    private long elapsedTime;
    private String Running;

    //        private long finishedTime;
//        private long elapsedTime;
//        private String amContainerLogs;
//        private String amHostHttpAddress;
//        private String amRPCAddress;
//        private int allocatedMB;
//        private int allocatedVCores;
//        private int runningContainers;
//        private long memorySeconds;
//        private long vcoreSeconds;
//        private float queueUsagePercentage;
//        private float clusterUsagePercentage;
//        private long preemptedResourcesMB;
//        private long preemptedResourceVCores;
//        private int numNonAMContainerPreempted;
//        private int numAmContainerPreempted;
//        private String logAggregationStatus;
//        private boolean unmanagedApplication;
//        private String appNodeLabelExpression;
//        private String amNodeLabelExpression;
    private List<HiveApp.HiveAppAttempt> ImpalaAppAttemptList;
    private final Map<String, Object> unmappedProperties =
            Maps.newHashMap();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExecutionEngine() {
        return executionEngine;
    }

    public void setExecutionEngine(String executionEngine) {
        this.executionEngine = executionEngine;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public long getRuntime() {
        return Runtime;
    }

    public void setRuntime(long Runtime) {
        this.Runtime = Runtime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getQueryDisplay() { return queryDisplay; }

    public void setQueryDisplay(String queryDisplay) { this.queryDisplay = queryDisplay; }

    public String getOperationLogLocation() {
        return operationLogLocation;
    }

    public void setOperationLogLocation(String operationLogLocation) {
        this.operationLogLocation = operationLogLocation;
    }


    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getRunning() {
        return Running;
    }

    public void setRunning(String Running) {
        this.Running = Running;
    }
//
//    public String getLast_event() {
//        return last_event;
//    }
//
//    public void setLast_event(String last_event) {
//        this.last_event = last_event;
//    }
//
//    public Boolean getWaiting() {
//        return waiting;
//    }
//
//    public void setWaiting(Boolean waiting) {
//        this.waiting = waiting;
//    }
//
//    public boolean getExecuting() {
//        return executing;
//    }
//
//    public void setExecuting(boolean executing) {
//        this.executing = executing;
//    }
//
//    public String getWaiting_time() { return waiting_time; }
//
//    public void setWaiting_time(String waiting_time) { this.waiting_time = waiting_time; }
//
//    public String getResource_pool() { return resource_pool; }
//
//    public void setResource_pool(String resource_pool) { this.resource_pool = resource_pool; }
//
//        public Long getFinishedTime() {
//            return finishedTime;
//        }
//
//        public void setFinishedTime(Long finishedTime) {
//            this.finishedTime = finishedTime;
//        }
//
//        public Long getElapsedTime() {
//            return elapsedTime;
//        }
//
//        public void setElapsedTime(Long elapsedTime) {
//            this.elapsedTime = elapsedTime;
//        }
//
//        public String getAmContainerLogs() {
//            return amContainerLogs;
//        }
//
//        public void setAmContainerLogs(String amContainerLogs) {
//            this.amContainerLogs = amContainerLogs;
//        }
//
//        public String getAmHostHttpAddress() {
//            return amHostHttpAddress;
//        }
//
//        public void setAmHostHttpAddress(String amHostHttpAddress) {
//            this.amHostHttpAddress = amHostHttpAddress;
//        }
//
//        public String getAmRPCAddress() {
//            return amRPCAddress;
//        }
//
//        public void setAmRPCAddress(String amRPCAddress) {
//            this.amRPCAddress = amRPCAddress;
//        }
//
//        public int getAllocatedMB() {
//            return allocatedMB;
//        }
//
//        public void setAllocatedMB(int allocatedMB) {
//            this.allocatedMB = allocatedMB;
//        }
//
//        public int getAllocatedVCores() {
//            return allocatedVCores;
//        }
//
//        public void setAllocatedVCores(int allocatedVCores) {
//            this.allocatedVCores = allocatedVCores;
//        }
//
//        public int getRunningContainers() {
//            return runningContainers;
//        }
//
//        public void setRunningContainers(int runningContainers) {
//            this.runningContainers = runningContainers;
//        }
//
//        public long getMemorySeconds() {
//            return memorySeconds;
//        }
//
//        public void setMemorySeconds(long memorySeconds) {
//            this.memorySeconds = memorySeconds;
//        }
//
//        public long getVcoreSeconds() {
//            return vcoreSeconds;
//        }
//
//        public void setVcoreSeconds(long vcoreSeconds) {
//            this.vcoreSeconds = vcoreSeconds;
//        }
//
//        public float getQueueUsagePercentage() {
//            return queueUsagePercentage;
//        }
//
//        public void setQueueUsagePercentage(float queueUsagePercentage) {
//            this.queueUsagePercentage = queueUsagePercentage;
//        }
//
//        public float getClusterUsagePercentage() {
//            return clusterUsagePercentage;
//        }
//
//        public void setClusterUsagePercentage(float clusterUsagePercentage) {
//            this.clusterUsagePercentage = clusterUsagePercentage;
//        }
//
//        public long getPreemptedResourceMB() {
//            return preemptedResourcesMB;
//        }
//
//        public void setPreemptedResourceMB(long preemptedResourcesMB) {
//            this.preemptedResourcesMB = preemptedResourcesMB;
//        }
//
//        public long getPreemptedResourceVCores() {
//            return preemptedResourceVCores;
//        }
//
//        public void setPreemptedResourceVCores(long preemptedResourceVCores) {
//            this.preemptedResourceVCores = preemptedResourceVCores;
//        }
//
//        public int getNumNonAMContainerPreempted() {
//            return numNonAMContainerPreempted;
//        }
//
//        public void setNumNonAMContainerPreempted(int numNonAMContainerPreempted) {
//            this.numNonAMContainerPreempted = numNonAMContainerPreempted;
//        }
//
//        public int getNumAMContainerPreempted() {
//            return numAmContainerPreempted;
//        }
//
//        public void setNumAMContainerPreempted(int numAMContainerPreempted) {
//            this.numAmContainerPreempted = numAMContainerPreempted;
//        }
//
//        public String getLogAggregationStatus() {
//            return logAggregationStatus;
//        }
//
//        public void setLogAggregationStatus(String logAggregationStatus) {
//            this.logAggregationStatus = logAggregationStatus;
//        }
//
//        public boolean getUnmanagedApplication() {
//            return unmanagedApplication;
//        }
//
//        public void setUnmanagedApplication(boolean unmanagedApplication) {
//            this.unmanagedApplication = unmanagedApplication;
//        }
//
//        public String getAppNodeLabelExpression() {
//            return appNodeLabelExpression;
//        }
//
//        public void setAppNodeLabelExpression(String appNodeLabelExpression) {
//            this.appNodeLabelExpression = appNodeLabelExpression;
//        }
//
//        public String getAmNodeLabelExpression() {
//            return amNodeLabelExpression;
//        }
//
//        public void setAmNodeLabelExpression(String amNodeLabelExpression) {
//            this.amNodeLabelExpression = amNodeLabelExpression;
//        }
//
//        public List<ImpalaApp.ImpalaAppAttempt> getYarnAppAttemptList() {
//            return yarnAppAttemptList;
//        }
//
//        public void setYarnAppAttemptList(List< ImpalaApp.ImpalaAppAttempt > yarnAppAttemptList) {
//            this.yarnAppAttemptList = yarnAppAttemptList;
//        }

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
        Field[] allFields = ImpalaApp.class.getDeclaredFields();
        for (Field field : allFields) {
            Object value = field.get(this);
            objectAsMap.put(field.getName(), value);
        }
        objectAsMap.putAll(unmappedProperties);
        return objectAsMap;
    }

    @Override public String toString() {
        return "HiveApp{" +
                "userName'" + userName + '\'' +
                "executionEngine'" + executionEngine + '\'' +
                "beginTime'" + beginTime + '\'' +
                "operationId'" + operationId + '\'' +
                "Runtime'" + Runtime + '\'' +
                "endTime'" + endTime + '\'' +
                "state'" + state + '\'' +
                "queryDisplay'" + queryDisplay+ '\'' +
                "operationLogLocation'" + operationLogLocation + '\'' +
                "elapsedTime'" + elapsedTime + '\'' +
                "Running'" + Running + '\'' +
                '}';
    }
}