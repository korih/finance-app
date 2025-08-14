package com.korih.finance_app.models.reddit.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korih.finance_app.models.reddit.data.PostData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "posts")
@NoArgsConstructor
public class PostEntity {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Id
  private String id;

  private String title;

  private String permalink;

  @Column(columnDefinition = "TEXT")
  private String selftext;

  private String subreddit;

  private String author;

  private String createdUtc;

  private int numComments;

  @Column(columnDefinition = "jsonb")
  @JdbcTypeCode(SqlTypes.JSON)
  private String rawPostData;

  public static PostData toData(PostEntity entity) {
    if (entity == null)
      return null;
    PostData postData = new PostData();
    postData.setId(entity.getId());
    postData.setTitle(entity.getTitle());
    postData.setPermalink(entity.getPermalink());
    postData.setSelftext(entity.getSelftext());
    postData.setSubreddit(entity.getSubreddit());
    postData.setAuthor(entity.getAuthor());
    postData.setCreatedUtc(entity.getCreatedUtc());
    postData.setNumComments(entity.getNumComments());
    // If rawPostData exists, add all its entries to extraFields (excluding known
    // fields)
    if (entity.getRawPostData() != null) {
      try {
        Map<String, Object> raw = objectMapper.readValue(
            entity.getRawPostData(),
            new TypeReference<Map<String, Object>>() {
            });
        // Remove known fields to avoid overwriting
        raw.remove("id");
        raw.remove("title");
        raw.remove("selftext");
        raw.remove("permalink");
        raw.remove("subreddit");
        raw.remove("author");
        raw.remove("created_utc");
        raw.remove("num_comments");
        postData.getExtraFields().putAll(raw);
      } catch (Exception e) {
        throw new RuntimeException("Failed to deserialize raw post data", e);
      }
    }
    return postData;
  }
}