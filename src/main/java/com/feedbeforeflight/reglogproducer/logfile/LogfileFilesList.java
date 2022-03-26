package com.feedbeforeflight.reglogproducer.logfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbeforeflight.reglogproducer.logfile.LogfileDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@PropertySource("classpath:application.properties")
public class LogfileFilesList {

    @Value("${log-directory-name}")
    private String logDirectoryName;
    @Value("${work-directory-name}")
    String workDirectoryName;

    private final ArrayList<LogfileDescription> logfileDescriptionList;

    public LogfileFilesList() {
        logfileDescriptionList = new ArrayList<>();
    }

    @PostConstruct
    private void init() {
        Path logDirectoryPath = Paths.get(logDirectoryName);
        try (Stream<Path> pathStream = Files.find(logDirectoryPath, 1, (p, attr) -> p.getFileName().toString().endsWith(".lgp"))) {
            pathStream.forEach(this::appendLogfile);
        } catch (IOException e) {
            log.debug("Error listing log files directory:", e);
        }
    }

    private void appendLogfile(Path filePath) {
        Path descriptionFilePath = Paths.get(workDirectoryName, filePath.getFileName().toString() + ".rpf");
        LogfileDescription logfileDescription;
        if (!Files.exists(descriptionFilePath)) {
            logfileDescription = new LogfileDescription(filePath, workDirectoryName);
        }
        else {
            try (BufferedReader bufferedReader = Files.newBufferedReader(descriptionFilePath)) {
                ObjectMapper objectMapper = new ObjectMapper();
                logfileDescription = objectMapper.readValue(bufferedReader, LogfileDescription.class);
                logfileDescription.setFilePath(filePath);
                logfileDescription.setWorkDirectoryName(workDirectoryName);
            } catch (IOException e) {
                log.debug("Error loading logfile description file ", e);
                logfileDescription = new LogfileDescription(filePath, workDirectoryName);
            }
        }

//        logfileDescription.load();
        logfileDescriptionList.add(logfileDescription);
    }

    public List<LogfileDescription> getFileDescriptionListToLoad() {
        return logfileDescriptionList.stream().filter(LogfileDescription::isSubjectToRead).collect(Collectors.toList());
    }

}
