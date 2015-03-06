package com.github.parisoft.resty.request;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.parisoft.resty.Client;
import com.github.parisoft.resty.request.http.HttpRequestFactory;
import com.github.parisoft.resty.response.Response;

public class Request {

    private final Client client;

    public Request(Client client) {
        this.client = client;
    }

    public Response get() throws IOException {
        return HttpRequestFactory
                .newGetRequest(client)
                .submit();
    }

    public <T> T get(Class<T> responseClass) throws IOException {
        return get()
                .getEntityAs(responseClass);
    }

    public <T> T get(TypeReference<T> responseReference) throws IOException {
        return get()
                .getEntityAs(responseReference);
    }

    public Response delete() throws IOException {
        return HttpRequestFactory
                .newDeleteRequest(client)
                .submit();
    }

    public <T> T delete(Class<T> responseClass) throws IOException {
        return delete()
                .getEntityAs(responseClass);
    }

    public <T> T delete(TypeReference<T> responseReference) throws IOException {
        return delete()
                .getEntityAs(responseReference);
    }

    public Response execute(String httpMethod) throws IOException {
        return HttpRequestFactory
                .newRequest(httpMethod, client)
                .submit();
    }

    public <T> T execute(String httpMethod, Class<T> responseClass) throws IOException {
        return execute(httpMethod)
                .getEntityAs(responseClass);
    }

    public <T> T execute(String httpMethod, TypeReference<T> responseReference) throws IOException {
        return execute(httpMethod)
                .getEntityAs(responseReference);
    }

    public Response execute(String httpMethod, Object entity) throws IOException {
        client.entity(entity);

        return execute(httpMethod);
    }

    public <T> T execute(String httpMethod, Object entity, Class<T> responseClass) throws IOException {
        client.entity(entity);

        return execute(httpMethod, responseClass);
    }

    public <T> T execute(String httpMethod, Object entity, TypeReference<T> responseReference) throws IOException {
        client.entity(entity);

        return execute(httpMethod, responseReference);
    }

    public Response post() throws IOException {
        return HttpRequestFactory
                .newPostRequest(client)
                .submit();
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
        return HttpRequestFactory
                .newPutRequest(client)
                .submit();
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
}
