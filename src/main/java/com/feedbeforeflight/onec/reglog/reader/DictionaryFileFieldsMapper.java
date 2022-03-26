package com.feedbeforeflight.onec.reglog.reader;

import com.feedbeforeflight.onec.reglog.reader.DictionaryFileRecord;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;

import java.util.stream.Stream;

public class DictionaryFileFieldsMapper {

    public static DictionaryFileRecord mapFields(String[] fields) {
        Assert.notEmpty(fields, "Field array should not be empty.");

        DictionaryFileRecord dictionaryRecord = new DictionaryFileRecord(Integer.parseInt(fields[0]), fields.length - 1);
        dictionaryRecord.setFields(Stream.of(fields).skip(1).toArray(String[]::new));

        return dictionaryRecord;
    }
}
