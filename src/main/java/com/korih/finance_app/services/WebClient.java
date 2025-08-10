package com.korih.finance_app.services;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korih.finance_app.models.ApiMessageEnum;
import com.korih.finance_app.models.ApiResponse;

@Service
public class WebClient {
  private final HttpClient httpClient = HttpClient.newHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Sends a GET request to the given URL, deserializes the JSON response into the
   * given responseType,
   * and wraps it in an ApiResponse. The blocking HTTP call is run on a virtual
   * thread.
   */
  public <T> ApiResponse<T> request(HttpRequest request, Class<T> responseType) {
    ApiResponse<T> apiResponse = new ApiResponse<>();
    try {
      Executors.newVirtualThreadPerTaskExecutor().submit(() -> {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        apiResponse.setStatusCode(response.statusCode());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
          T body = objectMapper.readValue(response.body(), responseType);
          apiResponse.setData(body);
          apiResponse.setMessage(ApiMessageEnum.SUCCESS);
        } else {
          apiResponse.setMessage(ApiMessageEnum.ERROR);
        }
        return apiResponse;
      }).get();

    } catch (Exception e) {
      apiResponse.setStatusCode(500);
      apiResponse.setMessage(ApiMessageEnum.INTERNAL_SERVER_ERROR);
    }
    return apiResponse;
  }
}
