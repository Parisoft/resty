package org.parisoft.resty.response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.parisoft.resty.response.entity.EntityReader;
import org.parisoft.resty.response.entity.EntityReaderImpl;

public class ResponseInvocationHandler implements InvocationHandler {

    private final HttpResponse httpResponse;
    private final EntityReader entityReader;

    public ResponseInvocationHandler(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        this.entityReader = new EntityReaderImpl(httpResponse);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Class<?> declaringClass = method.getDeclaringClass();

        if (declaringClass.equals(HttpResponse.class)) {
            return method.invoke(httpResponse, args);
        }

        if (declaringClass.equals(EntityReader.class)) {
            return method.invoke(entityReader, args);
        }

        if (declaringClass.equals(Response.class) && method.getName().equals("getStatusCode")) {
            return httpResponse.getStatusLine().getStatusCode();
        }

        return null;
    }

}
