package com.feedbeforeflight.reglogproducer.elastic;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@ConditionalOnProperty(
        name = "target-database",
        havingValue = "elasticsearch"
)@Repository
public interface ElasticLogEntryRepository extends ElasticsearchRepository<ElasticEntityLogFileItem, String> {


}
