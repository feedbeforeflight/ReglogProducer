package com.feedbeforeflight.reglogproducer.elastic;

import com.feedbeforeflight.enterprise1cfiles.reglog.data.LogFileItemFactory;
import com.feedbeforeflight.reglogproducer.LogFileItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;

@Configuration
@ConditionalOnProperty(
        name = "target-database",
        havingValue = "elasticsearch"
)
@EnableElasticsearchRepositories()
@ComponentScan
@Slf4j
public class ElasticRepositoryConfiguration {

    @Bean
    public RestHighLevelClient elasticClient() throws Exception {
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                .build();

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
//                .usingSsl(sslContext, NoopHostnameVerifier.INSTANCE)
                .withBasicAuth("elastic", "VFFFuiMwuJnbgHxmErWX")
                .build();


        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchRestTemplate(elasticClient());
    }

    @Bean
    public LogFileItemRepository elasticsearchRepository(ElasticLogEntryRepository logEntryRepository) {
        log.info("Starting up Elasticsearch repository");
        return new ElasticLogEntryRepositoryAdapter(logEntryRepository);
    }

    @Bean
    public LogFileItemFactory logFileItemFactory() {
        return () -> new ElasticEntityLogFileItem();
    }

}
