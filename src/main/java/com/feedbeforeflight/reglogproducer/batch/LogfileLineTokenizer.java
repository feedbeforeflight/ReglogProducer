package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;

import java.util.ArrayList;
import java.util.List;

public class LogfileLineTokenizer extends AbstractLineTokenizer {

    @Override
    protected List<String> doTokenize(String logLine) {
        List<String> tokens = new ArrayList();

        int start = 1;
        boolean inParenthesis = false;
        int parenthesisCount = 0;
        boolean inQuotes = false;

        for (int i = 1; i < logLine.length(); i++) {
            char currentChar = logLine.charAt(i);
            if (currentChar == ',' && !inParenthesis && !inQuotes) {
                tokens.add(logLine.substring(start, i));
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
                if (logLine.charAt(i + 1) == '"') { // double quotes
                    i++;
                    continue;
                }
                else {
                    inQuotes = !inQuotes;
                }
            }
        }
        tokens.add(logLine.substring(start, logLine.length() - 2));

        return tokens;
    }
}
