package com.korih.finance_app.tasks.api;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korih.finance_app.config.RedditConfig;
import com.korih.finance_app.models.ApiMessageEnum;
import com.korih.finance_app.models.ApiResponse;
import com.korih.finance_app.models.reddit.CommentData;
import com.korih.finance_app.models.reddit.ListResponse;
import com.korih.finance_app.models.reddit.PostData;
import com.korih.finance_app.repository.CommentDataRepository;
import com.korih.finance_app.repository.PostDataRepository;
import com.korih.finance_app.services.RedditMapper;
import com.korih.finance_app.services.WebClient;

@Service
@RequiredArgsConstructor
public class RedditApiScanner extends AbstractApi {
    private final RedditConfig redditConfig;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final PostDataRepository postDataRepository;
    private final CommentDataRepository commentDataRepository;

    /**
     * Will periodically fetch the latest posts from the WallStreetBets subreddit.
     * If a new post is found it will be then queried directly for its content and
     * comments
     */
    @Override
    public void execute() {
        try {
            String postsUrl = "https://www.reddit.com/r/wallstreetbets/new.json?limit=1000";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(postsUrl))
                    .header("User-Agent", redditConfig.getUserAgent())
                    .GET()
                    .build();
            var response = webClient.request(request, ListResponse.class);
            if (response.getMessage() == ApiMessageEnum.ERROR) {
                System.out.println("Error fetching Reddit posts: " + response.getStatusCode());
                return;
            }
            parseAndSavePost(response);

        } catch (Exception e) {
            System.out.println("Error fetching Reddit posts: " + e.getMessage());
        }
    }

    private void parseAndSavePost(ApiResponse<ListResponse> response) {
        ListResponse listResponse = response.getData();
        List<PostData> postDataList = new ArrayList<>();
        Arrays.stream(listResponse.getData().getChildren()).forEach(child -> {
            PostData postData = child.getData();
            postDataList.add(postData);
            // Save the post data to the repository
            var entity = RedditMapper.toEntity(postData);
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
                commentDataRepository.save(RedditMapper.toCommentEntity(comment, postData));
            });
        });
    }

    private void parseAndSaveComments(JsonNode postResponse, List<CommentData> comments) {
        if (postResponse.has("id")) {
            CommentData commentData = objectMapper.convertValue(postResponse, CommentData.class);
            if (commentData.getBody() == null || commentData.getId() == null) {
                return; 
            }
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
                && !postResponse.has("replies")
                ) {
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

    @Override
    public String getName() {
        return "Reddit WallStreetBets Scanner";
    }

}