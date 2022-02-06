package com.feedbeforeflight.reglogproducer;

public class HelpViewer {

    public static void printHelp() {

        System.out.println("Correct usage: ");
        System.out.println("--------------");
        System.out.println("Create in this directory text file named application.properties with internal format");
        System.out.println("    log-directory=<path-to-directory-containing-logs-to-read>");
        System.out.println("    work-directory=<path-to-directory-where-support-files-will-be-stored>");
        System.out.println("(don't forget to double backslashes)");

    }

}
