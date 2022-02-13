package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.reglogproducer.logfile.LogfileDescription;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;


public class LogfileItemReader implements ItemReader<LogfileItem> {

    private LineNumberReader reader;
    private LogfileDescription description;

    public LogfileItemReader(LogfileDescription description) {
        this.description = description;
    }

    @BeforeStep
    public void openFile() throws FileNotFoundException {
        reader = new LineNumberReader(new FileReader(description.getFilePath().toString()));
    }

    @Override
    public LogfileItem read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {


        return null;
    }

    @AfterStep
    public void closeFile() throws IOException {
        reader.close();
    }
}
