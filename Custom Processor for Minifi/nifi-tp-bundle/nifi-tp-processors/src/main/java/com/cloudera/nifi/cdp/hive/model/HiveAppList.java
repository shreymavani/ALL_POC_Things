package com.cloudera.nifi.cdp.hive.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HiveAppList {
    @JsonProperty("app")
    public List<HiveApp> appList;

    protected void setApps(HiveAppList jobs) {
        this.appList = jobs != null ? jobs.appList : null;
    }

    @Override public String toString() {
        return "HiveAppList{" +
                "appList=" + appList +
                '}';
    }
}
