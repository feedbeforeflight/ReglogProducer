package com.feedbeforeflight.onec.reglog.reader;

import org.springframework.util.Assert;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

public abstract class AbstractLogFileReader {

    protected LineNumberReader reader;
    private String fileName;

    public AbstractLogFileReader() {}
    public AbstractLogFileReader(String fileName) { this.fileName = fileName; }

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
//        reader.readLine();
//        reader.readLine();
    }

    public void closeFile() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    public List<String> read() throws IOException {
        Assert.notNull(reader, "File must be opened before read");

        String line = readSeparatedLine();

        if (line == null) {return null;};

        return tokenize(line);
    }


    protected abstract String readSeparatedLine() throws IOException;

    protected abstract List<String> tokenize(String line);

}
