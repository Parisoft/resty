package org.parisoft.resty.request;

import org.parisoft.resty.Client;

public class RequestFactory {

    public static Request newGetRequest(Client client) {
        return new GetRequest(client);
    }

    public static Request newDeleteRequest(Client client) {
        return new DeleteRequest(client);
    }

    public static Request newRequest(String method, Client client) {
        if (method == null) {
            throw new IllegalArgumentException("HTTP Method cannot be null");
        }

        final String httpMethod = method.toUpperCase();

        switch (httpMethod) {
        case "GET":
            return newGetRequest(client);

        case "DELETE":
            return newDeleteRequest(client);

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
