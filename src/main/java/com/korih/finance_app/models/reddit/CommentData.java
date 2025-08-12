package com.korih.finance_app.models.reddit;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentData {

  @JsonProperty("id")
  private String id;

  @JsonProperty("author_fullname")
  private String author;

  @JsonProperty("body")
  private String body;

  @JsonProperty("parent_id")
  private String parentId;

  @JsonProperty("created_utc")
  private String createdUtc;

  @JsonProperty("permalink")
  private String permalink;

  private Map<String, Object> extraFields = new HashMap<>();

  @JsonAnySetter
  public void setExtraField(String key, Object value) {
    extraFields.put(key, value);
  }
}
