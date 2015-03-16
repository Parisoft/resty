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
