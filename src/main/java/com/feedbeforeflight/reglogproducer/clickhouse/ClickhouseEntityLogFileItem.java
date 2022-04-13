package com.feedbeforeflight.reglogproducer.clickhouse;

import com.feedbeforeflight.onec.reglog.data.LogFileItem;
import lombok.Getter;
import lombok.Setter;

public class ClickhouseEntityLogFileItem extends LogFileItem {
    @Getter
    @Setter
    private String id; // format: [filename without extension].[row number]

    public void createId(String fileName, int rowNumber) {
        id = fileName + "." + rowNumber;
    }

    @Override
    public String toString() {
        return id + ", " +
                timestamp.toString() + ", " +
                (transactionState == null ? "" : transactionState.getName()) + ", " +
                (transactionDate == null ? "-" : transactionDate.toString()) + ", " +
                (transactionNumber == 0 ? "-" : transactionNumber) + ", " +
                username + ", " +
                computer + ", " +
                application + ", " +
                connection + ", " +
                event + ", " +
                (eventImportance == null ? "" : eventImportance.getName()) + ", " +
                comment + ", " +
                metadata + ", " +
                data + ", " +
                dataRepresentation + ", " +
                server + ", " +
                mainPort + ", " +
                auxiliaryPort + ", " +
                session;
    }
}
