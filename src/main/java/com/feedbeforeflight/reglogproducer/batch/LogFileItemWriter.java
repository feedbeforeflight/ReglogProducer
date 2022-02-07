package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogFileItemWriter implements ItemWriter<LogFileItem> {
    @Override
    public void write(List<? extends LogFileItem> list) throws Exception {

    }
}
