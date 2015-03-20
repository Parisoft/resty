package com.github.parisoft.resty.client;

import static com.github.parisoft.resty.request.RequestMethod.DELETE;
import static com.github.parisoft.resty.request.RequestMethod.GET;
import static com.github.parisoft.resty.request.RequestMethod.HEAD;
import static com.github.parisoft.resty.request.RequestMethod.OPTIONS;
import static com.github.parisoft.resty.request.RequestMethod.POST;
import static com.github.parisoft.resty.request.RequestMethod.PUT;
import static com.github.parisoft.resty.request.RequestMethod.TRACE;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.parisoft.resty.request.HttpRequest;
import com.github.parisoft.resty.request.Request;
import com.github.parisoft.resty.request.RequestMethod;
import com.github.parisoft.resty.request.retry.RequestRetryHandler;
import com.github.parisoft.resty.request.ssl.BypassTrustStrategy;
import com.github.parisoft.resty.response.Response;
import com.github.parisoft.resty.response.ResponseFactory;

public class Client {

    private final Request request;
    private int timeout = 0;
    private int retries = 0;
    private boolean bypassSSL = true;

    public Client(Request request) {
        this.request = request;
    }

    public Client retries(int retries) {
        this.retries = retries;

        return this;
    }

    public Client timeout(int value, TimeUnit unit) {
        timeout = (int) unit.toMillis(value);

        return this;
    }

    public Client bypassSSL(boolean bypassSSL) {
        this.bypassSSL = bypassSSL;

        return this;
    }


    public Response get() throws IOException {
        return execute(GET);
    }

    public <T> T get(Class<T> responseClass) throws IOException {
        return get()
                .getEntityAs(responseClass);
    }

    public <T> T get(TypeReference<T> responseReference) throws IOException {
        return get()
                .getEntityAs(responseReference);
    }

    public Response head() throws IOException {
        return execute(HEAD);
    }

    public <T> T head(Class<T> responseClass) throws IOException {
        return head()
                .getEntityAs(responseClass);
    }

    public <T> T head(TypeReference<T> responseReference) throws IOException {
        return head()
                .getEntityAs(responseReference);
    }


    public Response delete() throws IOException {
        return execute(DELETE);
    }

    public <T> T delete(Class<T> responseClass) throws IOException {
        return delete()
                .getEntityAs(responseClass);
    }

    public <T> T delete(TypeReference<T> responseReference) throws IOException {
        return delete()
                .getEntityAs(responseReference);
    }

    public Response post() throws IOException {
        return execute(POST);
    }

    public <T> T post(Class<T> responseClass) throws IOException {
        return post()
                .getEntityAs(responseClass);
    }

    public <T> T post(TypeReference<T> responseReference) throws IOException {
        return post()
                .getEntityAs(responseReference);
    }

    public Response post(Object entity) throws IOException {
        request.entity(entity);

        return post();
    }

    public <T> T post(Object entity, Class<T> responseClass) throws IOException {
        return post(entity)
                .getEntityAs(responseClass);
    }

    public <T> T post(Object entity, TypeReference<T> responseReference) throws IOException {
        return post(entity)
                .getEntityAs(responseReference);
    }

    public Response put() throws IOException {
        return execute(PUT);
    }

    public <T> T put(Class<T> responseClass) throws IOException {
        return put()
                .getEntityAs(responseClass);
    }

    public <T> T put(TypeReference<T> responseReference) throws IOException {
        return put()
                .getEntityAs(responseReference);
    }

    public Response put(Object entity) throws IOException {
        request.entity(entity);

        return put();
    }

    public <T> T put(Object entity, Class<T> responseClass) throws IOException {
        return put(entity)
                .getEntityAs(responseClass);
    }

    public <T> T put(Object entity, TypeReference<T> responseReference) throws IOException {
        return put(entity)
                .getEntityAs(responseReference);
    }

