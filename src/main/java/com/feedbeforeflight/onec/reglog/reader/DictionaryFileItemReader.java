package com.feedbeforeflight.onec.reglog.reader;

import org.springframework.util.Assert;

import java.io.*;

public class DictionaryFileItemReader {

    private LineNumberReader reader;
    private String fileName;

    public DictionaryFileItemReader() {

    }

    public DictionaryFileItemReader(String fileName) {
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return reader == null ? -1 : reader.getLineNumber();
    }

    public void openFile() throws IOException {
        Assert.hasText(fileName, "File name must be set");

        reader = new LineNumberReader(new FileReader(fileName));

        // skip 2 non-significant leading lines
        reader.readLine();
        reader.readLine();
    }

    public void closeFile() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    public String[] read() throws IOException {
        Assert.notNull(reader, "File must be opened before read");

        String line = readSeparatedLine();

        return tokenize(line);
    }

    private String readSeparatedLine() throws IOException {
        String line = reader.readLine();
        if (line != null) {
            if (!line.endsWith("},")) {
                String nextLine = reader.readLine();
                if (nextLine != null) {
                    line = line + nextLine;
                }
            }
        }
        return line;
    }

    private String[] tokenize(String line) {
        int cutFromTheEnd = line.endsWith(",") ? 2 : 1;
        line = line.substring(1, line.length() - cutFromTheEnd);
        return line.split(",");
    }

}
