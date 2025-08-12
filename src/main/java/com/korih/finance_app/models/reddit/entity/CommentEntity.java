package com.korih.finance_app.models.reddit.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korih.finance_app.models.reddit.data.CommentData;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "comments")
@Entity
@NoArgsConstructor
public class CommentEntity {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Id
  private String id;

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private PostEntity postData;

  @Nullable
  private String parentId;

  @Column(columnDefinition = "TEXT")
  private String body;

  @Column(columnDefinition = "jsonb")
  @JdbcTypeCode(SqlTypes.JSON)
  private String rawCommentData;

  private String author;

  private String createdUtc;

  public static CommentData toData(CommentEntity commentEntity) {
    if (commentEntity == null) {
      return null;
    }
    CommentData commentData = new CommentData();
    commentData.setId(commentEntity.getId());
    commentData.setBody(commentEntity.getBody());
    commentData.setAuthor(commentEntity.getAuthor());
    commentData.setCreatedUtc(commentEntity.getCreatedUtc());
    commentData.setParentId(commentEntity.getParentId());
    // Optionally set permalink if you have it in the entity
    // commentData.setPermalink(commentEntity.getPermalink());
    // Deserialize rawCommentData into extraFields
    if (commentEntity.getRawCommentData() != null) {
      try {
        Map<String, Object> raw = objectMapper.readValue(
            commentEntity.getRawCommentData(),
            new TypeReference<Map<String, Object>>() {
            });
        // Remove known fields to avoid overwriting
        raw.remove("id");
        raw.remove("body");
        raw.remove("author");
        raw.remove("created_utc");
        raw.remove("parent_id");
        commentData.getExtraFields().putAll(raw);
      } catch (Exception e) {
        throw new RuntimeException("Failed to deserialize raw comment data", e);
      }
    }
    return commentData;
  }
}
