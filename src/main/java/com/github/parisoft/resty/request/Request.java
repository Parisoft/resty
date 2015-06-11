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
package com.github.parisoft.resty.request;

import static com.github.parisoft.resty.utils.ArrayUtils.isEmpty;
import static com.github.parisoft.resty.utils.StringUtils.emptyIfNull;
import static com.github.parisoft.resty.utils.StringUtils.splitAfterSlashes;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.github.parisoft.resty.RESTy;
import com.github.parisoft.resty.client.Client;
import com.github.parisoft.resty.utils.CookieUtils;

/**
 * Class that contains methods to configure, create and execute an HTTP request.
 *
 * @author Andre Paris
 *
 */
public class Request {

    private static final String NULL_VALUE = null;

    private MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
    private List<NameValuePair> queries = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private Object entity;
    URI rootUri;

    /**
     * Creates a request from a URI string.<br>
     * The URI must conform the <a href=https://www.ietf.org/rfc/rfc2396.txt>RFC 2396</a> with the <i>path</i> and <i>query</i> properly encoded.<br>
     * <br>
     * For your convenience, call this constructor with the base address of your request - like {@literal <scheme>://<authority>} -
     * and use {@link #path(String...)} and {@link #query(String, String)} to configure the URI path and query respectively without worring about escape.<br>
     * <br>
     * As example, you can do<br>
     * <pre>
     * Request request = new Request("http://some.domain.org:1234")
     *                       .path("any unescaped path")
     *                       .query("don't", "scape too");
     * </pre>
     * or
     * <pre>
     * Request request = new Request(http://some.domain.org:1234/any%20unescaped%20path?don%27t=scape+too);
     * </pre>
     * <i>Note:</i> this is equivalent to {@link RESTy#request(String)}
     *
     * @param uri The string to be parsed into a URI
     * @throws IllegalArgumentException If the URI string is null or violates RFC 2396
     */
    public Request(String uri) {
        this(stringToUri(uri));
    }

    /**
     * Creates a request from a {@link URI}.<br>
     * The URI must conform the <a href=https://www.ietf.org/rfc/rfc2396.txt>RFC 2396</a> with the <i>path</i> and <i>query</i> properly encoded.<br>
     * <br>
     * For your convenience, call this constructor with the base address of your request - like {@literal <scheme>://<authority>} -
     * and use {@link #path(String...)} and {@link #query(String, String)} to configure the URI path and query respectively without worring about escape.<br>
     * <br>
     * As example, you can do<br>
     * <pre>
     * URI partialUri = new URI("http://some.domain.org:1234");
     * Request request = new Request(partialUri)
     *                       .path("any unescaped path")
     *                       .query("don't", "scape too");
     * </pre>
     * or
     * <pre>
     * URI fullUri = new URI("http://some.domain.org:1234/any%20unescaped%20path?don%27t=scape+too");
     * Request request = new Request(fullUri);
     * </pre>
     * <i>Note:</i> this is equivalent to {@link RESTy#request(URI)}
     *
     * @param uri A {@link URI} as defined by <a href=https://www.ietf.org/rfc/rfc2396.txt>RFC 2396</a>
     * @throws IllegalArgumentException If the URI is null
     */
    public Request(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Cannot create a request: URI cannot be null");
        }

        path(emptyIfNull(uri.getPath()));

