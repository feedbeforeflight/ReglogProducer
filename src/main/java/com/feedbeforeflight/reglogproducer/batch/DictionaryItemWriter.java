package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.reglogproducer.dictionary.Dictionary;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DictionaryItemWriter implements ItemWriter<DictionaryItem> {

    private Dictionary dictionary;

    public DictionaryItemWriter(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void write(List<? extends DictionaryItem> list) throws Exception {
//        System.out.println("Batch size: " + list.size());
        for (DictionaryItem dictionaryItem : list) {
//            System.out.println(dictionaryItem.toString());
            dictionary.addItem(dictionaryItem);
        }
    }
}
