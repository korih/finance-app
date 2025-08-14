package com.korih.finance_app.tasks.reddit;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.korih.finance_app.config.RedditConfig;
import com.korih.finance_app.models.ApiResponse;
import com.korih.finance_app.models.reddit.RedditThing;
import com.korih.finance_app.models.reddit.data.CommentData;
import com.korih.finance_app.models.reddit.data.ListingData;
import com.korih.finance_app.models.reddit.data.PostData;
import com.korih.finance_app.repository.CommentDataRepository;
import com.korih.finance_app.repository.PostDataRepository;
import com.korih.finance_app.services.WebClient;

@Service
@RequiredArgsConstructor
public class RedditSubredditApi {
    private final RedditConfig redditConfig;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final PostDataRepository postDataRepository;
    private final CommentDataRepository commentDataRepository;
    private final Cache<String, Integer> postCache = initCache();
    private final Cache<String, Integer> commentsCache = initCache();

    /**
     * Will periodically fetch the latest posts from the WallStreetBets subreddit.
     * If a new post is found it will be then queried directly for its content and
     * comments
     */
    public void execute(String subreddit) {
        try {
            // temp to 1 so i don't spam db
            String postsUrl = String.format("https://www.reddit.com/r/%s/new.json?limit=1000", subreddit);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(postsUrl))
                    .header("User-Agent", redditConfig.getUserAgent())
                    .GET()
                    .build();
            var response = webClient.request(request, RedditThing.class);
            parseAndSavePost(response);
        } catch (Exception e) {
            System.out.println("Error fetching Reddit posts: " + e.getMessage());
        }
    }

    private void parseAndSavePost(ApiResponse<RedditThing> response) {
        RedditThing listResponse = response.getData();
        List<PostData> postDataList = new ArrayList<>();
        ListingData listingData = objectMapper.convertValue(listResponse.getData(), ListingData.class);
        Arrays.stream(listingData.getChildren()).forEach(child -> {
            PostData postData = (PostData) child.getData();
            if (postCache.getIfPresent(postData.getId()) != null
                    && postData.getNumComments() == postData.getNumComments()) {
                return; // Post already processed
            }

            postCache.put(postData.getId(), postData.getNumComments());
            postDataList.add(postData);
            // Save the post data to the repository
            var entity = PostData.toEntity(postData);
            postDataRepository.save(entity);

            // lookup comments for each post found
            String postUrl = "https://www.reddit.com%s.json";
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(postUrl, postData.getPermalink())))
                    .header("User-Agent", redditConfig.getUserAgent())
                    .GET()
                    .build();
            var postResponse = webClient.request(postRequest, JsonNode.class);
            List<CommentData> comments = new ArrayList<>();
            parseAndSaveComments(postResponse.getData(), comments);
            comments.forEach(comment -> {
                commentDataRepository.save(CommentData.toEntity(comment, postData));
            });
        });
    }

    private void parseAndSaveComments(JsonNode postResponse, List<CommentData> comments) {
        if (postResponse.has("id")) {
            CommentData commentData = objectMapper.convertValue(postResponse, CommentData.class);
            if (commentData.getBody() == null || commentData.getId() == null
                    || commentsCache.getIfPresent(commentData.getId()) != null) {
                return;
            }
            commentsCache.put(commentData.getId(), 1);
            comments.add(commentData);
        }
        if (postResponse.isArray()) {
            for (JsonNode child : postResponse) {
                parseAndSaveComments(child, comments);
            }
            return;
        }
        if (!postResponse.has("data")
                && !postResponse.has("children")
                && !postResponse.has("replies")) {
            return; // No comments found
        }
        if (postResponse.has("data")) {
            parseAndSaveComments(postResponse.get("data"), comments);
            return;
        }
        if (postResponse.has("replies")) {
            for (JsonNode child : postResponse.get("replies")) {
                parseAndSaveComments(child, comments);
            }
            return;
        }
        if (postResponse.has("children")) {
            for (JsonNode child : postResponse.get("children")) {
                parseAndSaveComments(child, comments);
            }
            return;
        }
    }

    private Cache<String, Integer> initCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofDays(1))
                .build();
    }
}