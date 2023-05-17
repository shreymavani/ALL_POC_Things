package com.cloudera.nifi.cdp.impala.model;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
public class InFlightQueries implements Serializable{

    private static final long serialVersionUID = 2142301016370252412L;
    private String effective_user;
    private String default_db;
    private String stmt;
    private String stmt_type;
    private String start_time;
    private String end_time;
    private String duration;
    private String progress;
    private String state;
    private int rows_fetched;
    private String query_id;
    private String last_event;
    private Boolean waiting;
    private Boolean executing;
    private String waiting_time;
    private String resource_pool;

    public String getEffective_user() {
        return effective_user;
    }

    public void setEffective_user(String effective_user) {
        this.effective_user = effective_user;
    }

    public String getDefault_db() {
        return default_db;
    }

    public void setDefault_db(String default_db) {
        this.default_db = default_db;
    }

    public String getStmt() {
        return stmt;
    }

    public void setStmt(String stmt) {
        this.stmt = stmt;
    }

    public String getStmt_type() {
        return stmt_type;
    }

    public void setStmt_type(String stmt_type) {
        this.stmt_type = stmt_type;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String Start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getProgress() { return progress; }

    public void setProgress(String trackingUI) { this.progress = progress; }

    public String getState() {
        return state;
    }

    public void setState(String State) {
        this.state = State;
    }


    public int getRows_fetched() {
        return rows_fetched;
    }

    public void setRows_fetched(int rows_fetched) {
        this.rows_fetched = rows_fetched;
    }

    public String getQuery_id() {
        return query_id;
    }

    public void setQuery_id(String query_id) {
        this.query_id = query_id;
    }

    public String getLast_event() {
        return last_event;
    }

    public void setLast_event(String last_event) {
        this.last_event = last_event;
    }

    public Boolean getWaiting() {
        return waiting;
    }

    public void setWaiting(Boolean waiting) {
        this.waiting = waiting;
    }

    public Boolean getExecuting() {
        return executing;
    }

    public void setExecuting(Boolean executing) {
        this.executing = executing;
    }

    public String getWaiting_time() { return waiting_time; }

    public void setWaiting_time(String waiting_time) { this.waiting_time = waiting_time; }

    public String getResource_pool() { return resource_pool; }

    public void setResource_pool(String resource_pool) { this.resource_pool = resource_pool; }


    @Override public String toString() {
        return "ImpalaApp{" +
                "effective_user'" + effective_user + '\'' +
                "default_db'" + default_db + '\'' +
                "stmt'" + stmt + '\'' +
                "stmt_type'" + stmt_type + '\'' +
                "start_time'" + start_time + '\'' +
                "end_time'" + end_time + '\'' +
                "duration'" + duration + '\'' +
                "progress'" + progress + '\'' +
                "state'" + state + '\'' +
                "rows_fetched'" + rows_fetched + '\'' +
                "query_id'" + query_id + '\'' +
                "last_event'" + last_event + '\'' +
                "waiting'" + waiting + '\'' +
                "executing'" + executing + '\'' +
                "waiting_time'" + waiting_time + '\'' +
                "resource_pool'" + resource_pool + '\'' +
//                    "InFlightQueries'" + in_flight_queries + '\'' +
//                "completed_log_size'" +completed_log_size+
                '}';
    }
}
