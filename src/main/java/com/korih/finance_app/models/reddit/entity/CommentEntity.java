package com.korih.finance_app.models.reddit.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
}
