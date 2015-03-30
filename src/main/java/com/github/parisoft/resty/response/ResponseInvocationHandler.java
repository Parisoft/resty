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
package com.github.parisoft.resty.response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.http.HttpResponse;

import com.github.parisoft.resty.entity.EntityReader;
import com.github.parisoft.resty.entity.EntityReaderImpl;
import com.github.parisoft.resty.response.http.HttpResponseExtension;
import com.github.parisoft.resty.response.http.HttpResponseExtensionImpl;

public class ResponseInvocationHandler implements InvocationHandler {

    private final HttpResponse httpResponse;
    private final EntityReader entityReader;
    private final HttpResponseExtension responseExtension;

    public ResponseInvocationHandler(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        this.entityReader = new EntityReaderImpl(httpResponse);
        this.responseExtension = new HttpResponseExtensionImpl(httpResponse);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Class<?> declaringClass = method.getDeclaringClass();

        try {
            if (declaringClass.isAssignableFrom(HttpResponse.class)) {
                return method.invoke(httpResponse, args);
            }

            if (declaringClass.isAssignableFrom(EntityReader.class)) {
                return method.invoke(entityReader, args);
            }

            if (declaringClass.isAssignableFrom(HttpResponseExtension.class)) {
                responseExtension.setResponse((Response) proxy);
                return method.invoke(responseExtension, args);
            }
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        return null;
    }

}
