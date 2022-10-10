package com.feedbeforeflight.reglogproducer.batch;

import com.feedbeforeflight.enterprise1cfiles.reglog.reader.LogfileUtils;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogfileItemSeparatorPolicy implements RecordSeparatorPolicy {

    private final Pattern guidPattern;

    public LogfileItemSeparatorPolicy() {
        guidPattern = Pattern.compile("^\\{?\\p{XDigit}{8}-(?:\\p{XDigit}{4}-){3}\\p{XDigit}{12}}?$");
    }

    @Override
    public boolean isEndOfRecord(String s) {
        if (s.endsWith("},")) {
            return LogfileUtils.parenthesisCompleted(s);
        }
        if (s.endsWith("1CV8LOG(ver 2.0)")) { // to avoid "magic" EF BB BF bytes appearing in any unicode text file
            return true;
        }
        if (s.endsWith("}")) {
            return LogfileUtils.parenthesisCompleted(s);
        }
        if (s.isEmpty()) { // empty 3-rd string
            return true;
        }
        else {
            Matcher m = guidPattern.matcher(s);
            return m.matches();
        }
    }

    @Override
    public String postProcess(String s) {
        return s;
    }

    @Override
    public String preProcess(String s) {
        return s;
    }
}
