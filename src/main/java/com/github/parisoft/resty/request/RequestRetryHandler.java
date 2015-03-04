package com.github.parisoft.resty.request;

import java.io.IOException;
import java.net.SocketException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

public class RequestRetryHandler implements HttpRequestRetryHandler {

    private final int maxRetries;

    public RequestRetryHandler(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        final boolean isRetriesOver = executionCount >= maxRetries;

        if (isRetriesOver) {
            return false;
        }

        final boolean isUsusalSocketProblem = exception instanceof SocketException;

        if (isUsusalSocketProblem) {
            return true;
        }

        final boolean isDroppedByServer = exception instanceof NoHttpResponseException;

        if (isDroppedByServer) {
            return true;
        }

        final boolean isHandshakeProblem = exception instanceof SSLHandshakeException;

        if (isHandshakeProblem) {
            return false;
        }

        final HttpRequest request = (HttpRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST);
        final boolean isRequestIdempotent = !(request instanceof HttpEntityEnclosingRequest);

        if (isRequestIdempotent) {
            return true;
        }

        return false;
    }
}