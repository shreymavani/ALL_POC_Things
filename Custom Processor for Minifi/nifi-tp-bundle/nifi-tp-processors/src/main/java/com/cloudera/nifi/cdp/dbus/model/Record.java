/*
 * Copyright (c) 2018 Cloudera, Inc. All Rights Reserved.
 *
 * Portions Copyright (c) Copyright 2013-2018 Amazon.com, Inc. or its
 * affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.nifi.cdp.dbus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents small unit of data going into a stream.
 **/
@javax.annotation.Generated(value = "com.cloudera.cdp.client.codegen.CdpSDKJavaCodegen", date = "2022-09-13T16:49:45.717+05:30")
public class Record  {

  /**
   * Name of the target DataStream.
   **/
  private String streamName = null;

  /**
   * Partition key. Variance should be high enough to ensure even distribution.
   **/
  private String partitionKey = null;

  /**
   * Expected size of the payload in bytes, whether inlined or to be sent out of band.
   **/
  private Long payloadSize = null;

  /**
   * Arbitrary metadata that will be made available to subscribers of the message.
   **/
  private List<Header> headers = new ArrayList<Header>();

  /**
   * Optional inlined payload encoded in base64 format. Decoded size cannot exceed 1MB.
   **/
  private String payload = null;

  /**
   * Getter for streamName.
   * Name of the target DataStream.
   **/
  @JsonProperty("streamName")
  public String getStreamName() {
    return streamName;
  }

  /**
   * Setter for streamName.
   * Name of the target DataStream.
   **/
  public void setStreamName(String streamName) {
    this.streamName = streamName;
  }

  /**
   * Getter for partitionKey.
   * Partition key. Variance should be high enough to ensure even distribution.
   **/
  @JsonProperty("partitionKey")
  public String getPartitionKey() {
    return partitionKey;
  }

  /**
   * Setter for partitionKey.
   * Partition key. Variance should be high enough to ensure even distribution.
   **/
  public void setPartitionKey(String partitionKey) {
    this.partitionKey = partitionKey;
  }

  /**
   * Getter for payloadSize.
   * Expected size of the payload in bytes, whether inlined or to be sent out of band.
   **/
  @JsonProperty("payloadSize")
  public Long getPayloadSize() {
    return payloadSize;
  }

  /**
   * Setter for payloadSize.
   * Expected size of the payload in bytes, whether inlined or to be sent out of band.
   **/
  public void setPayloadSize(Long payloadSize) {
    this.payloadSize = payloadSize;
  }

  /**
   * Getter for headers.
   * Arbitrary metadata that will be made available to subscribers of the message.
   **/
  @JsonProperty("headers")
  public List<Header> getHeaders() {
    return headers;
  }

  /**
   * Setter for headers.
   * Arbitrary metadata that will be made available to subscribers of the message.
   **/
  public void setHeaders(List<Header> headers) {
    this.headers = headers;
  }

  /**
   * Getter for payload.
   * Optional inlined payload encoded in base64 format. Decoded size cannot exceed 1MB.
   **/
  @JsonProperty("payload")
  public String getPayload() {
    return payload;
  }

  /**
   * Setter for payload.
   * Optional inlined payload encoded in base64 format. Decoded size cannot exceed 1MB.
   **/
  public void setPayload(String payload) {
    this.payload = payload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Record record = (Record) o;
    if (!Objects.equals(this.streamName, record.streamName)) {
      return false;
    }
    if (!Objects.equals(this.partitionKey, record.partitionKey)) {
      return false;
    }
    if (!Objects.equals(this.payloadSize, record.payloadSize)) {
      return false;
    }
    if (!Objects.equals(this.headers, record.headers)) {
      return false;
    }
    if (!Objects.equals(this.payload, record.payload)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(streamName, partitionKey, payloadSize, headers, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Record {\n");
    sb.append("    streamName: ").append(toIndentedString(streamName)).append("\n");
    sb.append("    partitionKey: ").append(toIndentedString(partitionKey)).append("\n");
    sb.append("    payloadSize: ").append(toIndentedString(payloadSize)).append("\n");
    sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line except the first indented by 4 spaces.
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

