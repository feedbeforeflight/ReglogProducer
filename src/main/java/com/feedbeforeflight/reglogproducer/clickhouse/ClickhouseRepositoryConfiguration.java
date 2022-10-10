package com.feedbeforeflight.reglogproducer.clickhouse;

import com.feedbeforeflight.enterprise1cfiles.reglog.data.LogFileItemFactory;
import com.feedbeforeflight.reglogproducer.LogFileItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(
        name = "target-database",
        havingValue = "clickhouse"
)
@ComponentScan
@Slf4j
public class ClickhouseRepositoryConfiguration {

    @Value("${clickhouse.database.name}")
    private String databaseName;
    @Value("${clickhouse.table.name}")
    private String tableName;
    @Value("${clickhouse.host}")
    private String host;
    @Value("${clickhouse.port}")
    private String port;
    @Value("${spring.clickhouse.username}")
    private String username;
    @Value("${spring.clickhouse.password}")
    private String password;

    @Bean
    public LogFileItemRepository clickhouseRepository(JdbcTemplate jdbcTemplate) {
        log.info("Starting up Clickhouse repository");
        return new ClickhouseLogEntryRepositoryAdapter(jdbcTemplate, databaseName, tableName);
    }

    //@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.clickhouse.jdbc.ClickHouseDriver");
        dataSource.setUrl(getDatabaseURL());
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    LogFileItemFactory logFileItemFactory() {
        return ClickhouseEntityLogFileItem::new;
    }


    private String getDatabaseURL() {
//      url format "jdbc:clickhouse://localhost:8123/default";
        return "jdbc:clickhouse://" + host + ":" + port + "/" + databaseName;
    }

}
