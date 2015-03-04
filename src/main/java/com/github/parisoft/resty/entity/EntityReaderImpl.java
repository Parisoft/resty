package com.github.parisoft.resty.entity;

import static com.github.parisoft.resty.utils.ObjectUtils.isInstanciableFromString;
import static com.github.parisoft.resty.utils.ObjectUtils.newInstanceFromString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.MediaType.CHARSET_PARAMETER;
import static org.apache.http.HttpHeaders.CONTENT_ENCODING;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.parisoft.resty.utils.JacksonUtils;
import com.github.parisoft.resty.utils.MediaTypeUtils;

public class EntityReaderImpl implements EntityReader {

    private final HttpResponse httpResponse;
    private String body;

    public EntityReaderImpl(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public String getEntityAsString() throws IOException {
        if (body == null) {
            final HttpEntity entity = httpResponse.getEntity();

            if (entity == null) {
                return body = "";
            }

            try {
                final HttpEntity entityWrapper = isGzipEncoding() ? new GzipDecompressingEntity(entity) : entity;
                final ByteArrayOutputStream output = new ByteArrayOutputStream();

                entityWrapper.writeTo(output);

                body = output.toString(getContentCharSet());
            } finally {
                EntityUtils.consume(entity);
            }
        }

        return body;
    }

    @Override
    public <T> T getEntityAs(Class<T> someClass) throws IOException {
        if (isInstanciableFromString(someClass)) {
            return newInstanceFromString(someClass, getEntityAsString());
        }

        if (body != null) {
            return getEntityFromBodyAs(someClass);
        }

        final HttpEntity entity = httpResponse.getEntity();

        if (entity == null) {
            return null;
        }

        try {
            final HttpEntity entityWrapper = isGzipEncoding() ? new GzipDecompressingEntity(entity) : entity;

            return JacksonUtils.read(entityWrapper, someClass, MediaTypeUtils.valueOf(getContentType()));
        } catch (Exception e) {
            throw new IOException("Cannot read response entity: " + e.getMessage());
        } finally {
            EntityUtils.consume(entity);
        }
    }

    private <T> T getEntityFromBodyAs(Class<T> someClass) throws IOException {
        try {
            return JacksonUtils.read(body, someClass, MediaTypeUtils.valueOf(getContentType()));
        } catch (Exception e) {
            throw new IOException("Cannot read response entity: " + e.getMessage());
        }
    }

    @Override
    public <T> T getEntityAs(TypeReference<T> reference) throws IOException {
        if (body != null) {
            return getEntityFromBodyAs(reference);
        }

        final HttpEntity entity = httpResponse.getEntity();

        if (entity == null) {
            return null;
        }

        try {
            final HttpEntity entityWrapper = isGzipEncoding() ? new GzipDecompressingEntity(entity) : entity;

            return JacksonUtils.read(entityWrapper, reference, MediaTypeUtils.valueOf(getContentType()));
        } catch (Exception e) {
            throw new IOException("Cannot read response entity: " + e.getMessage());
        } finally {
            EntityUtils.consume(entity);
        }
    }

    private <T> T getEntityFromBodyAs(TypeReference<T> reference) throws IOException {
        try {
            return JacksonUtils.read(body, reference, MediaTypeUtils.valueOf(getContentType()));
        } catch (Exception e) {
            throw new IOException("Cannot read response entity: " + e.getMessage());
        }
    }

    private boolean isGzipEncoding() {
        return "gzip".equalsIgnoreCase(getContentEncoding().getValue());
    }

    @Override
    public String getContentCharSet() {
        final Header type = getContentType();

        if (type != null) {
            for (HeaderElement headerElement : type.getElements()) {
                if (CHARSET_PARAMETER.equalsIgnoreCase(headerElement.getName())) {
                    return headerElement.getValue();
                }
            }
        }

        return UTF_8.name();
    }

    @Override
    public Header getContentType() {
        final HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {
            return entity.getContentType();
        }

        final Header contentType = httpResponse.getFirstHeader(CONTENT_TYPE);

        if (contentType != null) {
            return contentType;
        }

        return new BasicHeader(CONTENT_TYPE, "");
    }

    @Override
    public Header getContentEncoding() {
        final HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {
            final Header encoding = entity.getContentEncoding();

            if (encoding != null) {
                return encoding;
            }
        }

        final Header contentEncoding = httpResponse.getFirstHeader(CONTENT_ENCODING);

        if (contentEncoding != null) {
            return contentEncoding;
        }

        return new BasicHeader(CONTENT_ENCODING, "");
    }
}
