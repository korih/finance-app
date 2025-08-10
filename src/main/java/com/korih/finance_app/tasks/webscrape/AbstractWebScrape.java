package com.korih.finance_app.tasks.webscrape;

import com.korih.finance_app.tasks.AbstractTask;

public abstract class AbstractWebScrape extends AbstractTask {

    /**
     * Executes the web scraping task.
     */
    @Override
    public abstract void execute();

    /**
     * Returns the name of the web scraping task.
     *
     * @return the name of the task
     */
    public abstract String getName();
  
}
