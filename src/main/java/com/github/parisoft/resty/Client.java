package com.github.parisoft.resty;

import static com.github.parisoft.resty.utils.ArrayUtils.isEmpty;
import static com.github.parisoft.resty.utils.StringUtils.emptyIfNull;
import static com.github.parisoft.resty.utils.StringUtils.splitAfterSlashes;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.github.parisoft.resty.request.Request;

public class Client {
    private static final String NULL_VALUE = null;

    private Map<String, List<String>> headers = new HashMap<>();
    private List<NameValuePair> queries = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private String rootPath;
    private int retries = 0;
    private int timeout = 0;
    private Object entity;

    Client(String baseAddress) {
        this(pathToUri(baseAddress));
    }

    Client(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Cannot create a client: URI cannot be null");
        }

        path(emptyIfNull(uri.getPath()));

        try {
            query(URLEncodedUtils.parse(uri, UTF_8.name()));
            rootPath = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, uri.getFragment()).toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create a client: " + e.getMessage());
        }
    }

    private static URI pathToUri(String path) {
        if (path == null) {
            throw new IllegalArgumentException("basic path cannot be null");
        }

        return URI.create(path);
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public String rootPath() {
        return rootPath;
    }

    public List<String> paths() {
        return paths;
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

    public Client accept(ContentType... contentTypes) {
        if (isEmpty(contentTypes)) {
            return header(ACCEPT, NULL_VALUE);
        }

        for (ContentType contentType : contentTypes) {
            header(ACCEPT, contentType.toString());
        }

        return this;
    }

    public Client type(ContentType... contentTypes) {
        if (isEmpty(contentTypes)) {
            return header(CONTENT_TYPE, NULL_VALUE);
        }

        for (ContentType contentType : contentTypes) {
            header(CONTENT_TYPE, contentType.toString());
        }

        return this;
    }

    public Client cookie(HttpCookie... cookies) {
        if (isEmpty(cookies)) {
            return header("Cookie", NULL_VALUE);
        }

        final StringBuilder cookieBuilder = new StringBuilder();

        for (HttpCookie cookie : cookies) {
            cookieBuilder.append(cookie.getName()).append("=").append(cookie.getValue());

            if (cookie.getComment() != null) {
                cookieBuilder.append(";").append("Comment=").append(cookie.getComment());
            }

            if (cookie.getDomain() != null) {
                cookieBuilder.append(";").append("Domain=").append(cookie.getDomain());
            }

            if (cookie.getPath() != null) {
                cookieBuilder.append(";").append("Path=").append(cookie.getPath());
            }

            if (cookie.getSecure()) {
                cookieBuilder.append(";").append("Secure");
            }

            if (cookie.isHttpOnly()) {
                cookieBuilder.append(";").append("HttpOnly");
            }

            if (cookie.getVersion() > 0) {
                cookieBuilder.append(";").append("Version=").append(cookie.getVersion());
            }

            if (cookie.hasExpired()) {
                cookieBuilder.append(";").append("Max-Age=0");
            } else {
                cookieBuilder.append(";").append("Max-Age=").append(cookie.getMaxAge());
            }

            cookieBuilder.append(", ");
        }

        return header("Cookie", cookieBuilder.deleteCharAt(cookieBuilder.length() - 1).toString());
    }

    public Client cookie(String cookieAsString) {
        return header("Cookie", cookieAsString);
    }

    public Client header(HttpHeaders name, String... values) {
        return header(name.toString(), values);
    }

    public Client header(String name, String... values) {
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

    public Client query(List<NameValuePair> nameValuePairs) {
        queries.addAll(nameValuePairs);

        return this;
    }

    public Client query(String name, String value) {
        queries.add(new BasicNameValuePair(name, value));

        return this;
    }

    public Client path(String... paths) {
        if (isEmpty(paths)) {
            throw new IllegalArgumentException("Cannot crate a client: path cannot be null");
        }

        for (String path : paths) {
            this.paths.addAll(splitAfterSlashes(path));
        }

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

    public Client entity(Object entity) {
        this.entity = entity;

        return this;
    }

    public Request request() {
        return new Request(this);
    }
}
