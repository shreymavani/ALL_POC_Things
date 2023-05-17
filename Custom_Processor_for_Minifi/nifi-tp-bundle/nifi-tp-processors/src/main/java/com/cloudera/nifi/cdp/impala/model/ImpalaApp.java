package com.cloudera.nifi.cdp.impala.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ImpalaApp implements Serializable {
        private static final long serialVersionUID = 2142301016370252402L;

        @JsonProperty("in_flight_queries")
        public List<InFlightQueries> in_flight_queries;

        @JsonProperty("completed_queries")
        public List<InFlightQueries> completed_queries;
        @JsonProperty("num_executing_queries")
        private int num_executing_queries;
        private long completed_log_size;
        private final Map<String, Object> unmappedProperties =
                Maps.newHashMap();

        public List<InFlightQueries> getInFlightQueries() { return in_flight_queries; }

        public void setInFlightQueries(List<InFlightQueries> in_flight_queries) { this.in_flight_queries = in_flight_queries; }

        public long getcompleted_log_size() { return completed_log_size; }

        public void setcompleted_log_size(long completed_log_size) { this.completed_log_size = completed_log_size; }

        public List<InFlightQueries> getcompleted_queries() { return completed_queries; }

        public void setcompleted_queries(List<InFlightQueries> completed_queries) { this.completed_queries = completed_queries; }
        public int getNum_Executing_Queries(){return num_executing_queries; }
        public void setNum_Executing_Queries(int num_executing_queries)
        {
            this.num_executing_queries = num_executing_queries;
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
            Field[] allFields = ImpalaApp.class.getDeclaredFields();
            for (Field field : allFields) {
                Object value = field.get(this);
                objectAsMap.put(field.getName(), value);
            }
            objectAsMap.putAll(unmappedProperties);
            return objectAsMap;
        }

        @Override public String toString() {
            return
                    "InFlightQueries'" + in_flight_queries.toString() + '\n' +
                    "CompletedQueries'" + completed_queries.toString() + '\n' +
                    "completed_log_size'" +completed_log_size+
                    "num_executing_queries'" +num_executing_queries+
                            '}';
        }
    }