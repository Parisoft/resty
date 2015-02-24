package org.parisoft.resty.response.entity;

import java.io.IOException;

import org.codehaus.jackson.type.TypeReference;


public interface EntityReader {
    /**
     * @return this HTTP entity content as {@link java.lang.String}.
     * @throws IOException if an error occurs while reading the input stream
     */
    public String getEntityAsString() throws IOException;

    public <T> T getEntityAs(Class<T> someClass) throws IOException ;

    public <T> T getEntityAs(TypeReference<T> reference) throws IOException;
}
