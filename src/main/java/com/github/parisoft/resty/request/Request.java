package com.github.parisoft.resty.request;

import static com.github.parisoft.resty.request.RequestMethod.DELETE;
import static com.github.parisoft.resty.request.RequestMethod.GET;
import static com.github.parisoft.resty.request.RequestMethod.HEAD;
import static com.github.parisoft.resty.request.RequestMethod.OPTIONS;
import static com.github.parisoft.resty.request.RequestMethod.POST;
import static com.github.parisoft.resty.request.RequestMethod.PUT;
import static com.github.parisoft.resty.request.RequestMethod.TRACE;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.parisoft.resty.client.Client;
import com.github.parisoft.resty.request.http.HttpRequest;
import com.github.parisoft.resty.response.Response;

public class Request {

    private final Client client;

    public Request(Client client) {
        this.client = client;
    }

    public Response get() throws IOException {
        return execute(GET);
    }

    public <T> T get(Class<T> responseClass) throws IOException {
        return get()
                .getEntityAs(responseClass);
    }

    public <T> T get(TypeReference<T> responseReference) throws IOException {
        return get()
                .getEntityAs(responseReference);
    }

    public Response head() throws IOException {
        return execute(HEAD);
    }

    public <T> T head(Class<T> responseClass) throws IOException {
        return head()
                .getEntityAs(responseClass);
    }

    public <T> T head(TypeReference<T> responseReference) throws IOException {
        return head()
                .getEntityAs(responseReference);
    }


    public Response delete() throws IOException {
        return execute(DELETE);
    }

    public <T> T delete(Class<T> responseClass) throws IOException {
        return delete()
                .getEntityAs(responseClass);
    }

    public <T> T delete(TypeReference<T> responseReference) throws IOException {
        return delete()
                .getEntityAs(responseReference);
    }

    public Response post() throws IOException {
        return execute(POST);
    }

    public <T> T post(Class<T> responseClass) throws IOException {
        return post()
                .getEntityAs(responseClass);
    }

    public <T> T post(TypeReference<T> responseReference) throws IOException {
        return post()
                .getEntityAs(responseReference);
    }

    public Response post(Object entity) throws IOException {
        client.entity(entity);

        return post();
    }

    public <T> T post(Object entity, Class<T> responseClass) throws IOException {
        return post(entity)
                .getEntityAs(responseClass);
    }

    public <T> T post(Object entity, TypeReference<T> responseReference) throws IOException {
        return post(entity)
                .getEntityAs(responseReference);
    }

    public Response put() throws IOException {
        return execute(PUT);
    }

    public <T> T put(Class<T> responseClass) throws IOException {
        return put()
                .getEntityAs(responseClass);
    }

    public <T> T put(TypeReference<T> responseReference) throws IOException {
        return put()
                .getEntityAs(responseReference);
    }

    public Response put(Object entity) throws IOException {
        client.entity(entity);

        return put();
    }

    public <T> T put(Object entity, Class<T> responseClass) throws IOException {
        return put(entity)
                .getEntityAs(responseClass);
    }

    public <T> T put(Object entity, TypeReference<T> responseReference) throws IOException {
        return put(entity)
                .getEntityAs(responseReference);
    }

    public Response options() throws IOException {
        return execute(OPTIONS);
    }

    public <T> T options(Class<T> responseClass) throws IOException {
        return options()
                .getEntityAs(responseClass);
    }

    public <T> T options(TypeReference<T> responseReference) throws IOException {
        return options()
                .getEntityAs(responseReference);
    }

    public Response trace() throws IOException {
        return execute(TRACE);
    }

    public <T> T trace(Class<T> responseClass) throws IOException {
        return trace()
                .getEntityAs(responseClass);
    }

    public <T> T trace(TypeReference<T> responseReference) throws IOException {
        return trace()
                .getEntityAs(responseReference);
    }

    public Response execute(RequestMethod requestMethod) throws IOException {
        return toHttpRequest(requestMethod)
                .submit();
    }

    public <T> T execute(RequestMethod requestMethod, Class<T> responseClass) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseClass);
    }

    public <T> T execute(RequestMethod requestMethod, TypeReference<T> responseReference) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseReference);
    }

    public Response execute(RequestMethod requestMethod, Object entity) throws IOException {
        client.entity(entity);

        return execute(requestMethod);
    }

    public <T> T execute(RequestMethod requestMethod, Object entity, Class<T> responseClass) throws IOException {
        client.entity(entity);

        return execute(requestMethod, responseClass);
    }

    public <T> T execute(RequestMethod requestMethod, Object entity, TypeReference<T> responseReference) throws IOException {
        client.entity(entity);

        return execute(requestMethod, responseReference);
    }


    public Response execute(String requestMethod) throws IOException {
        return toHttpRequest(requestMethod)
                .submit();
    }

    public <T> T execute(String requestMethod, Class<T> responseClass) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseClass);
    }

    public <T> T execute(String requestMethod, TypeReference<T> responseReference) throws IOException {
        return execute(requestMethod)
                .getEntityAs(responseReference);
    }

    public Response execute(String requestMethod, Object entity) throws IOException {
        client.entity(entity);

        return execute(requestMethod);
    }

    public <T> T execute(String requestMethod, Object entity, Class<T> responseClass) throws IOException {
        client.entity(entity);

        return execute(requestMethod, responseClass);
    }

    public <T> T execute(String requestMethod, Object entity, TypeReference<T> responseReference) throws IOException {
        client.entity(entity);

        return execute(requestMethod, responseReference);
    }

    public HttpRequest toHttpRequest(RequestMethod method) throws IOException {
        return new HttpRequest(client, methodToString(method));
    }

    public HttpRequest toHttpRequest(String method) throws IOException {
        return new HttpRequest(client, methodToString(method));
    }

    private String methodToString(Object methodObject) {
        if (methodObject == null) {
            throw new IllegalArgumentException("HTTP Request Method cannot be null");
        }

        return methodObject.toString();
    }
}
