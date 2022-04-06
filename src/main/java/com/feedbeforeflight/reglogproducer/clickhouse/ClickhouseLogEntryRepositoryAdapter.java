package com.feedbeforeflight.reglogproducer.clickhouse;

import com.feedbeforeflight.onec.reglog.data.LogFileItem;
import com.feedbeforeflight.reglogproducer.LogFileItemRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

public class ClickhouseLogEntryRepositoryAdapter implements LogFileItemRepository {
    @Override
    public void saveAll(List<? extends LogFileItem> list) {

    }
}
