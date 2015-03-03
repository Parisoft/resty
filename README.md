# RESTy
A minimalist HTTP client made on top of [Apache HttpComponets](http://hc.apache.org/) and [Jackson JSON/XML Processor] (http://wiki.fasterxml.com/JacksonHome).

It's made to be simple and elegant by avoiding *[boilerplate code](http://en.wikipedia.org/wiki/Boilerplate_code)* and a lot of configurations.

## Get Started
Given the [/meta](https://developer.github.com/v3/meta/) github API wich a request to https://api.github.com/meta bring a JSON like this:
```
{
  "verifiable_password_authentication": true,
  "github_services_sha": "f39e3c6fa71f1c5c0e8497c28a5e7e1a4501035f",
  "hooks": [
    "192.30.252.0/22"
  ],
  "git": [
    "192.30.252.0/22"
  ],
  "importer": [
    "54.221.4.64",
    "54.205.129.240",
    "54.227.175.200"
  ]
}
```
Create a **Meta** class to represent the JSON:
```java
package get.started;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Meta {

    @JsonProperty("verifiable_password_authentication")
    private boolean verifiable;

    private List<String> hooks;

    public boolean isVerifiable() {
        return verifiable;
    }

    public void setVerifiable(boolean verifiable) {
        this.verifiable = verifiable;
    }

    public List<String> getHooks() {
        return hooks;
    }

    public void setHooks(List<String> hooks) {
        this.hooks = hooks;
    }
}
```
This code consumes the [/meta](https://developer.github.com/v3/meta/) service and writes the result into a **Meta** object:
```java
package get.started;

import java.io.IOException;

import org.parisoft.resty.RESTy;

public class Example {

    public static void main(String[] args) throws IOException {

        Meta meta = RESTy.client("https://api.github.com")
                .path("meta")
                .request()
                .get(Meta.class);

        System.out.println("Verifiable = " + meta.isVerifiable());
        System.out.println("Hooks      = " + meta.getHooks());
    }
}
```
Executing the code above will print the follow result:
```
Verifiable = true
Hooks      = [192.30.252.0/22]
```
###Thank you for reading