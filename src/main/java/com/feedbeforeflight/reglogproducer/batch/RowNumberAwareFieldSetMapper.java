package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public interface RowNumberAwareFieldSetMapper<T> {

    T mapFieldSet(FieldSet var1, int lineNumber) throws BindException;

}

