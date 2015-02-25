package org.parisoft.resty;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;

public class RESTy {

    private static final Map<String, Object> providers = new HashMap<>();
    static {
        providers.put("JSON", new JacksonJaxbJsonProvider());
        providers.put("XML", new JacksonJaxbXMLProvider());
    }

    public static JacksonJaxbJsonProvider getJsonProvider() {
        return (JacksonJaxbJsonProvider) providers.get("JSON");
    }

    public static JacksonJaxbXMLProvider getXmlProvider() {
        return (JacksonJaxbXMLProvider) providers.get("XML");
    }

    public static Collection<Object> getProviders() {
        return providers.values();
    }

    public static Client client(String baseAddress) {
        return new Client(baseAddress);
    }

    public static Client client(URI uri) {
        return new Client(uri);
    }
}
