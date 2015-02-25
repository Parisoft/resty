package org.parisoft.resty.utils;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.Header;

public class MediaTypeUtils {

    public static MediaType valueOf(Header contentType) throws IOException {
        return valueOf(contentType.getValue());
    }

    public static MediaType valueOf(String contentType) throws IOException {
        if (contentType == null) {
            throw new IOException("Content-Type cannot be null");
        }

        try {
            return MediaType.valueOf(contentType);
        } catch (Exception e) {
            throw new IOException("unknown Content-Type=" + contentType);
        }
    }
}
