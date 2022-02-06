package com.feedbeforeflight.reglogproducer.batch;

import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryItemSeparatorPolicy implements RecordSeparatorPolicy {

    Pattern guidPattern;

    public DictionaryItemSeparatorPolicy() {
        guidPattern =  Pattern.compile("^\\{?\\p{XDigit}{8}-(?:\\p{XDigit}{4}-){3}\\p{XDigit}{12}}?$");
    }

    @Override
    public boolean isEndOfRecord(String s) {
        if (s.endsWith("},")) {
            return true;
        }
        if (s.endsWith("1CV8LOG(ver 2.0)")) { // to avoid "magic" EF BB BF bytes appearing in any unicode text file
            return true;
        }
        if (s.startsWith("{")) {
            if (StringUtils.countOccurrencesOf(s, "{") == StringUtils.countOccurrencesOf(s, "}")) {
                return true;
            }
        }
        else {
            Matcher m = guidPattern.matcher(s);
            return m.matches();
        }

        return false;
    }

    @Override
    public String postProcess(String s) {
        System.out.println(s);
        if (s.endsWith(",")) {
            return s.substring(1, s.length() - 2);
        }
        else {
            return s.substring(1, s.length() - 1);
        }
    }

    @Override
    public String preProcess(String s) {
        return s;
    }
}
