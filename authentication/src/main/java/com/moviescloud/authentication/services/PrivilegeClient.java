package com.moviescloud.authentication.services;

import entities.Privilege;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PrivilegeClient {
    private final RestTemplate restTemplate;

    @Value("${privilege.service.url}")
    private String privilegeServiceUrl;

    public Privilege findByName(String name){
        String url = privilegeServiceUrl + "/find-by-name/{name}";
        return restTemplate.getForObject(url, Privilege.class, name);
    }
}
