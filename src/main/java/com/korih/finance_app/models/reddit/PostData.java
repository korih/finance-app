package com.korih.finance_app.models.reddit;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostData {

  @JsonProperty("id")
  private String id;

  @JsonProperty("title")
  private String title;

  @JsonProperty("permalink")
  private String permalink;

  @Nullable
  @Column(columnDefinition = "TEXT")
  @JsonProperty("selftext")
  private String selftext;

  @JsonProperty("subreddit")
  private String subreddit;

  @JsonProperty("author_fullname")
  private String author;

  @JsonProperty("created_utc")
  private String createdUtc;

  @JsonProperty("num_comments")
  private int numComments;

  private Map<String, Object> extraFields = new HashMap<>();

  @JsonAnySetter
  public void setExtraField(String key, Object value) {
    extraFields.put(key, value);
  }
}
