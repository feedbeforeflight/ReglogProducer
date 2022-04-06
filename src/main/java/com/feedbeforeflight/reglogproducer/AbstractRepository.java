package com.feedbeforeflight.reglogproducer;

import java.util.List;

public interface AbstractRepository<T> {

    public void saveAll(List<? extends T> list);

}
