package com.github.parisoft.resty.feature.client.timeout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import com.github.parisoft.resty.RESTy;
import com.github.parisoft.resty.response.Response;
import com.github.parisoft.resty.server.LocalServer;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TimeoutStepDefs {

    private Exception actualException;
    private Response actualResponse;
    private long actualRequestTime;

    private Integer timeout;

    @Before
    public void before() {
        LocalServer.start();
    }

    @Given("^a timeout of (\\d+) millis$")
    public void a_timeout_of_millis(int millis) throws Throwable {
        timeout = millis;
    }

    @When("^do a HEAD request to \"(.*?)\" with the given timeout$")
    public void do_a_HEAD_request_to_with_the_given_timeout(String path) throws Throwable {
        final long startTime = System.currentTimeMillis();

        try {
            actualResponse = RESTy.request(LocalServer.getHost())
                    .path(path)
                    .client()
                    .timeout(timeout, TimeUnit.MILLISECONDS)
                    .head();
        } catch (Exception e) {
            actualException = e;
        }

        actualRequestTime = System.currentTimeMillis() - startTime;
    }

    @Then("^a SoketTimeoutException is thrown$")
    public void a_SoketTimeoutException_is_thrown() throws Throwable {
        assertNotNull(actualException);
        assertTrue(actualException instanceof SocketTimeoutException);
    }

    @Then("^the time of request is greater then the given timeout$")
    public void the_time_of_request_is_greater_then_the_given_timeout() throws Throwable {
        assertTrue(actualRequestTime > timeout);
    }

    @Then("^the time of request is lower then the given timeout$")
    public void the_time_of_request_is_lower_then_the_given_timeout() throws Throwable {
        assertTrue(actualRequestTime < timeout);
    }

    @Then("^the response status code is (\\d+)$")
    public void the_response_status_code_is(int expectedStatusCode) throws Throwable {
        assertEquals(expectedStatusCode, actualResponse.getStatusCode());
    }
}
