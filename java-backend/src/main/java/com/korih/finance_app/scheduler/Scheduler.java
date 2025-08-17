package com.korih.finance_app.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.korih.finance_app.tasks.reddit.RedditSubredditApi;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Scheduler {
  private final RedditSubredditApi redditApi;

  @Scheduled(fixedDelay = 600000) 
  public void tenMinTasks() {
  }

  @Scheduled(fixedDelay = 86400000) // 3 hours
  public void hourlyTasks() {
    redditApi.execute("wallstreetbets");
    redditApi.execute("daytrading");
    redditApi.execute("stockmarket");
    redditApi.execute("cryptocurrency");
    redditApi.execute("stocks");
    redditApi.execute("options");
  }
}
