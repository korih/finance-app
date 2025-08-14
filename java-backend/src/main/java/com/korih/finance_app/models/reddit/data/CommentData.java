package com.korih.finance_app.models.reddit.data;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.korih.finance_app.models.reddit.RedditData;
import com.korih.finance_app.models.reddit.entity.CommentEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class CommentData extends RedditData {

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

  public static CommentEntity toEntity(CommentData commentData, PostData postData) {
    if (commentData == null || postData == null) {
      return null;
    }
    CommentEntity entity = new CommentEntity();
    entity.setId(commentData.getId());
    entity.setBody(commentData.getBody());
    entity.setAuthor(commentData.getAuthor());
    entity.setCreatedUtc(commentData.getCreatedUtc());
    entity.setParentId(commentData.getParentId());
    entity.setPostData(PostData.toEntity(postData));

    // Store all extra fields and known fields in rawCommentData
    Map<String, Object> raw = new HashMap<>(commentData.getExtraFields());

    try {
      raw.put("id", commentData.getId());
      raw.put("body", commentData.getBody());
      raw.put("author", commentData.getAuthor());
      raw.put("created_utc", commentData.getCreatedUtc());
      raw.put("parent_id", commentData.getParentId());
      var json = objectMapper.writeValueAsString(raw);
      entity.setRawCommentData(json);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize raw comment data", e);
    }
    return entity;
  }
}
