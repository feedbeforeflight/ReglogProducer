package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.enterprise1cfiles.reglog.reader.DictionaryObjectCreator;
import com.feedbeforeflight.enterprise1cfiles.reglog.reader.DictionaryFileRecord;
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
        for (DictionaryFileRecord dictionaryFileRecord : list) {
            dictionaryObjectCreator.addItem(dictionaryFileRecord);
        }
    }
}
