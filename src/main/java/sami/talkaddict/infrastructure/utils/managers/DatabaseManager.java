package sami.talkaddict.infrastructure.utils.managers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import sami.talkaddict.di.Config;
import sami.talkaddict.domain.entities.chat.DirectMessage;
import sami.talkaddict.domain.entities.user.User;

import java.sql.SQLException;

public class DatabaseManager {
    private final Logger _logger;
    private final String JDBC_URL;
    private ConnectionSource _conn;

    private static final int SCHEMA_VERSION = 1;

    public DatabaseManager(Logger logger, String jdbcUrl) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger is null");
        }
        _logger = logger;
        if (jdbcUrl == null) {
            throw new IllegalArgumentException("JDBC URL is null");
        }
        JDBC_URL = jdbcUrl;

        _logger.info("Initializing database...");
        createConnectionSource();
        if (DotenvManager.get(Config.Database.APPLY_DB_MIGRATIONS).equals("true")) {
//            dropTables();
            createTables();
//            populateTables();
        }
        closeConnectionSource();
    }

    public synchronized ConnectionSource getConnectionSource() {
        if (_conn == null) {
            createConnectionSource();
        }
        return _conn;
    }

    public synchronized void closeConnectionSource() {
        if (_conn != null) {
            try {
                _conn.close();
            } catch (SQLException ex) {
                _logger.error(ex, "Error closing connection source: " + ex.getMessage(), ex.getStackTrace());
            }
        }
    }

    private void createConnectionSource() {
        try {
            _conn = new JdbcConnectionSource(JDBC_URL);
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating connection source: " + ex.getMessage(), ex.getStackTrace());
        }
    }

    private void createTables() {
        try {
            TableUtils.createTableIfNotExists(_conn, User.class);
            TableUtils.createTableIfNotExists(_conn, DirectMessage.class);
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating tables: " + ex.getMessage(), ex.getStackTrace());
        }
    }

//    private void populateTables() {
//        try {
//            final var userDataPath = DotenvManager.get(Config.Database.POPULATE_DB_DIRECTORY) + Config.Database.USER_DATA_FILENAME;
//            final List<User> userData = FileIOManager.readCsvFile(userDataPath);
//        } catch (Exception ex) {
//            _logger.error(ex, "Error populating tables: " + ex.getMessage(), ex.getStackTrace());
//        }
//    }

    private void dropTables() {
        try {
            TableUtils.dropTable(_conn, User.class, true);
        } catch (SQLException ex) {
            _logger.error(ex, "Error dropping tables: " + ex.getMessage(), ex.getStackTrace());
        }
    }
}
