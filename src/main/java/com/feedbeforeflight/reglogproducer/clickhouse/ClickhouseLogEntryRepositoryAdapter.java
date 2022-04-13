package com.feedbeforeflight.reglogproducer.clickhouse;

import com.feedbeforeflight.onec.reglog.data.LogFileItem;
import com.feedbeforeflight.reglogproducer.LogFileItemRepository;
import com.feedbeforeflight.reglogproducer.elastic.ElasticEntityLogFileItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.List;

public class ClickhouseLogEntryRepositoryAdapter implements LogFileItemRepository {

    private static final String dbUrl = "jdbc:clickhouse://localhost:8123/default";
    private Connection connection;

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;

    public ClickhouseLogEntryRepositoryAdapter(JdbcTemplate jdbcTemplate, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
    }

    @PostConstruct
    public void init() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);

        if (!tableExists()) { ensureTable(); }
    }

    @Override
    public void saveAll(List<? extends LogFileItem> list) {

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
                (List<ClickhouseEntityLogFileItem>) list,
                list.size(),
                new ParameterizedPreparedStatementSetter<ClickhouseEntityLogFileItem>() {
                    @Override
                    public void setValues(PreparedStatement ps, ClickhouseEntityLogFileItem argument) throws SQLException {
//                        argument.createId(argument.getFileName(), argument.getRowNumber());
                        ps.setString(1, argument.getFileName());
                        ps.setDate(2, new Date(argument.getTimestamp().getTime()));
                        ps.setInt(3, argument.getTransactionState().ordinal());
//                        ps.setDate(4, argument.getTransactionDate() == null ? new Date(0) : new Date(argument.getTransactionDate().getTime()));
                        ps.setDate(4, argument.getTransactionDate() == null ? null : new Date(argument.getTransactionDate().getTime()));
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
                    }
                });
    }

    private int getMaximumRowNumber() {
        String sql = "SELECT MAX(row_number) FROM " + tableName + " WHERE file_name=?";

        return 0;
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
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + "(\n" +
                "file_name          String,\n" +
                "row_number          Int32,\n" +
                "timestamp  DateTime,\n" +
                "transaction_state          Enum8('NONE' = 1, 'UPDATED' = 2, 'RUNNING' = 3, 'CANCELLED' = 4),\n" +
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
                "PRIMARY KEY (id)\n" +
                ")\n" +
                "engine=MergeTree()\n" +
                "PARTITION BY toYYYYMM(timestamp) ORDER BY id";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
    }

}
