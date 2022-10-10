package com.feedbeforeflight.reglogproducer.elastic;

import com.feedbeforeflight.enterprise1cfiles.reglog.data.LogFileItem;
import com.feedbeforeflight.reglogproducer.LogFileItemRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ElasticLogEntryRepositoryAdapter implements LogFileItemRepository {
    private final ElasticLogEntryRepository repository;

    public ElasticLogEntryRepositoryAdapter(ElasticLogEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<? extends LogFileItem> list) {
        repository.saveAll(list.stream().map(s->(ElasticEntityLogFileItem)s).collect(Collectors.toList()));
    }
}
