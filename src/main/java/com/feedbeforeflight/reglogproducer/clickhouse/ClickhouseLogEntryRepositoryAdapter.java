package com.feedbeforeflight.reglogproducer.clickhouse;

import com.feedbeforeflight.onec.reglog.data.LogFileItem;
import com.feedbeforeflight.reglogproducer.LogFileItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ClickhouseLogEntryRepositoryAdapter implements LogFileItemRepository {

    private final String dbUrl;
    private Connection connection;

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final String databaseName;

    public ClickhouseLogEntryRepositoryAdapter(JdbcTemplate jdbcTemplate, String databaseName, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.dbUrl = "jdbc:clickhouse://localhost:8123/" + databaseName;
    }

    @PostConstruct
    public void init() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);

        if (!tableExists()) { ensureTable(); }
    }

    private void doSaveAll(List<ClickhouseEntityLogFileItem> list) {

    }

    @Override
    public void saveAll(List<? extends LogFileItem> list) {

//        doSaveAll((List<ClickhouseEntityLogFileItem>) list);


        if (list.size() == 0) { return; }

        int maximumRowNumber;

        try {
            maximumRowNumber = getMaximumRowNumber(list.get(0).getFileName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<ClickhouseEntityLogFileItem> updateList;

        if (maximumRowNumber < list.get(0).getRowNumber()) {
            updateList = (List<ClickhouseEntityLogFileItem>) list;
        }
        else if (maximumRowNumber >= list.get(list.size() - 1).getRowNumber()) {
            //log.debug("Skipped " + list.size() + " of " + list.size());
            return;
        }
        else {
            updateList = (List<ClickhouseEntityLogFileItem>) list.stream().filter(item -> item.getRowNumber() > maximumRowNumber).collect(Collectors.toList());
            //log.debug("Skipped " + (list.size() - updateList.size()) + " of " + list.size());
        }

        //log.debug("Writing to database " + updateList.size() + " records. Max row number: " + maximumRowNumber);
        System.out.print("Writing to database " + updateList.size() + " records. Rows stored from file: " + maximumRowNumber + "\r");

        String sql = "INSERT INTO " + tableName + " (" +
                "file_name,\n" +
                "timestamp,\n" +
                "transaction_state,\n" +
                "transaction_date,\n" +
                "transaction_number,\n" +
                "username,\n" +
                "computer,\n" +
                "application,\n" +
                "connection,\n" +
                "event,\n" +
                "event_importance,\n" +
                "comment,\n" +
                "metadata,\n" +
                "data,\n" +
                "data_representation,\n" +
                "server,\n" +
                "main_port,\n" +
                "auxiliary_port,\n" +
                "session, \n" +
                "row_number) \n" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql,
                updateList,
                updateList.size(),
                (ps, argument) -> {
//                        argument.createId(argument.getFileName(), argument.getRowNumber());
                    ps.setString(1, argument.getFileName());
                    ps.setTimestamp(2, new Timestamp(argument.getTimestamp().getTime()));
                    ps.setInt(3, argument.getTransactionState().ordinal());
//                        ps.setTimestamp(4, argument.getTransactionDate() == null ? new Date(0) : new Timestamp(argument.getTransactionDate().getTime()));
                    ps.setTimestamp(4, argument.getTransactionDate() == null ? null : new Timestamp(argument.getTransactionDate().getTime()));
                    ps.setInt(5, argument.getTransactionNumber());
                    ps.setString(6, argument.getUsername());
                    ps.setString(7, argument.getComputer());
                    ps.setString(8, argument.getApplication());
                    ps.setInt(9, argument.getConnection());
                    ps.setString(10, argument.getEvent());
                    ps.setInt(11, argument.getEventImportance().ordinal());
                    ps.setString(12, argument.getComment());
                    ps.setString(13, argument.getMetadata());
                    ps.setString(14, argument.getData());
                    ps.setString(15, argument.getDataRepresentation());
                    ps.setString(16, argument.getServer());
                    ps.setInt(17, argument.getMainPort());
                    ps.setInt(18, argument.getAuxiliaryPort());
                    ps.setInt(19, argument.getSession());
                    ps.setInt(20, argument.getRowNumber());
                });
    }

    private int getMaximumRowNumber(String fileName) throws SQLException {
        String sql = "SELECT MAX(row_number) FROM " + databaseName + "." + tableName + " WHERE file_name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, fileName);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getInt(1);
    }

    private boolean tableExists() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
                + "FROM information_schema.tables "
                + "WHERE table_name = ? "
                + "LIMIT 1");
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getInt(1) == 1;
    }

    private void ensureTable() throws SQLException {
        log.info("Creating table " + databaseName + "." + tableName);

        String query = "CREATE TABLE IF NOT EXISTS " + databaseName + "." + tableName + "(\n" +
                "file_name          String,\n" +
                "row_number          Int32,\n" +
                "timestamp  DateTime,\n" +
                "transaction_state          Enum8('NONE' = 0, 'UPDATED' = 1, 'RUNNING' = 2, 'CANCELLED' = 3),\n" +
                "transaction_date  DateTime NULL,\n" +
                "transaction_number     Int32,\n" +
                "username          String,\n" +
                "computer          String,\n" +
                "application          String,\n" +
                "connection          String,\n" +
                "event          String,\n" +
                "event_importance          String,\n" +
                "comment          String,\n" +
                "metadata          String,\n" +
                "data          String,\n" +
                "data_representation          String,\n" +
                "server          String,\n" +
                "main_port          String,\n" +
                "auxiliary_port          String,\n" +
                "session          String,\n" +
                "PRIMARY KEY (file_name, row_number)\n" +
                ")\n" +
                "engine=MergeTree()\n" +
                "PARTITION BY toYYYYMM(timestamp) ORDER BY (file_name, row_number)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
    }

}
