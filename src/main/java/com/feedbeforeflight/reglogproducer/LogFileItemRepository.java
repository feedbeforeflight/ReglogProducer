package com.feedbeforeflight.reglogproducer;

import com.feedbeforeflight.enterprise1cfiles.reglog.data.LogFileItem;

import java.util.List;

public interface LogFileItemRepository {
//    public interface LogFileItemRepository extends AbstractRepository<LogFileItem> {

    public void saveAll(List<? extends LogFileItem> list);


}
