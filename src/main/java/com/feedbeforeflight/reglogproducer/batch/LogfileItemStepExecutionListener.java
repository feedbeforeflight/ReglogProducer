package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.reglogproducer.logfile.LogfileDescription;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class LogfileItemStepExecutionListener implements StepExecutionListener {

    private final LogfileDescription logfileDescription;

    public LogfileItemStepExecutionListener(LogfileDescription logfileDescription) {
        this.logfileDescription = logfileDescription;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("-----------------------");
        System.out.println("Start reading log file: " + logfileDescription.getFilePath());
        System.out.println("Items processed earlier: " + logfileDescription.getItemsProcessed());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Finished reading log file");


        // save read rows count
        if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
            logfileDescription.readDone(stepExecution.getReadCount());
            logfileDescription.save();
        }

        System.out.println("Items read: " + stepExecution.getReadCount());
        System.out.println(stepExecution.getExitStatus().toString());

        return stepExecution.getExitStatus();
    }
}
