package com.feedbeforeflight.reglogproducer.logfile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class LogfileFilesList {

    private final ArrayList<LogfileDescription> logfileDescriptionList;
    @Value("${work-directory-name}")
    String workDirectoryName;

    public LogfileFilesList() {
        logfileDescriptionList = new ArrayList<>();

        Path workDirectoryPath = Paths.get(workDirectoryName);
        try (Stream<Path> pathStream = Files.find(workDirectoryPath, 1, (p, attr) -> p.endsWith(".lgf"))) {
            pathStream.forEach(this::appendLogfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendLogfile(Path filePath) {
        LogfileDescription logfileDescription = new LogfileDescription(filePath.getFileName().toString());
        logfileDescription.load();
        logfileDescriptionList.add(logfileDescription);
    }

    public List<LogfileDescription> getFileDescriptionsToLoad() {

    }

}
