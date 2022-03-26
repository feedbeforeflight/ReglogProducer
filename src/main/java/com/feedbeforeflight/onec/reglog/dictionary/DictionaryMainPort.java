package com.feedbeforeflight.onec.reglog.dictionary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DictionaryMainPort {

    @Getter
    private final int id;
    @Getter
    private final int number;

    public  int Presentation() {
        return number;
    }
}
