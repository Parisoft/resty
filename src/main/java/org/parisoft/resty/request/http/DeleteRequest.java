package org.parisoft.resty.request.http;

import org.parisoft.resty.Client;

public class DeleteRequest extends HttpRequest {

    DeleteRequest(Client client) {
        super(client);
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }
}
