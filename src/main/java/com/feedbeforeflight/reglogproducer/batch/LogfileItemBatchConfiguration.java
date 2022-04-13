package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.onec.reglog.data.LogFileItem;
import com.feedbeforeflight.onec.reglog.data.LogFileItemFactory;
import com.feedbeforeflight.onec.reglog.dictionary.Dictionary;
import com.feedbeforeflight.onec.reglog.reader.LogFileFieldsMapper;
import com.feedbeforeflight.onec.reglog.reader.LogFileItemReader;
import com.feedbeforeflight.reglogproducer.LogFileItemRepository;
import com.feedbeforeflight.reglogproducer.elastic.ElasticEntityLogFileItem;
import com.feedbeforeflight.reglogproducer.logfile.LogfileDescription;
import com.feedbeforeflight.reglogproducer.logfile.LogfileFilesList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@EnableBatchProcessing
@Slf4j
public class LogfileItemBatchConfiguration {

    private ApplicationContext applicationContext;
    private final StepBuilderFactory stepBuilderFactory;
    private final Dictionary dictionary;
    private final LogFileItemRepository logFileItemRepository;

    @Value("${timezone}")
    private int timezone;
    @Value("${database-name}")
    private String databaseName;
    @Value("${chunk-size:1000}")
    private int chunkSize;

    public LogfileItemBatchConfiguration(StepBuilderFactory stepBuilderFactory, Dictionary dictionary, LogFileItemRepository logFileItemRepository) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.dictionary = dictionary;
        this.logFileItemRepository = logFileItemRepository;
    }

    @Bean("loadLogfilesJob")
    public Job loadLogfilesJob(JobBuilderFactory jobs, LogfileFilesList logfileFilesList, LogFileItemFactory logFileItemFactory) throws IOException {

        List<LogfileDescription> fileDescriptionListToLoad = logfileFilesList.getFileDescriptionListToLoad();
        if (fileDescriptionListToLoad.size() == 0) {
            return null;
        }

        JobBuilder loadLogfilesJob = jobs.get("loadLogfilesJob").incrementer(new RunIdIncrementer());
        SimpleJobBuilder simpleJobBuilder = loadLogfilesJob.start(getLoadLogfileStep(fileDescriptionListToLoad.get(0), logFileItemFactory));
        for (int i = 1; i < fileDescriptionListToLoad.size(); i++) {
            simpleJobBuilder.next(getLoadLogfileStep(fileDescriptionListToLoad.get(i), logFileItemFactory));
        }

        return simpleJobBuilder.build();
    }

    private Step getLoadLogfileStep(LogfileDescription logfileDescription, LogFileItemFactory logFileItemFactory) throws IOException {
        log.info("Building loading step for file " + logfileDescription.getFilePath().getFileName());
        log.info("With chunk size " + chunkSize);

        return stepBuilderFactory.get("loadLogfileStep")
                .<LogFileItem, LogFileItem>chunk(chunkSize)
                .reader(createReader(logfileDescription.getFilePath().toString(), logfileDescription.getItemsProcessed(), logFileItemFactory))
                .processor(createItemProcessor())
                .writer(createItemWriter())
                .listener(new LogfileItemStepExecutionListener(logfileDescription))
                .build();
    }

    private ItemReader<LogFileItem> createReader(String filename, int skipItems, LogFileItemFactory logFileItemFactory) throws IOException {
//        FlatFileItemReader<LogfileItem> reader = new FlatFileItemReader<>();
//        reader.setRecordSeparatorPolicy(new LogfileItemSeparatorPolicy());
//        reader.setResource(new FileSystemResource(filename));
//        reader.setLinesToSkip(3 + skipItems);
//
        Path path = Paths.get(filename).getFileName();
//        RowNumberAwareLineMapper<LogfileItem> lineMapper = new RowNumberAwareLineMapper<>();
//        lineMapper.setLineTokenizer(new LogfileLineTokenizer());
//        lineMapper.setFieldSetMapper(new LogfileItemFieldSetMapper(dictionary, timezone, databaseName, dropExtension(path.toString())));
//
//        reader.setLineMapper(lineMapper);

        LogFileItemReader logFileItemReader = new LogFileItemReader(filename);
        logFileItemReader.openFile();
        LogFileFieldsMapper logFileFieldsMapper = new LogFileFieldsMapper(dictionary,
                timezone, databaseName, dropExtension(path.toString()), logFileItemFactory);

        return () -> logFileFieldsMapper.mapFields(logFileItemReader.readTokenizedLine(), logFileItemReader.getLineNumber());
    }

    private String dropExtension(String filename) {
        if (filename == null) return null;

        int dotPosition = filename.lastIndexOf(".");
        if (dotPosition == -1) return filename;

        return filename.substring(0, dotPosition);
    }

    private ItemProcessor<LogFileItem, LogFileItem> createItemProcessor() {
        return logfileItem -> logfileItem;
    }

    private ItemWriter<LogFileItem> createItemWriter() {
        return new LogFileItemWriter(logFileItemRepository);
    }

}
