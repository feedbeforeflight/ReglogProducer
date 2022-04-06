package com.feedbeforeflight.reglogproducer.clickhouse;

import com.feedbeforeflight.reglogproducer.LogFileItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        name = "target-database",
        havingValue = "clickhouse"
)
@ComponentScan
@Slf4j
public class ClickhouseRepositoryConfiguration {

    @Bean
    public LogFileItemRepository clickhouseRepository() {
        log.info("Starting up Clickhouse repository");
        return new ClickhouseLogEntryRepositoryAdapter();
    }
}
