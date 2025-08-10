package com.korih.finance_app.tasks.api;

import com.korih.finance_app.tasks.AbstractTask;

public abstract class AbstractApi extends AbstractTask {

    /**
     * Executes the API task.
     */
    @Override
    public abstract void execute();

    /**
     * Returns the name of the API task.
     *
     * @return the name of the task
     */
    public abstract String getName();
  
}
