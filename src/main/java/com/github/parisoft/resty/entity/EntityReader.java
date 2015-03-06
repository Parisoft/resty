package com.github.parisoft.resty.entity;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;


public interface EntityReader {
    /**
     * @return this HTTP entity content as {@link java.lang.String}.
     * @throws IOException if an error occurs while reading the input stream
     */
    public String getEntityAsString() throws IOException;

    public <T> T getEntityAs(Class<T> someClass) throws IOException ;

    public <T> T getEntityAs(TypeReference<T> reference) throws IOException;

    /**
     * @param entity The HTTP entity to get the charset
     * @return the charset of a given HTTP entity
     */
    public Charset getContentCharSet();

    public ContentType getContentType();

    /**
     * @param entity The HTTP entity to get the encoding
     * @return the encoding of a given HTTP entity or a empty String if unknow
     */
    public Header getContentEncoding();
}
