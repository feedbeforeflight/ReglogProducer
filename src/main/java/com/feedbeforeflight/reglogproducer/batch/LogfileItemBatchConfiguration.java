package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.reglogproducer.dictionary.Dictionary;
import com.feedbeforeflight.reglogproducer.logfile.LogfileDescription;
import com.feedbeforeflight.reglogproducer.logfile.LogfileFilesList;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.net.MalformedURLException;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class LogfileItemBatchConfiguration {

    private ApplicationContext applicationContext;
    private final StepBuilderFactory stepBuilderFactory;
    private final Dictionary dictionary;

    @Value("${timezone}")
    private int timezone;
    @Value("${database-name}")
    private String databaseName;

    public LogfileItemBatchConfiguration(StepBuilderFactory stepBuilderFactory, Dictionary dictionary) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.dictionary = dictionary;
    }

    @Bean("loadLogfilesJob")
    public Job loadLogfilesJob(JobBuilderFactory jobs, LogfileFilesList logfileFilesList) throws MalformedURLException {

//        logfileFilesList.init();
        List<LogfileDescription> fileDescriptionListToLoad = logfileFilesList.getFileDescriptionListToLoad();
        if (fileDescriptionListToLoad.size() == 0) {
            return null;
        }

        JobBuilder loadLogfilesJob = jobs.get("loadLogfilesJob").incrementer(new RunIdIncrementer());
        SimpleJobBuilder simpleJobBuilder = loadLogfilesJob.start(getLoadLogfileStep(fileDescriptionListToLoad.get(0)));
        for (int i = 1; i < fileDescriptionListToLoad.size(); i++) {
            simpleJobBuilder.next(getLoadLogfileStep(fileDescriptionListToLoad.get(i)));
        }

        return simpleJobBuilder.build();
    }

    private Step getLoadLogfileStep(LogfileDescription logfileDescription) throws MalformedURLException {
        return stepBuilderFactory.get("loadLogfileStep")
                .<LogfileItem, LogfileItem>chunk(10)
                .reader(createReader(logfileDescription.getFilePath().toString(), logfileDescription.getItemsProcessed()))
                .processor(createItemProcessor())
                .writer(createItemWriter())
                .listener(new LogfileItemStepExecutionListener(logfileDescription))
                .build();
    }

    private ItemReader<LogfileItem> createReader(String filename, int skipItems) throws MalformedURLException {
        FlatFileItemReader<LogfileItem> reader = new FlatFileItemReader<>();
        reader.setRecordSeparatorPolicy(new LogfileItemSeparatorPolicy());
        reader.setResource(new FileSystemResource(filename));
        reader.setLinesToSkip(3 + skipItems);

        DefaultLineMapper<LogfileItem> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(new LogfileLineTokenizer());
        lineMapper.setFieldSetMapper(new LogfileItemFieldSetMapper(dictionary, timezone, databaseName));

        reader.setLineMapper(lineMapper);

        return reader;
    }

    private ItemProcessor<LogfileItem, LogfileItem> createItemProcessor() {
        return logfileItem -> logfileItem;
    }

    private ItemWriter<LogfileItem> createItemWriter() {
        return new LogFileItemWriter();
    }

}
