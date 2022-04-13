package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.onec.reglog.data.LogFileItem;
import com.feedbeforeflight.reglogproducer.AbstractRepository;
import com.feedbeforeflight.reglogproducer.elastic.ElasticEntityLogFileItem;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogFileItemWriter implements ItemWriter<LogFileItem> {

    private AbstractRepository<LogFileItem> logFileItemRepository;

//    @Autowired(required = true)
//    public void setLogEntryRepository(LogEntryRepository logEntryRepository) {
//        this.logEntryRepository = logEntryRepository;
//    }

    public LogFileItemWriter(AbstractRepository<LogFileItem> logFileItemRepository) {
        this.logFileItemRepository = logFileItemRepository;
    }

    @Override
    public void write(List<? extends LogFileItem> list) throws Exception {
//        list.stream().forEach(i-> System.out.println(i.toString()));
//        List<LogfileItem> logfileItems = new ArrayList<>();
////        list.stream().forEach(i->logEntryRepository.save(new LogEntry(i.toString())));
//        list.stream().forEach(i-> logfileItems.add(new LogEntry(i.toString())));
        if (logFileItemRepository != null) {
            logFileItemRepository.saveAll(list);
        }
//        throw new Exception("dummy");
    }
}
