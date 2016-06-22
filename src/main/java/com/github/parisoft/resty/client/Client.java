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
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.IdleConnectionEvictor;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.parisoft.resty.request.HttpRequest;
import com.github.parisoft.resty.request.Request;
import com.github.parisoft.resty.request.RequestMethod;
import com.github.parisoft.resty.request.retry.IdempotentRequestMethods;
import com.github.parisoft.resty.request.retry.RequestRetryHandler;
import com.github.parisoft.resty.request.ssl.BypassTrustStrategy;
import com.github.parisoft.resty.response.Response;
import com.github.parisoft.resty.response.ResponseFactory;

/**
 * Class that contains methods to execute a {@link Request}.<br>
 * Also, contains shortcuts to configure a request execution timeout, retries and SSL verification.
 *
 * @author Andre Paris
 *
 */
public class Client {

    private static final HttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    static {
        new IdleConnectionEvictor(connectionManager, null, 1, TimeUnit.MINUTES, 0, null).start();
    }
    
    private final Request request;
    private int timeout = 0;
    private int retries = 0;
    private boolean bypassSSL = true;

    /**
     * Creates an instance of a Client to execute a given {@link Request}.<br>
     * <br>
     *<i>Note:</i> this is equivalent to {@link Request#client()}
     *
     * @param request The request to be executed by this client
     * @throws IllegalArgumentException If the request is <code>null</code>
     */
    public Client(Request request) {
        if (request == null) {
            throw new IllegalArgumentException("Cannot create client: request cannot be null");
        }

        this.request = request;
    }

    /**
     * Sets the number of attempts that a failed request execution must be retried.<br>
     * <br>
     * Default is 0 (no retries)
     *
     * @param retries The number of requests retries
     * @return this client
     * @see RequestRetryHandler
     * @see IdempotentRequestMethods
     */
    public Client retries(int retries) {
        this.retries = retries;

        return this;
    }

    /**
     * Sets the request execution timeout.<br>
     * Under the hood this is done with these {@link HttpClient} configurations:
     * <ul>
     * <li>{@link SocketConfig#getSoTimeout()}</li>
     * <li>{@link RequestConfig#getConnectionRequestTimeout()}</li>
     * <li>{@link RequestConfig#getConnectTimeout()}</li>
     * <li>{@link RequestConfig#getSocketTimeout()}</li>
     * </ul>
     * Default is 0 (no timeout)
     *
     * @param value The timeout value
     * @param unit The timeout unit
     * @return this client
     */
    public Client timeout(int value, TimeUnit unit) {
        timeout = (int) unit.toMillis(value);

        return this;
    }

    /**
     * Sets the bypass of certificate verification process on SSL contexts.<br>
     * <br>
     * Default is true (bypass the verification)
     *
     * @param bypassSSL True to bypass the verification, false otherwise
     * @return this client
     * @see BypassTrustStrategy
     * @see NoopHostnameVerifier
     */
    public Client bypassSSL(boolean bypassSSL) {
        this.bypassSSL = bypassSSL;

        return this;
    }

