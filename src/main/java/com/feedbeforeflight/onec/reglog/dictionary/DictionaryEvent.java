package com.feedbeforeflight.onec.reglog.dictionary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DictionaryEvent {

    @Getter
    private final int id;
    @Getter
    private final String name;

    public String Presentation() {
        return name;
    }
}
