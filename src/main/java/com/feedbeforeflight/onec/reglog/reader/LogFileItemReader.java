package com.feedbeforeflight.onec.reglog.reader;

import org.springframework.util.Assert;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class LogFileItemReader extends AbstractLogFileReader {

    public LogFileItemReader() {}
    public LogFileItemReader(String fileName) {super(fileName);}

    public void openFile() throws IOException {
        super.openFile();

        // skip 3 non-significant leading lines
        reader.readLine();
        reader.readLine();
        reader.readLine();
    }

    @Override
    protected String readSeparatedLine() throws IOException {
        StringBuilder result = new StringBuilder();

        String newLine = reader.readLine();
        if (newLine == null) {
            return null;
        }
        result.append(newLine);

        while (newLine != null) {
            newLine = reader.readLine();
            if (newLine != null) {
                result.append(newLine);
                if (newLine.equals("},")) {
                    break;
                }
            }
        }

        return result.toString();
    }

    @Override
    protected List<String> tokenize(String line) {
        List<String> tokens = new ArrayList();

        int start = 1;
        boolean inParenthesis = false;
        int parenthesisCount = 0;
        boolean inQuotes = false;

        for (int i = 1; i < line.length(); i++) {
            char currentChar = line.charAt(i);
            if (currentChar == ',' && !inParenthesis && !inQuotes) {
                tokens.add(line.substring(start, i));
                start = i + 1;
            }
            else if (currentChar == '{' && !inQuotes) {
                inParenthesis = true;
                parenthesisCount++;
            }
            else if (currentChar == '}' && !inQuotes) {
                parenthesisCount--;
                inParenthesis = parenthesisCount == 0 ? false : true;
            }
            else if (currentChar == '"') {
                if (line.charAt(i + 1) == '"') { // double quotes
                    i++;
                    continue;
                }
                else {
                    inQuotes = !inQuotes;
                }
            }
        }
        tokens.add(line.substring(start, line.length() - 2));

        return tokens;
    }

}
