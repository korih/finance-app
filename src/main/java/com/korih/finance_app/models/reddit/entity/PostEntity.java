package com.korih.finance_app.models.reddit.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

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
}