    public Response options() throws IOException {
        return execute(OPTIONS);
    }

    public <T> T options(Class<T> responseClass) throws IOException {
        return options()
                .getEntityAs(responseClass);
    }

    public <T> T options(TypeReference<T> responseReference) throws IOException {
        return options()
                .getEntityAs(responseReference);
    }

    public Response trace() throws IOException {
        return execute(TRACE);
    }

    public <T> T trace(Class<T> responseClass) throws IOException {
        return trace()
                .getEntityAs(responseClass);
    }

    public <T> T trace(TypeReference<T> responseReference) throws IOException {
        return trace()
                .getEntityAs(responseReference);
    }

    public Response execute(RequestMethod requestMethod) throws IOException {
        final HttpClient httpClient = toHttpClient();
        final HttpRequest httpRequest = request.toHttpRequest(requestMethod);
        final HttpResponse httpResponse = httpClient.execute(httpRequest);

        return ResponseFactory.newResponse(httpResponse);
    }

    public <T> T execute(RequestMethod requestMethod, Class<T> responseClass) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseClass);
    }

    public <T> T execute(RequestMethod requestMethod, TypeReference<T> responseReference) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseReference);
    }

    public Response execute(RequestMethod requestMethod, Object entity) throws IOException {
        request.entity(entity);

        return execute(requestMethod);
    }

    public <T> T execute(RequestMethod requestMethod, Object entity, Class<T> responseClass) throws IOException {
        request.entity(entity);

        return execute(requestMethod, responseClass);
    }

    public <T> T execute(RequestMethod requestMethod, Object entity, TypeReference<T> responseReference) throws IOException {
        request.entity(entity);

        return execute(requestMethod, responseReference);
    }


    public Response execute(String requestMethod) throws IOException {
        final HttpClient httpClient = toHttpClient();
        final HttpRequest httpRequest = request.toHttpRequest(requestMethod);
        final HttpResponse httpResponse = httpClient.execute(httpRequest);

        return ResponseFactory.newResponse(httpResponse);
    }

    public <T> T execute(String requestMethod, Class<T> responseClass) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseClass);
    }

    public <T> T execute(String requestMethod, TypeReference<T> responseReference) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseReference);
    }

    public Response execute(String requestMethod, Object entity) throws IOException {
        request.entity(entity);

        return execute(requestMethod);
    }

    public <T> T execute(String requestMethod, Object entity, Class<T> responseClass) throws IOException {
        request.entity(entity);

        return execute(requestMethod, responseClass);
    }

    public <T> T execute(String requestMethod, Object entity, TypeReference<T> responseReference) throws IOException {
        request.entity(entity);

        return execute(requestMethod, responseReference);
    }

    public HttpClient toHttpClient() throws IOException {
        final SocketConfig socketConfig = SocketConfig
                .custom()
                .setSoTimeout(timeout)
                .build();

        final RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();

        final SSLContext sslContext;
        final HostnameVerifier hostnameVerifier;

        if (bypassSSL) {
            hostnameVerifier = NoopHostnameVerifier.INSTANCE;

            try {
                sslContext = SSLContexts
                        .custom()
                        .loadTrustMaterial(new BypassTrustStrategy())
                        .useProtocol(SSLConnectionSocketFactory.TLS)
                        .build();
            } catch (Exception e) {
                throw new IOException("Cannot create bypassed SSL context", e);
            }
        } else {
            sslContext = SSLContexts.createSystemDefault();
            hostnameVerifier = null;
        }

        final HttpRequestRetryHandler retryHandler = new RequestRetryHandler(retries);

        return HttpClientBuilder
                .create()
                .setRetryHandler(retryHandler)
                .setDefaultSocketConfig(socketConfig)
                .setDefaultRequestConfig(requestConfig)
                .setSslcontext(sslContext)
                .setSSLHostnameVerifier(hostnameVerifier)
                .build();
    }
}
