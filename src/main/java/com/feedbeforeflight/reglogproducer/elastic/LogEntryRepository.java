package com.feedbeforeflight.reglogproducer.elastic;

import com.feedbeforeflight.reglogproducer.batch.LogfileItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEntryRepository extends ElasticsearchRepository<LogfileItem, String> {
//public interface LogEntryRepository {

}
