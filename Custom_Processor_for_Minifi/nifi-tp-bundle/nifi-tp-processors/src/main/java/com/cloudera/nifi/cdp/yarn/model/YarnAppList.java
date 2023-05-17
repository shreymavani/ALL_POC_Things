// (c) Copyright 2018 Cloudera, Inc. All rights reserved.
package com.cloudera.nifi.cdp.yarn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class YarnAppList {

    @JsonProperty("app")
    public List<YarnApp> appList;

    protected void setApps(YarnAppList jobs) {
        this.appList = jobs != null ? jobs.appList : null;
    }

    @Override public String toString() {
        return "YarnAppList{" +
                "appList=" + appList +
                "}\n";
    }
}