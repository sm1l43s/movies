package com.moviescloud.movies.controllers;

import com.moviescloud.movies.services.impl.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/proxy")
public class ProxyController {
    final ProxyService service;
    @Autowired
    public ProxyController(ProxyService service) {
        this.service = service;
    }

    @RequestMapping("/**")
    public ResponseEntity<String> sendRequestToSPM(@RequestBody(required = false) String body,
                                                   HttpMethod method, HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {
        return service.processProxyRequest(body,method,request,response, UUID.randomUUID().toString());
    }
}
