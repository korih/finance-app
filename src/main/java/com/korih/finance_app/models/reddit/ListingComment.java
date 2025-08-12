package com.korih.finance_app.models.reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListingComment {
  
  @JsonProperty("kind")
  private String kind;

  @JsonProperty("data")
  private CommentData data;
}
