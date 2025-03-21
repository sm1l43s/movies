package com.moviescloud.authentication.services;

import entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserClient {
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public User save(User user) {
        String url = userServiceUrl + "/save";
        return restTemplate.postForObject(url, user, User.class);
    }

    public User findByEmail(String email){
        String url = userServiceUrl + "/find-by-email/{email}";
        return restTemplate.getForObject(url, User.class, email);
    }

    public boolean existsUserByEmail(String email) {
       String url = userServiceUrl + "/exists-by-email/{email}";
       return restTemplate.getForObject(url, Boolean.class, email);
    }

}
