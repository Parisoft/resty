package com.github.parisoft.resty.response;

import java.lang.reflect.Proxy;

import org.apache.http.HttpResponse;

public class ResponseFactory {

    public static Response newResponse(HttpResponse httpResponse) {
        return (Response) Proxy.newProxyInstance(
                Response.class.getClassLoader(),
                new Class[]{ Response.class },
                new ResponseInvocationHandler(httpResponse));
    }
}
