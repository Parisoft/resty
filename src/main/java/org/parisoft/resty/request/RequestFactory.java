package org.parisoft.resty.request;

import java.io.IOException;

import org.parisoft.resty.Client;

public class RequestFactory {

    public static Request newGetRequest(Client client) {
        return new GetRequest(client);
    }

    public static Request newDeleteRequest(Client client) {
        return new DeleteRequest(client);
    }

    public static Request newPostRequest(Client client) throws IOException {
        return new PostRequest(client);
    }

    public static Request newPutRequest(Client client) throws IOException {
        return new PutRequest(client);
    }

    public static Request newRequest(String method, Client client) throws IOException {
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
            return new Request(client) {

                @Override
                public String getMethod() {
                    return httpMethod;
                }
            };
        }
    }
}
