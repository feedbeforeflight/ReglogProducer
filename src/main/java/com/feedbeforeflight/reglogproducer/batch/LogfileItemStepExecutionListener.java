package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class LogfileItemStepExecutionListener implements StepExecutionListener {


    @Override
    public void beforeStep(StepExecution stepExecution) {
        // save file last modified time
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // save read rows count
        return stepExecution.getExitStatus();
    }
}
