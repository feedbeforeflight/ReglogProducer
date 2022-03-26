package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.onec.reglog.reader.DictionaryObjectCreator;
import com.feedbeforeflight.onec.reglog.reader.DictionaryFileRecord;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DictionaryItemWriter implements ItemWriter<DictionaryFileRecord> {

    private final DictionaryObjectCreator dictionaryObjectCreator;

    public DictionaryItemWriter(DictionaryObjectCreator dictionaryObjectCreator) {
        this.dictionaryObjectCreator = dictionaryObjectCreator;
    }

    @Override
    public void write(List<? extends DictionaryFileRecord> list) throws Exception {
//        System.out.println("Batch size: " + list.size());
        for (DictionaryFileRecord dictionaryFileRecord : list) {
//            System.out.println(dictionaryItem.toString());
            dictionaryObjectCreator.addItem(dictionaryFileRecord);
        }
    }
}
