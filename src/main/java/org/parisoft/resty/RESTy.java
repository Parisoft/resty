/*
 *    Copyright 2013-2014 Parisoft Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.parisoft.resty;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.parisoft.resty.processor.DataFormat;

import com.fasterxml.jackson.jaxrs.base.ProviderBase;
import com.fasterxml.jackson.jaxrs.cbor.JacksonCBORProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.smile.JacksonJaxbSmileProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;

public class RESTy {

    private static final Map<DataFormat, ProviderBase<?, ?, ?, ?>> dataProcessors = new LinkedHashMap<>();
    static {
        dataProcessors.put(DataFormat.JSON, new JacksonJaxbJsonProvider());
        dataProcessors.put(DataFormat.XML, new JacksonJaxbXMLProvider());
        dataProcessors.put(DataFormat.SMILE, new JacksonJaxbSmileProvider());
        dataProcessors.put(DataFormat.CBOR, new JacksonCBORProvider());
    }

    public static Map<DataFormat, ProviderBase<?, ?, ?, ?>> getDataProcessors() {
        return dataProcessors;
    }

    public static ProviderBase<?, ?, ?, ?> getDataProcessor(DataFormat dataFormat) {
        return dataProcessors.get(dataFormat);
    }

    public static Client client(String baseAddress) {
        return new Client(baseAddress);
    }

    public static Client client(URI uri) {
        return new Client(uri);
    }
}
