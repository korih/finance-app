package com.korih.finance_app.tasks.api;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.korih.finance_app.config.RedditConfig;
import com.korih.finance_app.models.ApiMessageEnum;
import com.korih.finance_app.models.reddit.ListResponse;
import com.korih.finance_app.repository.PostDataRepository;
import com.korih.finance_app.services.RedditMapper;
import com.korih.finance_app.services.WebClient;

@Service
@RequiredArgsConstructor
public class RedditApiScanner extends AbstractApi {
    private final RedditConfig redditConfig;
    private final WebClient webClient;
    private final PostDataRepository postDataRepository;

    /**
     * Will periodically fetch the latest posts from the WallStreetBets subreddit.
     * If a new post is found it will be then queried directly for its content and comments
     */
    @Override
    public void execute() {
        try {
            String url = "https://www.reddit.com/r/wallstreetbets/new.json?limit=1000";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", redditConfig.getUserAgent())
                    .GET()
                    .build();
            var response = webClient.request(request, ListResponse.class);
            if (response.getMessage() == ApiMessageEnum.SUCCESS) {
                ListResponse listResponse = response.getData();
                Arrays.stream(listResponse.getData().getChildren()).forEach(child -> {
                    var postData = child.getData();
                    // Save the post data to the repository
                    var entity = RedditMapper.toEntity(postData);
                    postDataRepository.save(entity);
                    System.out.println("Saved post: " + postData.getTitle());
                });
            }
        } catch (Exception e) {
            System.out.println("Error fetching Reddit posts: " + e.getMessage());
            // TODO: handle exception
        }
    }

    @Override
    public String getName() {
        return "Reddit WallStreetBets Scanner";
    }
}
