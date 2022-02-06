package com.feedbeforeflight.reglogproducer.dictionary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DictionaryAuxiliaryPort {

    @Getter
    private final int id;
    @Getter
    private final String name;

    public String Presentation() {
        return name;
    }
}
