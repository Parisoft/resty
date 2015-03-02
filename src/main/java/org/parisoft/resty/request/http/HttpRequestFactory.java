package org.parisoft.resty.request.http;

import java.io.IOException;

import org.parisoft.resty.Client;

public class HttpRequestFactory {

    public static HttpRequest newGetRequest(Client client) {
        return new GetRequest(client);
    }

    public static HttpRequest newDeleteRequest(Client client) {
        return new DeleteRequest(client);
    }

    public static HttpRequest newPostRequest(Client client) throws IOException {
        return new PostRequest(client);
    }

    public static HttpRequest newPutRequest(Client client) throws IOException {
        return new PutRequest(client);
    }

    public static HttpRequest newRequest(String method, Client client) throws IOException {
        if (method == null) {
            throw new IllegalArgumentException("HTTP Method cannot be null");
        }

        final String httpMethod = method.toUpperCase();

        switch (httpMethod) {
        case "GET":
            return newGetRequest(client);

        case "DELETE":
            return newDeleteRequest(client);

        case "POST":
            return newPostRequest(client);

        case "PUT":
            return newPutRequest(client);

        default:
            return new HttpRequest(client) {

                @Override
                public String getMethod() {
                    return httpMethod;
                }
            };
        }
    }
}
