package com.feedbeforeflight.reglogproducer;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.config.RepositoryConfigurationDelegate;

//@Configuration
//@EnableAutoConfiguration(exclude={ElasticsearchDataAutoConfiguration.class,
//        ElasticsearchRestClientAutoConfiguration.class,
//        ReactiveElasticsearchRepositoriesAutoConfiguration.class,
//        ElasticsearchRepositoriesAutoConfiguration.class})
public class ReglogProducerApplicationConfiguration {
}
