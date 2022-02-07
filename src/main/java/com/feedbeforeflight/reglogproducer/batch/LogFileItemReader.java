package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class LogFileItemReader implements ItemReader<LogFileItem> {
    @Override
    public LogFileItem read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        

        return null;
    }
}
