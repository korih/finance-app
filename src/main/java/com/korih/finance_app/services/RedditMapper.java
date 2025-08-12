package com.korih.finance_app.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korih.finance_app.models.reddit.CommentData;
import com.korih.finance_app.models.reddit.PostData;
import com.korih.finance_app.models.reddit.entity.CommentEntity;
import com.korih.finance_app.models.reddit.entity.PostEntity;

import java.util.HashMap;
import java.util.Map;

public class RedditMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    public static CommentData toCommentData(CommentEntity commentEntity) {
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

    public static CommentEntity toCommentEntity(CommentData commentData, PostData postData) {
        if (commentData == null || postData == null) {
            return null;
        }
        CommentEntity entity = new CommentEntity();
        entity.setId(commentData.getId());
        entity.setBody(commentData.getBody());
        entity.setAuthor(commentData.getAuthor());
        entity.setCreatedUtc(commentData.getCreatedUtc());
        entity.setParentId(commentData.getParentId());
        entity.setPostData(toEntity(postData));
        // Optionally set permalink if you add it to the entity
        // entity.setPermalink(commentData.getPermalink());
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
