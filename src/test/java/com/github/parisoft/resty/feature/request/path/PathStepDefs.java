package com.github.parisoft.resty.feature.request.path;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import com.github.parisoft.resty.RESTy;
import com.github.parisoft.resty.request.Request;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PathStepDefs {

    private Request request;

    @When("^a request is created with \"(.*?)\"$")
    public void a_request_is_created_with(String uri) throws Throwable {
        request = RESTy.request(uri);
    }

    @When("^a request is created from \"(.*?)\" and \"(.*?)\"$")
    public void a_request_is_created_from_and(String uri, List<String> paths) throws Throwable {
        request = RESTy.request(uri);

        for (String path : paths) {
            request.path(path);
        }
    }

    @When("^a request is created from \"(.*?)\" and $")
    public void a_request_is_created_from_and(String uri) throws Throwable {
        request = RESTy.request(uri);
    }

    @Then("^the paths from request are \"(.*?)\"$")
    public void the_paths_from_request_are(List<String> expectedPaths) throws Throwable {
        assertEquals(expectedPaths, request.paths());
    }

    @Then("^the paths from request are $")
    public void the_paths_from_request_are() throws Throwable {
        assertEquals(Collections.emptyList(), request.paths());
    }
}
