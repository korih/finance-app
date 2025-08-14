package com.korih.finance_app.services;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korih.finance_app.models.ApiMessageEnum;
import com.korih.finance_app.models.ApiResponse;

@Service
public class WebClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Semaphore available = new Semaphore(MAX_CONCURRENT_REQUESTS, true);
    private final ExecutorService executors = Executors.newVirtualThreadPerTaskExecutor();

    private static final int MAX_CONCURRENT_REQUESTS = 10;

    public <T> ApiResponse<T> request(HttpRequest request, Class<T> responseType) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        try {
            available.acquire();
            try {
                var response = executors.submit(() -> httpClient.send(request, HttpResponse.BodyHandlers.ofString())).get();
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    T body = objectMapper.readValue(response.body(), responseType);
                    apiResponse.setData(body);
                    apiResponse.setStatusCode(response.statusCode());
                    apiResponse.setMessage(ApiMessageEnum.SUCCESS);
                } else {
                    apiResponse.setStatusCode(response.statusCode());
                    apiResponse.setMessage(ApiMessageEnum.BAD_REQUEST);
                }
            } finally {
                available.release();
            }
        } catch (Exception e) {
            System.out.println("Error during request: " + e.getMessage());
            apiResponse.setStatusCode(0);
            apiResponse.setMessage(ApiMessageEnum.ERROR);
            return apiResponse;
        }
        return apiResponse;
    }
}
