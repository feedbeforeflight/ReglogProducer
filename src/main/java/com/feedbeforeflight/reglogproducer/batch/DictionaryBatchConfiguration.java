package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.reglogproducer.dictionary.Dictionary;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.net.MalformedURLException;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
public class DictionaryBatchConfiguration {

    @Value("${log-directory-path}")
    private String logDirectoryPath;

    @Bean
    public ItemReader<DictionaryItem> reader(RecordSeparatorPolicy recordSeparatorPolicy) throws MalformedURLException {
        FlatFileItemReader<DictionaryItem> reader = new FlatFileItemReader<>();
        reader.setRecordSeparatorPolicy(recordSeparatorPolicy);
        reader.setResource(new FileSystemResource(Paths.get(logDirectoryPath, "1Cv8.lgf").toString()));
        reader.setLinesToSkip(2);

        DefaultLineMapper<DictionaryItem> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_COMMA));
        lineMapper.setFieldSetMapper(new DictionaryItemFieldSetMapper());

        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    public RecordSeparatorPolicy recordSeparatorPolicy() {
        return new DictionaryItemSeparatorPolicy();
    }

    @Bean
    public ItemProcessor<DictionaryItem, DictionaryItem> processor() {
        return dictionaryRecord -> dictionaryRecord;
    }

    @Bean
    public ItemProcessor<LogFileItem, LogFileItem> logFileItemProcessor() {
        return logFileItem -> logFileItem;
    }

    @Bean
    public Job importUserJob(JobBuilderFactory jobs,
                             @Qualifier("loadDictionaryStep") Step loadDictionaryStep,
                             @Qualifier("loadLogDataStep") Step loadLogDataStep) {
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .start(loadDictionaryStep)
                .next(loadLogDataStep)
                .build();
    }

    @Bean
    public Step loadDictionaryStep(StepBuilderFactory stepBuilderFactory, ItemReader<DictionaryItem> itemReader,
                                   ItemWriter<DictionaryItem> itemWriter, ItemProcessor<DictionaryItem, DictionaryItem> itemProcessor,
                                   DictionaryLoadCompleteListener listener) {
        return stepBuilderFactory.get("loadDictionaryStep")
                .<DictionaryItem, DictionaryItem>chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .listener(listener)
                .build();
    }

    @Bean
    public Step loadLogDataStep(StepBuilderFactory stepBuilderFactory, ItemReader<LogFileItem> itemReader,
                      ItemWriter<LogFileItem> itemWriter, ItemProcessor<LogFileItem, LogFileItem> itemProcessor) {
        return stepBuilderFactory.get("loadLogDataStep")
                .allowStartIfComplete(true)
                .<LogFileItem, LogFileItem>chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

}
