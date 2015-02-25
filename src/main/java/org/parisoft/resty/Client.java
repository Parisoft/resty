package org.parisoft.resty;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.parisoft.resty.utils.ArrayUtils.isEmpty;
import static org.parisoft.resty.utils.StringUtils.splitOnSlashes;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.parisoft.resty.request.RequestInvoker;

public class Client extends RequestInvoker {
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

        path(splitOnSlashes(uri.getPath()));

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

    public Client accept(MediaType... types) {
        if (isEmpty(types)) {
            return header(ACCEPT, NULL_VALUE);
        }

        for (MediaType type : types) {
            header(ACCEPT, type.toString());
        }

        return this;
    }

    public Client type(MediaType... types) {
        if (isEmpty(types)) {
            return header(CONTENT_TYPE, NULL_VALUE);
        }

        for (MediaType type : types) {
            header(CONTENT_TYPE, type.toString());
        }

        return this;
    }

    public Client cookie(Cookie... cookies) {
        if (isEmpty(cookies)) {
            return header("Cookie", NULL_VALUE);
        }

        final StringBuilder cookieBuilder = new StringBuilder();

        for (Cookie cookie : cookies) {
            cookieBuilder.append(cookie.getName()).append("=").append(cookie.getValue());

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
            if (path.isEmpty()) {
                continue;
            }

            this.paths.add(path);
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

    @Override
    protected Client getClient() {
        return this;
    }
}
