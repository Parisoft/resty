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

import static com.github.parisoft.resty.utils.ObjectUtils.isPrimitive;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.github.parisoft.resty.request.Request;
import com.github.parisoft.resty.utils.JacksonUtils;
import com.github.parisoft.resty.utils.MediaTypeUtils;

public class EntityWriterImpl implements EntityWriter {

    private final Request request;

    public EntityWriterImpl(Request request) {
        this.request = request;
    }

    @Override
    public HttpEntity getEntity() throws IOException {
        final Object rawEntity = request.entity();
        final String rawType = request.headers().containsKey(CONTENT_TYPE) ? request.headers().getFirst(CONTENT_TYPE) : null;
        final HttpEntity entity;

        try {
            if (rawEntity == null || rawEntity instanceof HttpEntity) {
                entity = (HttpEntity) rawEntity;
            } else if (rawEntity instanceof String || isPrimitive(rawEntity)) {
                final ContentType contentType = (rawType == null) ? null : ContentType.parse(rawType);

                entity = new StringEntity(rawEntity.toString(), contentType);
            } else {
                final ContentType contentType = ContentType.parse(rawType);
                final MediaType mediaType = MediaTypeUtils.valueOf(contentType);
                final String entityAsString = JacksonUtils.write(rawEntity, mediaType);

                entity = new StringEntity(entityAsString, contentType);
            }
        } catch (Exception e) {
            throw new IOException("Cannot write request entity", e);
        }

        return entity;
    }

}
