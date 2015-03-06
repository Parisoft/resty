package com.github.parisoft.resty.utils;

import javax.ws.rs.core.MediaType;

import org.apache.http.entity.ContentType;

public class MediaTypeUtils {

    public static MediaType valueOf(ContentType contentType) {
        final String[] mime = StringUtils.splitOnSlashes(contentType.getMimeType());
        final String charset = contentType.getCharset() != null ? contentType.getCharset().toString() : null;

        return new MediaType(mime[0], mime[1], charset);
    }
}
