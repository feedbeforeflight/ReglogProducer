package com.feedbeforeflight.reglogproducer.logfile;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component
public class LogfileFilesList {

    private final ArrayList<LogfileDescription> logfileDescriptions;
    @Value("${work-directory-name}")
    String workDirectoryName;

    public LogfileFilesList() {
        logfileDescriptions = new ArrayList<>();
    }


    private class LogfileDescription {
        @Getter
        private final String fileName;
        @Getter
        private Date lastModified;
        @Getter
        private long linesProcessed;

        private final SimpleDateFormat simpleDateFormat;

        private LogfileDescription(String fileName) {
            this.fileName = fileName;
            simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmmss");
        }

        private void load() throws IOException {
            Path descriptionFilePath = Paths.get(workDirectoryName, fileName);
            if (!Files.exists(descriptionFilePath)) {
                lastModified = null;
                linesProcessed = 0;
                return;
            }
            BufferedReader bufferedReader = Files.newBufferedReader(descriptionFilePath);

            String buffer = bufferedReader.readLine();
            try {
                lastModified = simpleDateFormat.parse(buffer);
            } catch (ParseException e) {
                lastModified = null;
            }

            buffer = bufferedReader.readLine();
            linesProcessed = Long.valueOf(buffer);
        }

        private void save() throws IOException {
            if (lastModified == null) {
                return;
            }

            Path descriptionFilePath = Paths.get(workDirectoryName, fileName);

            BufferedWriter bufferedWriter = Files.newBufferedWriter(descriptionFilePath);
            bufferedWriter.write(simpleDateFormat.format(lastModified) + "\n");
            bufferedWriter.write(String.valueOf(linesProcessed));
        }
    }

}
