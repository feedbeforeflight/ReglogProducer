package com.feedbeforeflight.reglogproducer;

import com.feedbeforeflight.reglogproducer.logfile.LogfileFilesList;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
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
    LogfileFilesList filesList;

    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Run, Forrest, run...");

        if (filesList.getFileDescriptionListToLoad().size() == 0) {
            System.out.println("Nothing to read. Exit.");
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
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
