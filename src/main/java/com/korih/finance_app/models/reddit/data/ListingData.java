package com.korih.finance_app.models.reddit.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.korih.finance_app.models.reddit.RedditData;
import com.korih.finance_app.models.reddit.RedditThing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class ListingData extends RedditData {
  
  @JsonProperty("after")
  private String after;

  @JsonProperty("dist")
  private String dist;

  @JsonProperty("children")
  private RedditThing[] children;
}