        try {
            query(URLEncodedUtils.parse(uri, UTF_8.name()));
            rootUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, uri.getFragment());
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create a request: " + e.getMessage());
        }
    }

    private static URI stringToUri(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Cannot create a request: URI cannot be null");
        }

        return URI.create(string);
    }

    /**
     * @return The headers configured on this request
     */
    public MultivaluedMap<String, String> headers() {
        return headers;
    }

    /**
     * @return The paths configured on this request
     */
    public List<String> paths() {
        if (!this.paths.isEmpty()) {
            final List<String> normalized = new ArrayList<String>();

            for (int i = 0; i < this.paths.size() - 1; i++) {
                if (!this.paths.get(i).isEmpty()) {
                    normalized.add(this.paths.get(i));
                }
            }

            normalized.add(this.paths.get(this.paths.size() - 1));

            this.paths.clear();
            this.paths.addAll(normalized);
        }

        return paths;
    }

    /**
     * @return The "name=value" pairs of the query configured on this request
     */
    public List<NameValuePair> queries() {
        return queries;
    }

    /**
     * @return The entity body object configured on this request
     */
    public Object entity() {
        return entity;
    }

    /**
     * Sets the <code><a href=http://tools.ietf.org/html/rfc2616#section-14.1>Accept</a></code> header values.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@code header("Accept", String...)}
     *
     * @param contentTypes The {@link ContentType}(s) expected as response, or <code>null</code> to remove the Accept header
     * @return this request
     */
    public Request accept(ContentType... contentTypes) {
        if (isEmpty(contentTypes)) {
            return header(ACCEPT, NULL_VALUE);
        }

        for (ContentType contentType : contentTypes) {
            header(ACCEPT, contentType.toString());
        }

        return this;
    }

    /**
     * Sets the <a href=http://tools.ietf.org/html/rfc2616#section-14.1>Accept</a> header values.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@code header("Accept", String...)}
     *
     * @param mediaTypes The {@link MediaType}(s) expected as response, or <code>null</code> to remove the Accept header
     * @return this request
     */
    public Request accept(MediaType... mediaTypes) {
        if (isEmpty(mediaTypes)) {
            return header(ACCEPT, NULL_VALUE);
        }

        for (MediaType mediaType : mediaTypes) {
            header(ACCEPT, mediaType.toString());
        }

        return this;
    }

    /**
     * Sets the <a href=http://tools.ietf.org/html/rfc2616#section-14.17>Content-Type</a> header value.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@code header("Content-Type", String)}
     *
     * @param contentType The {@link ContentType} of the request entity, or <code>null</code> to remove the Content-Type header
     * @return this request
     */
    public Request type(ContentType contentType) {
        if (contentType == null) {
            return header(CONTENT_TYPE, NULL_VALUE);
        }

        return header(CONTENT_TYPE, contentType.toString());
    }

    /**
     * Sets the <a href=http://tools.ietf.org/html/rfc2616#section-14.17>Content-Type</a> header value.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@code header("Content-Type", String)}
     *
     * @param mediaType The {@link MediaType} of the request entity, or <code>null</code> to remove the Content-Type header
     * @return this request
     */
    public Request type(MediaType mediaType) {
        if (mediaType == null) {
            return header(CONTENT_TYPE, NULL_VALUE);
        }

        return header(CONTENT_TYPE, mediaType.toString());
    }

    /**
     * Sets the <a href=http://tools.ietf.org/html/rfc2109>Cookie</a> header values.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@code header("Cookie", String...)}
     * @param cookies The {@link HttpCookie}(s) to send over request, or <code>null</code> to remove the Cookie header
     * @return this request
     */
    public Request cookie(HttpCookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    /**
     * Sets the <a href=http://tools.ietf.org/html/rfc2109>Cookie</a> header values.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@code header("Cookie", String...)}
     *
     * @param cookies The {@link Cookie}(s) to send over request, or <code>null</code> to remove the Cookie header
     * @return this request
     */
    public Request cookie(Cookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    /**
     * Sets the <a href=http://tools.ietf.org/html/rfc2109>Cookie</a> header values.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@code header("Cookie", String...)}
     *
     * @param cookies The {@link org.apache.http.cookie.Cookie}(s) to send over request, or <code>null</code> to remove the Cookie header
     * @return this request
     */
    public Request cookie(org.apache.http.cookie.Cookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    /**
     * Sets the <a href=http://tools.ietf.org/html/rfc2109>Cookie</a> header values.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@code header("Cookie", String)}
     *
     * @param cookieAsString The cookie value to send over request, or <code>null</code> to remove the Cookie header
     * @return this request
     */
    public Request cookie(String cookieAsString) {
        return header("Cookie", cookieAsString);
    }

    /**
     * Sets a request Authorization header field to Basic authentication <a
     * href=http://tools.ietf.org/html/rfc2617#page-6>(RFC 2617)</a>. <br>
     *
     * @param username A username
     * @param password A password
     * @return this request
     */
    public Request basicAuth(String username, String password) {
        return header("Authorization", "Basic " + org.apache.commons.codec.binary.Base64.encodeBase64String((username + ":" + password).getBytes()));
    }

    /**
     * Sets a request <a href=http://tools.ietf.org/html/rfc2616#section-5.3>header</a> field.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@link #header(String, String...)}
     *
     * @param name A {@link HttpHeaders} name
     * @param values One or more header values, or <code>null</code> to remove the header
     * @return this request
     */
    public Request header(HttpHeaders name, String... values) {
        return header(name.toString(), values);
    }

    /**
     * Sets a request <a href=http://tools.ietf.org/html/rfc2616#section-5.3>header</a> field.<br>
     *
     * @param name The name of the header
     * @param values One or more header values, or <code>null</code> to remove the header
     * @return this request
     */
    public Request header(String name, String... values) {
        if (isEmpty(values)) {
            headers.remove(name);
            return this;
        }

        for (String value : values) {
            final List<String> valueList = headers.containsKey(name) ? headers.get(name) : new ArrayList<String>();
            valueList.add(value);
            headers.put(name, valueList);
        }

        return this;
    }

    /**
     * Adds some data to the <a href=https://tools.ietf.org/html/rfc3986#section-3.4>query</a> request in the form of "name=value" pairs.<br>
     * The final URI of the request contains a query with all pairs in the order that this method was called, encoded and separated by a single '&amp;' character.<br>
     * <br>
     * <i>Note:</i> the "name=value" pairs cannot be encoded.
     *
     * @param nameValuePairs The "name=value" pairs to be added to the request query, may not be <code>null</code>
     * @return this request
     */
    public Request query(List<NameValuePair> nameValuePairs) {
        queries.addAll(nameValuePairs);

        return this;
    }

    /**
     * Adds some data to the <a href=https://tools.ietf.org/html/rfc3986#section-3.4>query</a> request in the form of "name=value" pairs.<br>
     * The final URI of the request contains a query with all pairs in the order that this method was called, encoded and separated by a single '&amp;' character.
     *
     * @param name The name component of the query, may not be encoded
     * @param value The value component of the query, may not be encoded nor <code>null</code>
     * @return this request
     */
    public Request query(String name, String value) {
        queries.add(new BasicNameValuePair(name, value));

        return this;
    }

    /**
     * Adds some <a href=https://tools.ietf.org/html/rfc3986#section-3.3>path</a>s to the request URI.<br>
     * The final URI of the request contains all paths in the order that this method was called, encoded and separated by a single slash.
     *
     * @param paths One or more paths to append to the request URI, may not be encoded nor <code>null</code>
     * @return this request
     * @throws IllegalArgumentException If the paths are <code>null</code>
     */
    public Request path(String... paths) {
        if (isEmpty(paths)) {
            throw new IllegalArgumentException("Cannot crate a request: path cannot be null");
        }

        for (String path : paths) {
            this.paths.addAll(splitAfterSlashes(path));
        }

        return this;
    }

    /**
     * Sets the <a href=http://tools.ietf.org/html/rfc2616#page-43>entity body</a> to be sent over the request.<br>
     * If the entity is not a primitive type nor an instance of {@link String} or {@link HttpEntity},
     * the Content-Type header must be provided to know how to be processed.
     *
     * @param entity The entity body to be sent, may be <code>null</code>
     * @return this request
     * @see {@link Class#isPrimitive()}
     */
    public Request entity(Object entity) {
        this.entity = entity;

        return this;
    }

    /**
     * Creates a {@link Client} instance to execute this request.
     *
     * @return A client to execute this request
     */
    public Client client() {
        return new Client(this);
    }

    /**
     * Returns an {@link HttpRequest} instance configured according to this request.<br>
     * The instance is ready to be executed via an {@link HttpClient}.<br>
     * <br>
     * This method is not intend to be invoked outside the RESTy library.<br>
     * For your convenience just call {@link #client()} and choose the proper method
     * to execute this request in place of calling a {@link HttpClient#execute(HttpUriRequest)} with this method result.<br>
     * <br>
     * <i>Note:</i> this is equivalent to {@link #toHttpRequest(String)}
     *
     * @param method One of {@link RequestMethod}s value to be the request method.
     * @return An {@link HttpRequest} instance from this request
     * @throws IOException In case some problem occurs during the URI's creation or the entity's processing
     * @throws IllegalArgumentException If method is <code>null</code>
     */
    public HttpRequest toHttpRequest(RequestMethod method) throws IOException {
        return new HttpRequest(this, methodToString(method));
    }

    /**
     * Returns an {@link HttpRequest} instance configured according to this request.<br>
     * The instance is ready to be executed via an {@link HttpClient}.<br>
     * <br>
     * This method is not intend to be invoked outside the RESTy library.<br>
     * For your convenience just call {@link #client()} and choose the proper method
     * to execute this request in place of calling a {@link HttpClient#execute(HttpUriRequest)} with this method result.<br>
     *
     * @param method The request method of the request like GET, POST, PUT, DELETE or other.
     * @return An {@link HttpRequest} instance from this request
     * @throws IOException In case some problem occurs during the URI's creation or the entity's processing
     * @throws IllegalArgumentException If method is <code>null</code>
     */
    public HttpRequest toHttpRequest(String method) throws IOException {
        return new HttpRequest(this, methodToString(method));
    }

    private String methodToString(Object methodObject) {
        if (methodObject == null) {
            throw new IllegalArgumentException("HTTP Request Method cannot be null");
        }

        return methodObject.toString();
    }
}
