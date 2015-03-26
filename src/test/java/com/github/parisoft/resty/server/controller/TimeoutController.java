package com.github.parisoft.resty.server.controller;

import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeoutController {

    public static AtomicInteger requestCounter = new AtomicInteger(0);

    @RequestMapping(method={HEAD, POST}, value="/sleep/{time}")
    public ResponseEntity<Void> sleep(@PathVariable("time") Long time) throws InterruptedException {
        requestCounter.incrementAndGet();
        Thread.sleep(time);
        return ResponseEntity.noContent().build();
    }
}
