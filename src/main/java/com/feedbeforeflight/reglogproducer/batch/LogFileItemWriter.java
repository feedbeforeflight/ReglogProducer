package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogFileItemWriter implements ItemWriter<LogfileItem> {

    @Override
    public void write(List<? extends LogfileItem> list) throws Exception {
//        list.stream().forEach(i-> System.out.println(i.toString()));
    }
}
