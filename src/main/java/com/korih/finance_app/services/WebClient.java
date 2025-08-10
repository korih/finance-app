package com.korih.finance_app.services;

import java.net.http.HttpClient;

import org.springframework.stereotype.Service;

import com.korih.finance_app.models.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebClient {
  private final HttpClient httpClient;

  public <T> ApiResponse<T> getAsync(String url, Class<T> responseType) {
    // Implementation for asynchronous GET request
    // This is a placeholder; actual implementation would involve using httpClient to make the request
    ApiResponse<T> response = new ApiResponse<>();
    response.setData(null); // Replace with actual data from the response
    response.setStatusCode(200); // Replace with actual status code
    response.setMessage("Success"); // Replace with actual message
    return response;
  }
}
