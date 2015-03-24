/*
 *    Copyright 2013-2014 Parisoft Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.parisoft.resty.request.retry;

import java.io.IOException;
import java.net.SocketException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

import com.github.parisoft.resty.client.Client;

/**
 * Thread-safe implementation of {@link HttpRequestRetryHandler}.<br>
 * <br>
 * A request retry don't occurs when:
 * <ul>
 * <li>The requests attempts reached this maximum retries counter</li>
 * <li>The client and server could not negotiate the desired level of security</li>
 * </ul>
 * A request retry occurs when:
 * <ul>
 * <li>There is an error creating or accessing a Socket</li>
 * <li>The connection is dropped by the remote server</li>
 * <li>None of above and the request is Idempotent</li>
 * </ul>
 *
 * @author Andre Paris
 * @see IdempotentRequestMethods
 * @see Client#retries(int)
 */
public class RequestRetryHandler implements HttpRequestRetryHandler {

    private final int maxRetries;

    /**
     * Creates a retry handler with a maximum retries counter.
     *
     * @param maxRetries The maximum retries counter
     */
    public RequestRetryHandler(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (isRetriesOver(executionCount) || isHandshake(exception)) {
            return false;
        }

        if (isSocket(exception) || isDroppedByServer(exception)) {
            return true;
        }

        return isRequestIdempotent(context);
    }

    private boolean isRetriesOver(int executionCount) {
        return executionCount > maxRetries;
    }

    private boolean isSocket(IOException exception) {
        return exception instanceof SocketException;
    }

    private boolean isDroppedByServer(IOException exception) {
        return exception instanceof NoHttpResponseException;
    }

    private boolean isHandshake(IOException exception) {
        return exception instanceof SSLHandshakeException;
    }

    private boolean isRequestIdempotent(HttpContext context) {
        final HttpUriRequest request = (HttpUriRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST);
        return IdempotentRequestMethods.getInstance().contains(request.getMethod());
    }
}