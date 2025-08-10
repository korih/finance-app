package com.korih.finance_app.tasks.api;

import org.springframework.stereotype.Service;

import com.korih.finance_app.config.RedditConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedditApi extends AbstractApi {
  private final RedditConfig redditConfig;

    @Override
    public void execute() {
        // Implementation for executing the Reddit API task
        System.out.println("Executing Reddit API task...");
        System.out.println("Config test: " + redditConfig.getClientId());
        // Add your logic here
    }

    @Override
    public String getName() {
        return "Reddit API Task";
    }
  
}
