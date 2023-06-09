/*
 * Copyright (c) 2020 Cloudera, Inc. All Rights Reserved.
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

package com.cloudera.nifi.cdp.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;

public abstract class BaseResponse {
  private Integer httpCode = null;
  private Map<String, List<String>> responseHeaders = null;

  /**
   * Gets the http code that was returned by the CDP server.
   * @return the http code
   */
  public int getHttpCode() {
    checkNotNull(httpCode);
    return httpCode;
  }

  /**
   * Sets the http code that was returned by the CDP server. This
   * should be called only once and never outside the SDK internals.
   * @param httpCode the status code
   */
  void setHttpCode(int httpCode) {
    checkArgument(this.httpCode == null);
    this.httpCode = httpCode;
  }

  /**
   * Gets the http response headers that were returned by the CDP server.
   * @return the response headers
   */
  public Map<String, List<String>> getResponseHeaders() {
    checkNotNull(responseHeaders);
    return responseHeaders;
  }

  /**
   * Sets the http response headers that were returned by the CDP server.
   * This should be called only once and never outside the SDK internals.
   * @param responseHeaders the response headers
   */
  void setResponseHeaders(Map<String, List<String>> responseHeaders) {
    checkArgument(this.responseHeaders == null);
    this.responseHeaders = responseHeaders;
  }
}
