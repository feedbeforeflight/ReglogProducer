package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.onec.reglog.dictionary.Dictionary;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class DictionaryLoadCompleteListener implements StepExecutionListener {

    private final Dictionary dictionary;

    public DictionaryLoadCompleteListener(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println();
        System.out.println("Loaded dictionary:");
        System.out.println("------------------");
        System.out.println(dictionary.summary());

        return stepExecution.getExitStatus();
    }
}
