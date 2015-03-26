package com.github.parisoft.resty.server.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrimitiveController {

    @RequestMapping(method=GET, value="/primitive")
    public ResponseEntity<String> primitive(@RequestParam("value") String value) {
        return ResponseEntity.ok(value);
    }
}
