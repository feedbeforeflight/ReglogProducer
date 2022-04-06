package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.reglogproducer.elastic.ElasticEntityLogFileItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

@NoArgsConstructor
public class RowNumberAwareLineMapper<T> implements LineMapper<T>, InitializingBean {
    @Getter @Setter
    private LineTokenizer lineTokenizer;

    @Getter @Setter
    private RowNumberAwareFieldSetMapper<T> fieldSetMapper;

    private ElasticEntityLogFileItem elasticEntityLogFileItem;

    public T mapLine(String line, int lineNumber) throws Exception {
        return this.fieldSetMapper.mapFieldSet(this.lineTokenizer.tokenize(line), lineNumber);
    }

    public void afterPropertiesSet() {
        Assert.notNull(this.lineTokenizer, "The LineTokenizer must be set");
        Assert.notNull(this.fieldSetMapper, "The FieldSetMapper must be set");
    }

}
