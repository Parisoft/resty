package com.github.parisoft.resty.feature.request.uri;

import static com.github.parisoft.resty.request.RequestMethod.GET;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.List;

import com.github.parisoft.resty.RESTy;
import com.github.parisoft.resty.request.Request;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UriStepDefs {

    private Request request;

    @When("^a request is created from \"(.*?)\" and $")
    public void a_request_is_created_from_and(String uri) throws Throwable {
        request = RESTy.request(uri);
    }

    @When("^a request is created from \"(.*?)\" and \"(.*?)\"$")
    public void a_request_is_created_from_and(String uri, List<String> paths) throws Throwable {
        request = RESTy.request(uri);

        for (String path : paths) {
            request.path(path);
        }
    }

    @When("^a request is created with \"(.*?)\"$")
    public void a_request_is_created_with(String uri) throws Throwable {
        request = RESTy.request(uri);
    }

    @Then("^the URI from HTTP request is \"(.*?)\"$")
    public void the_URI_from_HTTP_request_is(URI expectedUri) throws Throwable {
        final URI actualUri = request.toHttpRequest(GET).getURI();
        assertEquals(expectedUri, actualUri);
    }
}
