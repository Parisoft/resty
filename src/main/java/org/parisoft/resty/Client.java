package org.parisoft.resty;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.parisoft.resty.utils.JacksonUtils.toJsonReference;
import static org.parisoft.resty.utils.StringUtils.removeLeadingSlashes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.type.TypeReference;
import org.parisoft.resty.request.RequestFactory;
import org.parisoft.resty.response.Response;

public class Client {

    private Map<String, String> headers = new HashMap<>();
    private List<NameValuePair> queries = new ArrayList<>();
    private String path;
    private int retries = 0;
    private int timeout = 0;
    private Object entity;

    Client(String basicPath) {
        if (basicPath == null) {
            throw new IllegalArgumentException("The basic path cannot be null");
        }

        this.path = basicPath;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String path() {
        return path;
    }

    public List<NameValuePair> queries() {
        return queries;
    }

    public int retries() {
        return retries;
    }

    public int timeout() {
        return timeout;
    }

    public Object entity() {
        return entity;
    }

    public Client accept(MediaType...types) {
        if (types == null || types.length == 0) {
            return header(ACCEPT, (String) null);
        }

        for (MediaType type : types) {
            header(ACCEPT, type.toString());
        }

        return this;
    }

    public Client type(MediaType...types) {
        if (types == null || types.length == 0) {
            return header(CONTENT_TYPE, (String) null);
        }

        for (MediaType type : types) {
            header(CONTENT_TYPE, type.toString());
        }

        return this;
    }

    public Client cookie(Cookie cookie) {
        if (cookie == null) {
            return header("Cookie", (String) null);
        }

        final StringBuilder cookieBuilder = new StringBuilder(cookie.getName()).append("=").append(cookie.getValue());

        if (cookie.getComment() != null) {
            cookieBuilder.append(";").append("Comment=").append(cookie.getComment());
        }

        if (cookie.getDomain() != null) {
            cookieBuilder.append(";").append("Domain=").append(cookie.getDomain());
        }

        final Date now = new Date();

        if (cookie.isExpired(now)) {
            cookieBuilder.append(";").append("Max-Age=0");
        } else if (cookie.getExpiryDate() != null) {
            final long deltaSeconds = TimeUnit.MILLISECONDS.toSeconds(cookie.getExpiryDate().getTime() - now.getTime());
            cookieBuilder.append(";").append("Max-Age=").append(deltaSeconds);
        }

        if (cookie.getPath() != null) {
            cookieBuilder.append(";").append("Path=").append(cookie.getPath());
        }

        if (cookie.isSecure()) {
            cookieBuilder.append(";").append("Secure");
        }

        if (cookie.getVersion() > 0) {
            cookieBuilder.append(";").append("Version=").append(cookie.getVersion());
        }

        return header("Cookie", cookieBuilder.toString());
    }

    public Client cookie(String cookieAsString) {
        return header("Cookie", cookieAsString);
    }

    public Client header(HttpHeaders name, String value) {
        return header(name.toString(), value);
    }

    public Client header(String name, String value) {
        if (value == null) {
            headers.remove(name);
            return this;
        }

        final String newValue = headers.containsKey(name) ? headers.get(name) + ", " + value : value;

        headers.put(name, newValue);

        return this;
    }

    public Client header(HttpHeaders name, String... values) {
        return header(name.toString(), values);
    }

    public Client header(String name, String... values) {
        if (values == null || values.length == 0) {
            return header(name, (String) null);
        }

        for (String value : values) {
            header(name, value);
        }

        return this;
    }

    public Client query(String name, String value) {
        queries.add(new BasicNameValuePair(name, value));

        return this;
    }

    public Client path(String path) {
        if (path == null) {
            throw new IllegalArgumentException("A path cannot be null");
        }

        this.path += (this.path.endsWith("/") ? "" : "/") + removeLeadingSlashes(path);

        return this;
    }

    public Client retries(int retries) {
        this.retries = retries;

        return this;
    }

    public Client timeout(int value, TimeUnit unit) {
        timeout = (int) unit.toMillis(value);

        return this;
    }

    public Client entity(HttpEntity entity) {
        this.entity = entity;

        return this;
    }

    public Response get() throws IOException {
        return RequestFactory
                .newGetRequest(this)
                .submit();
    }

    public <T> T get(Class<T> responseClass) throws IOException {
        return get()
                .getEntityAs(responseClass);
    }

    public <T> T get(TypeReference<T> responseReference) throws IOException {
        return get()
                .getEntityAs(responseReference);
    }

    public <T> T get(final com.fasterxml.jackson.core.type.TypeReference<T> responseReference) throws IOException {
        return get(toJsonReference(responseReference));
    }

    public Response delete() throws IOException {
        return RequestFactory
                .newDeleteRequest(this)
                .submit();
    }

    public <T> T delete(Class<T> responseClass) throws IOException {
        return delete()
                .getEntityAs(responseClass);
    }

    public <T> T delete(TypeReference<T> responseReference) throws IOException {
        return delete()
                .getEntityAs(responseReference);
    }

    public <T> T delete(com.fasterxml.jackson.core.type.TypeReference<T> responseReference) throws IOException {
        return delete(toJsonReference(responseReference));
    }

    public Response execute(String httpMethod) throws IOException {
        return RequestFactory
                .newRequest(httpMethod, this)
                .submit();
    }

    public <T> T execute(String httpMethod, Class<T> responseClass) throws IOException {
        return execute(httpMethod)
                .getEntityAs(responseClass);
    }

    public <T> T execute(String httpMethod, TypeReference<T> responseReference) throws IOException {
        return execute(httpMethod)
                .getEntityAs(responseReference);
    }

    public <T> T execute(String httpMethod, com.fasterxml.jackson.core.type.TypeReference<T> responseReference) throws IOException {
        return execute(httpMethod, toJsonReference(responseReference));
    }

    public Response post() throws IOException {
        return RequestFactory
                .newPostRequest(this)
                .submit();
    }

    public <T> T post(Class<T> responseClass) throws IOException {
        return post()
                .getEntityAs(responseClass);
    }

    public <T> T post(TypeReference<T> responseReference) throws IOException {
        return post()
                .getEntityAs(responseReference);
    }

    public <T> T post(com.fasterxml.jackson.core.type.TypeReference<T> responseReference) throws IOException {
        return post(toJsonReference(responseReference));
    }

    public Response post(Object entity) throws IOException {
        this.entity = entity;

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

    public <T> T post(Object entity, com.fasterxml.jackson.core.type.TypeReference<T> responseReference) throws IOException {
        return post(entity, toJsonReference(responseReference));
    }

    public Response put() throws IOException {
        return RequestFactory
                .newPutRequest(this)
                .submit();
    }

    public <T> T put(Class<T> responseClass) throws IOException {
        return put()
                .getEntityAs(responseClass);
    }

    public <T> T put(TypeReference<T> responseReference) throws IOException {
        return put()
                .getEntityAs(responseReference);
    }

    public <T> T put(com.fasterxml.jackson.core.type.TypeReference<T> responseReference) throws IOException {
        return put(toJsonReference(responseReference));
    }

    public Response put(Object entity) throws IOException {
        this.entity = entity;

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

    public <T> T put(Object entity, com.fasterxml.jackson.core.type.TypeReference<T> responseReference) throws IOException {
        return put(entity, toJsonReference(responseReference));
    }
}
