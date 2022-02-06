package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class DictionaryItemFieldSetMapper implements FieldSetMapper<DictionaryItem> {
    @Override
    public DictionaryItem mapFieldSet(FieldSet fieldSet) throws BindException {
        if (fieldSet.getFieldCount() == 0) {
            throw new BindException(new DictionaryItem(0), "DictionaryRecord");
        }

        DictionaryItem dictionaryRecord = new DictionaryItem(Integer.valueOf(fieldSet.readString(0)), fieldSet.getFieldCount() - 1);
        for (int i = 1; i < fieldSet.getFieldCount(); i++) {
            dictionaryRecord.getFields()[i - 1] = fieldSet.readString(i);
        }

        return dictionaryRecord;
    }
}
