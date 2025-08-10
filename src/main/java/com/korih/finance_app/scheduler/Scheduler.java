package com.korih.finance_app.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.korih.finance_app.tasks.api.RedditApi;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Scheduler {
  // Api's
  private final RedditApi redditApi;

  @Scheduled(fixedRate = 60000) // Runs every 60 seconds
  public void runApiTasks() {
    redditApi.execute();
  }
}
