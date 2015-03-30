/*
 *    Copyright 2013-2014 Parisoft Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.parisoft.resty.entity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Interface that contains useful methods to deal with a response entity body.
 *
 * @author Andre Paris
 *
 */
public interface EntityReader {

    /**
     * Read and convert the response HTTP entity body as a {@link String}.
     *
     * @return The response HTTP entity body as a {@link String}
     * @throws IOException if an error occurs while reading the input stream
     */
    public String getEntityAsString() throws IOException;

    /**
     * Read and convert the response HTTP entity body as a given type.
     *
     * @param someClass The type to convert the response entity
     * @return The response HTTP entity body as a given type
     * @throws IOException if an error occurs while reading the input stream
     */
    public <T> T getEntityAs(Class<T> someClass) throws IOException ;

    /**
     * Read and convert the response HTTP entity body as a given type reference.<br>
     * <br>
     * As exemple, to convert the response entity into a <code>List{@literal <String>}</code> do
     * <pre>
     * getEntityAs( new TypeReference{@literal <List<String>>}(){} )
     * </pre>
     *
     * @param reference The type reference to convert the response entity
     * @return The response HTTP entity body as a given type reference
     * @throws IOException if an error occurs while reading the input stream
     * @see TypeReference
     */
    public <T> T getEntityAs(TypeReference<T> reference) throws IOException;

    /**
     * Extract the response Content-Type charset.<br>
     * <br>
     * Default is UTF-8.
     *
     * @return The response Content-Type charset, or {@link StandardCharsets#UTF_8} if not found
     */
    public Charset getContentCharSet();

    /**
     * @return The response Content-Type header, or text/plain if not found
     */
    public ContentType getContentType();

    /**
     * @return The response Content-Encoding header, or empty if not found
     */
    public Header getContentEncoding();
}
