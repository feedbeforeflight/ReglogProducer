package com.feedbeforeflight.reglogproducer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ReglogProducerApplication {

//    @Value("${log-directory-path}")
//    private static String logDirectoryPath;
//    @Value("${work-directory-path}")
//    private static String workDirectoryPath;

    public static void main(String[] args) {
//        if (logDirectoryPath.isEmpty() || workDirectoryPath.isEmpty()) {
//            System.out.println("Some parameters are undefined!");
//            System.out.println();
//            HelpViewer.printHelp();
////            workDirectory = ReglogProducerRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//            return;
//        }
//
//        System.out.println("It works!");
//        System.out.println("Log dir : " + logDirectoryPath);
//        System.out.println("Work dir: " + workDirectoryPath);
//        System.out.println();
//
//        Path logDirectory = Paths.get(logDirectoryPath);
//
//        Path dictionaryFile = Paths.get(logDirectoryPath, "1Cv8.lgf");
//        if (!Files.exists(dictionaryFile)) {
//            System.out.println("Dictionary file 1Cv8.lgf not found in specified log directory.");
//            return;
//        }

        SpringApplication.run(ReglogProducerApplication.class, args);
    }

}
