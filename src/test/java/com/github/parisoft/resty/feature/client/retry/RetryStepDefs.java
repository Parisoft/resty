package com.github.parisoft.resty.feature.client.retry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.parisoft.resty.RESTy;
import com.github.parisoft.resty.request.retry.IdempotentRequestMethods;
import com.github.parisoft.resty.server.LocalServer;
import com.github.parisoft.resty.server.controller.TimeoutController;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RetryStepDefs {

    private Exception actualException;

    private Integer timeout;
    private Integer attempts;
    private AtomicInteger requestCounter = TimeoutController.requestCounter;

    @Before
    public void before() {
        LocalServer.start();
        IdempotentRequestMethods.getInstance().reset();
    }

    @Given("^a timeout of (\\d+) millis$")
    public void a_timeout_of_millis(int millis) throws Throwable {
        timeout = millis;
    }

    @Given("^a retry value of (\\d+) attempts$")
    public void a_retry_value_of_attempts(int attempts) throws Throwable {
        this.attempts = attempts;
    }

    @Given("^a request counter on (\\d+)$")
    public void a_request_counter_on(int counterValue) throws Throwable {
        requestCounter.set(counterValue);
    }

    @Given("^the \"(.*?)\" method as idempotent$")
    public void the_method_as_idempotent(String method) throws Throwable {
        IdempotentRequestMethods.getInstance().add(method);
    }

    @Given("^the \"(.*?)\" method as no idempotent$")
    public void the_method_as_no_idempotent(String method) throws Throwable {
        IdempotentRequestMethods.getInstance().remove(method);
    }

    @When("^do a POST request to \"(.*?)\" with the given timeout and retry$")
    public void do_a_POST_request_to_with_the_given_timeout_and_retry(String path) throws Throwable {
        try {
            RESTy.request(LocalServer.getHost())
            .path(path)
            .client()
            .timeout(timeout, TimeUnit.MILLISECONDS)
            .retries(attempts)
            .post();
        } catch (Exception e) {
            actualException = e;
        }
    }

    @When("^do a HEAD request to \"(.*?)\" with the given timeout and retry$")
    public void do_a_HEAD_request_to_with_the_given_timeout_and_retry(String path) throws Throwable {
        try {
            RESTy.request(LocalServer.getHost())
            .path(path)
            .client()
            .timeout(timeout, TimeUnit.MILLISECONDS)
            .retries(attempts)
            .head();
        } catch (Exception e) {
            actualException = e;
        }
    }

    @When("^do a HEAD request to \"(.*?)\" with the given timeout$")
    public void do_a_HEAD_request_to_with_the_given_timeout(String path) throws Throwable {
        try {
            RESTy.request(LocalServer.getHost())
            .path(path)
            .client()
            .timeout(timeout, TimeUnit.MILLISECONDS)
            .head();
        } catch (Exception e) {
            actualException = e;
        }
    }

    @Then("^the request counter is (\\d+)$")
    public void the_request_counter_is(int exepectedRequestCounter) throws Throwable {
        assertEquals(exepectedRequestCounter, requestCounter.get());
    }

    @Then("^a SoketTimeoutException is thrown$")
    public void a_SoketTimeoutException_is_thrown() throws Throwable {
        assertNotNull(actualException);
        assertTrue(actualException instanceof SocketTimeoutException);
    }
}
