package com.feedbeforeflight.reglogproducer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ReglogProducerRunner implements CommandLineRunner {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running runner");
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
