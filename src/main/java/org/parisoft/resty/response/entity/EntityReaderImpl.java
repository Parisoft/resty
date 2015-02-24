package org.parisoft.resty.response.entity;

import static javax.ws.rs.core.MediaType.CHARSET_PARAMETER;
import static org.apache.http.HttpHeaders.CONTENT_ENCODING;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.type.TypeReference;
import org.parisoft.resty.utils.JacksonUtils;

public class EntityReaderImpl implements EntityReader {
    public static final String CHARSET_DEFAULT = "UTF-8";

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

            final String encoding = getContentEncoding().getValue();
            final String charset = getContentCharSet();

            try {
                final HttpEntity entityWrapper = "gzip".equalsIgnoreCase(encoding) ? new GzipDecompressingEntity(entity) : entity;
                final ByteArrayOutputStream output = new ByteArrayOutputStream();

                entityWrapper.writeTo(output);

                body = output.toString(charset);
            } finally {
                EntityUtils.consume(entity);
            }
        }

        return body;
    }

    @Override
    public <T> T getEntityAs(Class<T> someClass) throws IOException {
        if (body == null) {
            final HttpEntity entity = httpResponse.getEntity();

            if (entity == null) {
                return null;
            }

            final MediaType type;

            try {
                type = MediaType.valueOf(getContentType().getValue());
            } catch (Exception e) {
                throw new IOException("Cannot read response entity: unknown Content-Type=" + getContentType().getValue());
            }

            final String encoding = getContentEncoding().getValue();

            try {
                final HttpEntity entityWrapper = "gzip".equalsIgnoreCase(encoding) ? new GzipDecompressingEntity(entity) : entity;

                return JacksonUtils.read(entityWrapper, someClass, type);
            } finally {
                EntityUtils.consume(entity);
            }
        }

        return null;
    }

    @Override
    public <T> T getEntityAs(TypeReference<T> reference) throws IOException {
        if (body == null) {
            final HttpEntity entity = httpResponse.getEntity();

            if (entity == null) {
                return null;
            }

            final MediaType type;

            try {
                type = MediaType.valueOf(getContentType().getValue());
            } catch (Exception e) {
                throw new IOException("Cannot read response entity: unknown Content-Type=" + getContentType().getValue());
            }

            final String encoding = getContentEncoding().getValue();

            try {
                final HttpEntity entityWrapper = "gzip".equalsIgnoreCase(encoding) ? new GzipDecompressingEntity(entity) : entity;

                return JacksonUtils.read(entityWrapper, reference, type);
            } finally {
                EntityUtils.consume(entity);
            }
        }

        return null;
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

        return CHARSET_DEFAULT;
    }

    @Override
    public Header getContentType() {
        final HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {
            return entity.getContentType();
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

        return new BasicHeader(CONTENT_ENCODING, "");
    }
}
