package com.feedbeforeflight.onec.reglog.reader;

import com.feedbeforeflight.onec.reglog.reader.DictionaryFileRecord;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;

import java.util.List;
import java.util.stream.Stream;

public class DictionaryFileFieldsMapper {

    public static DictionaryFileRecord mapFields(List<String> fields) {
        if (fields == null) {return null;}

        Assert.notEmpty(fields, "Field array should not be empty.");

        DictionaryFileRecord dictionaryRecord = new DictionaryFileRecord(Integer.parseInt(fields.get(0)), fields.size() - 1);
        dictionaryRecord.setFields(fields.stream().skip(1).toArray(String[]::new));

        return dictionaryRecord;
    }
}
