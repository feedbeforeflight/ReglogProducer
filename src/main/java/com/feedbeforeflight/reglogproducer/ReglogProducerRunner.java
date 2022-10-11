package com.feedbeforeflight.reglogproducer;

import com.feedbeforeflight.enterprise1cfiles.reglog.reader.LogfileFilesList;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ReglogProducerRunner implements CommandLineRunner, ApplicationContextAware {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("loadDictionaryJob")
    private Job dictionaryJob;
//    @Autowired
//            @Qualifier("loadLogfilesJob")
//    private Job logfileJob;
    @Autowired
    private LogfileFilesList filesList;
    @Autowired(required = false)
    private RestHighLevelClient restHighLevelClient;

    @Value("${elastic-index-name}")
    private String elasticIndexName;

    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        log.info("Run, Forrest, run...");

        if (filesList.getFileDescriptionListToLoad().size() == 0) {
            log.info("Nothing to read. Exit.");
            closeClient();
            return;
        }

        JobParameters dictionaryJobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(dictionaryJob, dictionaryJobParameters);

        Job logfileJob = (Job) applicationContext.getBean("loadLogfilesJob");

        JobParameters logfileJobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(logfileJob, logfileJobParameters);

        // have to close client explicitly, otherwise program will not exit
        closeClient();
    }

    private void closeClient() throws IOException {
        if (restHighLevelClient != null) {
            restHighLevelClient.getLowLevelClient().close();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
