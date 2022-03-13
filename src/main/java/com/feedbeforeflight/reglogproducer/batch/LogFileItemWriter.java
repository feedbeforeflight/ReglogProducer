package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.reglogproducer.elastic.LogEntry;
import com.feedbeforeflight.reglogproducer.elastic.LogEntryRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LogFileItemWriter implements ItemWriter<LogfileItem> {

    private final LogEntryRepository logEntryRepository;

    public LogFileItemWriter(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    @Override
    public void write(List<? extends LogfileItem> list) throws Exception {
//        list.stream().forEach(i-> System.out.println(i.toString()));
//        List<LogfileItem> logfileItems = new ArrayList<>();
////        list.stream().forEach(i->logEntryRepository.save(new LogEntry(i.toString())));
//        list.stream().forEach(i-> logfileItems.add(new LogEntry(i.toString())));
        logEntryRepository.saveAll(list);
//        throw new Exception("dummy");
    }
}
