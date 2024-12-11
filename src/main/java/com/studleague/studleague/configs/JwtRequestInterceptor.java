package com.studleague.studleague.configs;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtRequestInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private HttpSession session;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = (String) session.getAttribute("jwtToken");

        if (token != null) {
            request.getHeaders().add("Authorization", "Bearer " + token);
        }

        return execution.execute(request, body);
    }
}