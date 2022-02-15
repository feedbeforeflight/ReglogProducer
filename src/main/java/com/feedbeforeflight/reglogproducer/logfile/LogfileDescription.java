package com.feedbeforeflight.reglogproducer.logfile;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class LogfileDescription {
    @Getter
    private final Path filePath;
    @Getter
    private Date lastModified;
    @Getter
    private int itemsProcessed;

    private final SimpleDateFormat simpleDateFormat;

    String workDirectoryName;

    LogfileDescription(Path filePath, String workDirectoryName) {
        this.filePath = filePath;
        this.workDirectoryName = workDirectoryName;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmmss");
    }

    public void load() {
        Path descriptionFilePath = Paths.get(workDirectoryName, filePath.getFileName().toString() + ".rpf");
        if (!Files.exists(descriptionFilePath)) {
            lastModified = null;
            itemsProcessed = 0;
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
            itemsProcessed = Integer.parseInt(buffer);
        } catch (IOException e) {
            log.debug("Error loading logfile description file: ");
        }
    }

    public void save() {
        if (lastModified == null) {
            return;
        }

        Path descriptionFilePath = Paths.get(workDirectoryName, filePath.getFileName().toString() + ".rpf");

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(descriptionFilePath)) {
            bufferedWriter.write(simpleDateFormat.format(lastModified) + "\n");
            bufferedWriter.write(String.valueOf(itemsProcessed));
        } catch (IOException e) {
            log.debug("Error saving logfile description file: ", e);
        }
    }

    public boolean isSubjectToRead() {
        if (lastModified == null ) {
            return true;
        }

        BasicFileAttributes basicFileAttributes = null;
        try {
            basicFileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);
            Date date = new Date((basicFileAttributes.lastModifiedTime().toMillis()/1000)*1000);
            return date.after(lastModified);
        } catch (IOException e) {
            log.debug("Error reading logfile attributes: ", e);
            return false;
        }
    }

    public void readDone(int itemsProcessed) {
        this.itemsProcessed += itemsProcessed;

        BasicFileAttributes basicFileAttributes = null;
        try {
            basicFileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);
            lastModified = new Date(basicFileAttributes.lastModifiedTime().toMillis());
        } catch (IOException e) {
            log.debug("Error reading logfile attributes: ", e);
            e.printStackTrace();
        }
    }

}
