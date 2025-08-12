package com.korih.finance_app.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.korih.finance_app.tasks.api.RedditApiScanner;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Scheduler {
  // Api's
  private final RedditApiScanner redditApi;

  @Scheduled(fixedRate = 6000000) // Runs every 6000 seconds
  public void runApiTasks() {
    redditApi.execute();
  }
}
