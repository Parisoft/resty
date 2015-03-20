package com.github.parisoft.resty.processor;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.jaxrs.base.ProviderBase;
import com.fasterxml.jackson.jaxrs.cbor.JacksonCBORProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.smile.JacksonJaxbSmileProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;

public class DataProcessors extends LinkedHashMap<DataFormat, ProviderBase<?, ?, ?, ?>> {
    private static final long serialVersionUID = -3085599118858127040L;

    private static final DataProcessors instance = new DataProcessors();

    private DataProcessors() {
        put(DataFormat.JSON, new JacksonJaxbJsonProvider());
        put(DataFormat.XML, new JacksonJaxbXMLProvider());
        put(DataFormat.SMILE, new JacksonJaxbSmileProvider());
        put(DataFormat.CBOR, new JacksonCBORProvider());
    }

    public static DataProcessors getInstance() {
        return instance;
    }
}
