package com.korih.finance_app.models.reddit.data;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.korih.finance_app.models.reddit.RedditData;
import com.korih.finance_app.models.reddit.entity.PostEntity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class PostData extends RedditData {

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

  public static PostEntity toEntity(PostData postData) {
    if (postData == null)
      return null;
    PostEntity entity = new PostEntity();
    entity.setId(postData.getId());
    entity.setTitle(postData.getTitle());
    entity.setSelftext(postData.getSelftext());
    entity.setPermalink(postData.getPermalink());
    entity.setSubreddit(postData.getSubreddit());
    entity.setAuthor(postData.getAuthor());
    entity.setCreatedUtc(postData.getCreatedUtc());
    entity.setNumComments(postData.getNumComments());
    // Store all extra fields and known fields in rawPostData
    Map<String, Object> raw = new HashMap<>(postData.getExtraFields());
    try {
      raw.put("id", postData.getId());
      raw.put("title", postData.getTitle());
      raw.put("selftext", postData.getSelftext());
      raw.put("permalink", postData.getPermalink());
      raw.put("subreddit", postData.getSubreddit());
      raw.put("author", postData.getAuthor());
      raw.put("created_utc", postData.getCreatedUtc());
      raw.put("num_comments", postData.getNumComments());
      var json = objectMapper.writeValueAsString(raw);
      entity.setRawPostData(json);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize raw post data", e);
    }
    return entity;
  }
}
