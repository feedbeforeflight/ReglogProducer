package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.item.ItemProcessor;

public class DictionaryItemProcessor implements ItemProcessor<DictionaryItem, DictionaryItem> {
    @Override
    public DictionaryItem process(DictionaryItem dictionaryRecord) throws Exception {
        return null;
    }
}
