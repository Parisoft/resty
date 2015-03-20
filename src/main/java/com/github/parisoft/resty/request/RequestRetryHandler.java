package com.github.parisoft.resty.request;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

public class RequestRetryHandler implements HttpRequestRetryHandler {

    public static final List<String> IDEMPOTENT_METHODS = new ArrayList<>();
    static {
        IDEMPOTENT_METHODS.add("GET");
        IDEMPOTENT_METHODS.add("HEAD");
        IDEMPOTENT_METHODS.add("TRACE");
        IDEMPOTENT_METHODS.add("OPTIONS");
    }

    private final int maxRetries;

    public RequestRetryHandler(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (isRetriesOver(executionCount)) {
            return false;
        }

        if (isUsualSocketProblem(exception)) {
            return true;
        }

        if (isDroppedByServer(exception)) {
            return true;
        }

        if (isHandshake(exception)) {
            return false;
        }

        if (isRequestIdempotent(context)) {
            return true;
        }

        return false;
    }

    private boolean isRetriesOver(int executionCount) {
        return executionCount > maxRetries;
    }

    private boolean isUsualSocketProblem(IOException exception) {
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
        return IDEMPOTENT_METHODS.contains(request.getMethod());
    }
}