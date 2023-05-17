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
import java.util.Objects;

/**
 * Reply for the transmission of an individual record.
 **/
@javax.annotation.Generated(value = "com.cloudera.cdp.client.codegen.CdpSDKJavaCodegen", date = "2022-12-02T15:21:34.961+05:30")
public class RecordReply  {

  /**
   * Unique identifier for the record.
   **/
  private String recordId = null;

  /**
   * The status of the record.
   **/
  private String status = null;

  /**
   * Optional url to HTTP PUT the actual payload.
   **/
  private String uploadUrl = null;

  /**
   * Getter for recordId.
   * Unique identifier for the record.
   **/
  @JsonProperty("recordId")
  public String getRecordId() {
    return recordId;
  }

  /**
   * Setter for recordId.
   * Unique identifier for the record.
   **/
  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  /**
   * Getter for status.
   * The status of the record.
   **/
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  /**
   * Setter for status.
   * The status of the record.
   **/
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Getter for uploadUrl.
   * Optional url to HTTP PUT the actual payload.
   **/
  @JsonProperty("uploadUrl")
  public String getUploadUrl() {
    return uploadUrl;
  }

  /**
   * Setter for uploadUrl.
   * Optional url to HTTP PUT the actual payload.
   **/
  public void setUploadUrl(String uploadUrl) {
    this.uploadUrl = uploadUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecordReply recordReply = (RecordReply) o;
    if (!Objects.equals(this.recordId, recordReply.recordId)) {
      return false;
    }
    if (!Objects.equals(this.status, recordReply.status)) {
      return false;
    }
    if (!Objects.equals(this.uploadUrl, recordReply.uploadUrl)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordId, status, uploadUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecordReply {\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    uploadUrl: ").append(toIndentedString(uploadUrl)).append("\n");
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

