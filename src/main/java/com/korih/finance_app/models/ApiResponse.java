package com.korih.finance_app.models;

import lombok.Data;

@Data
public class ApiResponse<T> {
  private T data;
  private int statusCode;
  private String message;
}
