package org.parisoft.resty.request;

import org.parisoft.resty.Client;

public class DeleteRequest extends Request {

    DeleteRequest(Client client) {
        super(client);
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }
}
