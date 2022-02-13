package com.feedbeforeflight.reglogproducer.logfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
        LogfileDescription logfileDescription = new LogfileDescription(filePath);
        logfileDescription.load();
        logfileDescriptionList.add(logfileDescription);
    }

    public List<LogfileDescription> getFileDescriptionListToLoad() {
        return logfileDescriptionList.stream().filter(LogfileDescription::isSubjectToRead).collect(Collectors.toList());
    }

}
