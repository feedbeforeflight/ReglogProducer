package com.feedbeforeflight.reglogproducer.logfile;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class LogfileDescription {
    @Getter
    private final String fileName;
    @Getter
    private Date lastModified;
    @Getter
    private long linesProcessed;

    private final SimpleDateFormat simpleDateFormat;

    @Value("${work-directory-name}")
    String workDirectoryName;

    LogfileDescription(String fileName) {
        this.fileName = fileName;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmmss");
    }

    public void load() {
        Path descriptionFilePath = Paths.get(workDirectoryName, fileName + ".rpf");
        if (!Files.exists(descriptionFilePath)) {
            lastModified = null;
            linesProcessed = 0;
            return;
        }
        String buffer;
        try (BufferedReader bufferedReader = Files.newBufferedReader(descriptionFilePath)) {

            buffer = bufferedReader.readLine();
            try {
                lastModified = simpleDateFormat.parse(buffer);
            } catch (ParseException e) {
                lastModified = null;
            }

            buffer = bufferedReader.readLine();
            linesProcessed = Long.valueOf(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (lastModified == null) {
            return;
        }

        Path descriptionFilePath = Paths.get(workDirectoryName, fileName + ".rpf");

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(descriptionFilePath)) {
            bufferedWriter.write(simpleDateFormat.format(lastModified) + "\n");
            bufferedWriter.write(String.valueOf(linesProcessed));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSubjectToRead() {
        if (lastModified == null ) {
            return true;
        }

        Path targetFile = Paths.get(getFileName());
        BasicFileAttributes basicFileAttributes = null;
        try {
            basicFileAttributes = Files.readAttributes(targetFile, BasicFileAttributes.class);
            Date date = new Date(basicFileAttributes.lastModifiedTime().toMillis());
            return date.after(lastModified);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
