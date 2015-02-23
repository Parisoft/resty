package org.parisoft.resty;

public class RESTy {

    public static Client client(String basicPath) {
        return new Client(basicPath);
    }
}
