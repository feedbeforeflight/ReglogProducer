package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.enterprise1cfiles.reglog.dictionary.Dictionary;
import com.feedbeforeflight.enterprise1cfiles.reglog.reader.DictionaryFileFieldsMapper;
import com.feedbeforeflight.enterprise1cfiles.reglog.reader.DictionaryFileItemReader;
import com.feedbeforeflight.enterprise1cfiles.reglog.reader.DictionaryFileRecord;
import com.feedbeforeflight.enterprise1cfiles.reglog.reader.DictionaryObjectCreator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
public class DictionaryBatchConfiguration {

    @Value("${log-directory-name}")
    private String logDirectoryName;

    @Bean
    public Dictionary dictionary() {
        return new Dictionary();
    }

    @Bean
    public DictionaryObjectCreator dictionaryObjectCreator(Dictionary dictionary) {
        return new DictionaryObjectCreator(dictionary);
    }

    @Bean
    public DictionaryFileItemReader dictionaryFileItemReader() throws IOException {
        DictionaryFileItemReader dictionaryFileItemReader = new DictionaryFileItemReader(Paths.get(logDirectoryName, "1Cv8.lgf").toString());
        dictionaryFileItemReader.openFile();
        return dictionaryFileItemReader;
    }

    @Bean
    public ItemReader<DictionaryFileRecord> reader(DictionaryFileItemReader dictionaryFileItemReader) {
        return () -> DictionaryFileFieldsMapper.mapFields(dictionaryFileItemReader.readTokenizedLine());
    }

    @Bean
    public ItemProcessor<DictionaryFileRecord, DictionaryFileRecord> processor() {
        return dictionaryRecord -> dictionaryRecord;
    }

    @Bean
    public Job loadDictionaryJob(JobBuilderFactory jobs,
                                 @Qualifier("loadDictionaryStep") Step loadDictionaryStep) {
        return jobs.get("loadDictionaryJob")
                .incrementer(new RunIdIncrementer())
                .start(loadDictionaryStep)
                .build();
    }

    @Bean
    public Step loadDictionaryStep(StepBuilderFactory stepBuilderFactory, ItemReader<DictionaryFileRecord> itemReader,
                                   ItemWriter<DictionaryFileRecord> itemWriter, ItemProcessor<DictionaryFileRecord, DictionaryFileRecord> itemProcessor,
                                   DictionaryLoadCompleteListener listener) {
        return stepBuilderFactory.get("loadDictionaryStep")
                .<DictionaryFileRecord, DictionaryFileRecord>chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .listener(listener)
                .build();
    }

}
