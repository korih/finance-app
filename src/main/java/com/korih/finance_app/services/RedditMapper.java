package com.korih.finance_app.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korih.finance_app.models.reddit.PostData;
import com.korih.finance_app.models.reddit.entity.PostEntity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class RedditMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static PostEntity toEntity(PostData postData) {
        if (postData == null) return null;
        PostEntity entity = new PostEntity();
        entity.setId(postData.getId());
        entity.setTitle(postData.getTitle());
        entity.setSelftext(postData.getSelftext());
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
        if (entity == null) return null;
        PostData postData = new PostData();
        postData.setId(entity.getId());
        postData.setTitle(entity.getTitle());
        postData.setSelftext(entity.getSelftext());
        postData.setSubreddit(entity.getSubreddit());
        postData.setAuthor(entity.getAuthor());
        postData.setCreatedUtc(entity.getCreatedUtc());
        postData.setNumComments(entity.getNumComments());
        // If rawPostData exists, add all its entries to extraFields (excluding known fields)
        if (entity.getRawPostData() != null) {
            try {
                Map<String, Object> raw = objectMapper.readValue(
                    entity.getRawPostData(),
                    new TypeReference<Map<String, Object>>() {}
                );
                // Remove known fields to avoid overwriting
                raw.remove("id");
                raw.remove("title");
                raw.remove("selftext");
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