    /**
     * Executes the request associated with this client as a GET.
     *
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response get() throws IOException {
        return execute(GET);
    }

    /**
     * Executes the request associated with this client as a GET.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T get(Class<T> responseClass) throws IOException {
        return get()
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a GET.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * get( new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T get(TypeReference<T> responseReference) throws IOException {
        return get()
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a HEAD.
     *
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response head() throws IOException {
        return execute(HEAD);
    }

    /**
     * Executes the request associated with this client as a HEAD.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T head(Class<T> responseClass) throws IOException {
        return head()
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a HEAD.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * head( new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T head(TypeReference<T> responseReference) throws IOException {
        return head()
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a DELETE.
     *
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response delete() throws IOException {
        return execute(DELETE);
    }

    /**
     * Executes the request associated with this client as a DELETE.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T delete(Class<T> responseClass) throws IOException {
        return delete()
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a DELETE.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * delete( new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T delete(TypeReference<T> responseReference) throws IOException {
        return delete()
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a POST.
     *
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response post() throws IOException {
        return execute(POST);
    }

    /**
     * Executes the request associated with this client as a POST.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T post(Class<T> responseClass) throws IOException {
        return post()
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a POST.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * post( new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T post(TypeReference<T> responseReference) throws IOException {
        return post()
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a POST.<br>
     * <br>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #post()}.
     *
     * @param entity The entity body to send over request
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response post(Object entity) throws IOException {
        request.entity(entity);

        return post();
    }

    /**
     * Executes the request associated with this client as a POST.<br>
     * The entity of the response is processed and converted into a given type.<br>
     * <br>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #post(Class)}.
     *
     * @param entity The entity body to send over request
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T post(Object entity, Class<T> responseClass) throws IOException {
        return post(entity)
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a POST.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * post( entity, new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #post(TypeReference)}.
     *
     * @param entity The entity body to send over request
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T post(Object entity, TypeReference<T> responseReference) throws IOException {
        return post(entity)
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a PUT.
     *
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response put() throws IOException {
        return execute(PUT);
    }

    /**
     * Executes the request associated with this client as a PUT.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T put(Class<T> responseClass) throws IOException {
        return put()
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a PUT.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * put( new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T put(TypeReference<T> responseReference) throws IOException {
        return put()
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a PUT.<br>
     * <br>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #put()}.
     *
     * @param entity The entity body to send over request
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response put(Object entity) throws IOException {
        request.entity(entity);

        return put();
    }

    /**
     * Executes the request associated with this client as a PUT.<br>
     * The entity of the response is processed and converted into a given type.<br>
     * <br>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #put(Class)}.
     *
     * @param entity The entity body to send over request
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T put(Object entity, Class<T> responseClass) throws IOException {
        return put(entity)
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a PUT.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * put( entity, new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #put(TypeReference)}.
     *
     * @param entity The entity body to send over request
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T put(Object entity, TypeReference<T> responseReference) throws IOException {
        return put(entity)
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a OPTIONS.
     *
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response options() throws IOException {
        return execute(OPTIONS);
    }

    /**
     * Executes the request associated with this client as a OPTIONS.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T options(Class<T> responseClass) throws IOException {
        return options()
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a OPTIONS.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * options( new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T options(TypeReference<T> responseReference) throws IOException {
        return options()
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a TRACE.
     *
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response trace() throws IOException {
        return execute(TRACE);
    }

    /**
     * Executes the request associated with this client as a TRACE.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T trace(Class<T> responseClass) throws IOException {
        return trace()
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a TRACE.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * trace( new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T trace(TypeReference<T> responseReference) throws IOException {
        return trace()
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a given method.
     *
     * @param requestMethod The method in which the request is executed
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     * @see RequestMethod
     */
    public Response execute(RequestMethod requestMethod) throws IOException {
        final HttpClient httpClient = toHttpClient();
        final HttpRequest httpRequest = request.toHttpRequest(requestMethod);
        final HttpResponse httpResponse = httpClient.execute(httpRequest);

        return ResponseFactory.newResponse(httpResponse);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param requestMethod The method in which the request is executed
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see RequestMethod
     */
    public <T> T execute(RequestMethod requestMethod, Class<T> responseClass) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * execute( anyMethod, new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param requestMethod The method in which the request is executed
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     * @see RequestMethod
     */
    public <T> T execute(RequestMethod requestMethod, TypeReference<T> responseReference) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * <br>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #execute(RequestMethod)}.
     *
     * @param requestMethod The method in which the request is executed
     * @param entity The entity body to send over request
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     * @see RequestMethod
     */
    public Response execute(RequestMethod requestMethod, Object entity) throws IOException {
        request.entity(entity);

        return execute(requestMethod);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * The entity of the response is processed and converted into a given type.<br>
     * <br>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #execute(RequestMethod, Class)}.
     *
     * @param requestMethod The method in which the request is executed
     * @param entity The entity body to send over request
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see RequestMethod
     */
    public <T> T execute(RequestMethod requestMethod, Object entity, Class<T> responseClass) throws IOException {
        request.entity(entity);

        return execute(requestMethod, responseClass);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * execute( anyMethod, entity, new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #execute(RequestMethod, TypeReference)}.
     *
     * @param requestMethod The method in which the request is executed
     * @param entity The entity body to send over request
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     * @see RequestMethod
     */
    public <T> T execute(RequestMethod requestMethod, Object entity, TypeReference<T> responseReference) throws IOException {
        request.entity(entity);

        return execute(requestMethod, responseReference);
    }

    /**
     * Executes the request associated with this client as a given method.
     *
     * @param requestMethod The method in which the request is executed
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response execute(String requestMethod) throws IOException {
        final HttpClient httpClient = toHttpClient();
        final HttpRequest httpRequest = request.toHttpRequest(requestMethod);
        final HttpResponse httpResponse = httpClient.execute(httpRequest);

        return ResponseFactory.newResponse(httpResponse);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * The entity of the response is processed and converted into a given type.
     *
     * @param requestMethod The method in which the request is executed
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T execute(String requestMethod, Class<T> responseClass) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseClass);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * execute( anyMethod, new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * @param requestMethod The method in which the request is executed
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T execute(String requestMethod, TypeReference<T> responseReference) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseReference);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * <br>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #execute(String)}.
     *
     * @param requestMethod The method in which the request is executed
     * @param entity The entity body to send over request
     * @return The result of the execution as a {@link Response} instance
     * @throws IOException If any problem occurs during the request
     */
    public Response execute(String requestMethod, Object entity) throws IOException {
        request.entity(entity);

        return execute(requestMethod);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * The entity of the response is processed and converted into a given type.<br>
     * <br>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #execute(String, Class)}.
     *
     * @param requestMethod The method in which the request is executed
     * @param entity The entity body to send over request
     * @param responseClass The type to convert the response entity
     * @return The response entity converted into a given type
     * @throws IOException If any problem occurs during the request or during the entity processing
     */
    public <T> T execute(String requestMethod, Object entity, Class<T> responseClass) throws IOException {
        request.entity(entity);

        return execute(requestMethod, responseClass);
    }

    /**
     * Executes the request associated with this client as a given method.<br>
     * The entity of the response is processed and converted into a given type reference.<br>
     * <br>
     * As example, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * execute( anyMethod, entity, new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     * <i>Note:</i> this is equivalent to invoke {@link Request#entity(Object)} and then {@link #execute(String, TypeReference)}.
     *
     * @param requestMethod The method in which the request is executed
     * @param entity The entity body to send over request
     * @param responseReference The type reference to convert the response entity
     * @return The response entity converted into a given type reference
     * @throws IOException If any problem occurs during the request or during the entity processing
     * @see TypeReference
     */
    public <T> T execute(String requestMethod, Object entity, TypeReference<T> responseReference) throws IOException {
        request.entity(entity);

        return execute(requestMethod, responseReference);
    }

    /**
     * Returns an {@link HttpClient} instance configured according to this client.<br>
     * <br>
     * This method is not intend to be invoked outside the RESTy library.<br>
     * For your convenience, to execute a request associated with this client, choose the proper method like {@link #get()}, {@link #post()}, or other.
     *
     * @return A configured instance of {@link HttpClient}
     * @throws IOException If some problem occurs during the HttpClient configuration
     */
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
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .setRetryHandler(retryHandler)
                .setDefaultSocketConfig(socketConfig)
                .setDefaultRequestConfig(requestConfig)
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(hostnameVerifier)
                .build();
    }
    
}
