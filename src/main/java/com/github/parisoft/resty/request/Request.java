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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.github.parisoft.resty.client.Client;
import com.github.parisoft.resty.utils.CookieUtils;

public class Request {

    private static final String NULL_VALUE = null;

    private Map<String, List<String>> headers = new HashMap<>();
    private List<NameValuePair> queries = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private Object entity;
    URI rootUri;

    public Request(String uri) {
        this(stringToUri(uri));
    }

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

    public Map<String, List<String>> headers() {
        return headers;
    }

    public List<String> paths() {
        return paths;
    }

    public List<NameValuePair> queries() {
        return queries;
    }

    public Object entity() {
        return entity;
    }

    public Request accept(ContentType... contentTypes) {
        if (isEmpty(contentTypes)) {
            return header(ACCEPT, NULL_VALUE);
        }

        for (ContentType contentType : contentTypes) {
            header(ACCEPT, contentType.toString());
        }

        return this;
    }

    public Request accept(MediaType... mediaTypes) {
        if (isEmpty(mediaTypes)) {
            return header(ACCEPT, NULL_VALUE);
        }

        for (MediaType mediaType : mediaTypes) {
            header(ACCEPT, mediaType.toString());
        }

        return this;
    }

    public Request type(ContentType contentType) {
        if (contentType == null) {
            return header(CONTENT_TYPE, NULL_VALUE);
        }

        return header(CONTENT_TYPE, contentType.toString());
    }

    public Request type(MediaType mediaType) {
        if (mediaType == null) {
            return header(CONTENT_TYPE, NULL_VALUE);
        }

        return header(CONTENT_TYPE, mediaType.toString());
    }

    public Request cookie(HttpCookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    public Request cookie(Cookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    public Request cookie(org.apache.http.cookie.Cookie... cookies) {
        return header("Cookie", CookieUtils.toString(cookies));
    }

    public Request cookie(String cookieAsString) {
        return header("Cookie", cookieAsString);
    }

    public Request header(HttpHeaders name, String... values) {
        return header(name.toString(), values);
    }

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

    public Request query(List<NameValuePair> nameValuePairs) {
        queries.addAll(nameValuePairs);

        return this;
    }

    public Request query(String name, String value) {
        queries.add(new BasicNameValuePair(name, value));

        return this;
    }

    public Request path(String... paths) {
        if (isEmpty(paths)) {
            throw new IllegalArgumentException("Cannot crate a request: path cannot be null");
        }

        for (String path : paths) {
            this.paths.addAll(splitAfterSlashes(path));
        }

        return this;
    }

    public Request entity(Object entity) {
        this.entity = entity;

        return this;
    }

    public Client client() {
        return new Client(this);
    }

    public HttpRequest toHttpRequest(RequestMethod method) throws IOException {
        return new HttpRequest(this, methodToString(method));
    }

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
