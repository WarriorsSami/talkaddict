package sami.talkaddict.infrastructure.utils.managers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.infrastructure.utils.Config;

import java.sql.SQLException;

public class DatabaseManager {
    private final Logger _logger;
    private final String JDBC_URL;
    private ConnectionSource _conn;

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
            dropTables();
            createTables();
        }
        closeConnectionSource();
    }

    public ConnectionSource getConnectionSource() {
        if (_conn == null) {
            createConnectionSource();
        }
        return _conn;
    }

    public void closeConnectionSource() {
        if (_conn != null) {
            try {
                _conn.close();
            } catch (SQLException ex) {
                _logger.error(ex, "Error closing connection source: " + ex.getMessage());
            }
        }
    }

    private void createConnectionSource() {
        try {
            _conn = new JdbcConnectionSource(JDBC_URL);
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating connection source: " + ex.getMessage());
        }
    }

    private void createTables() {
        try {
            TableUtils.createTableIfNotExists(_conn, User.class);
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating tables: " + ex.getMessage());
        }
    }

    private void dropTables() {
        try {
            TableUtils.dropTable(_conn, User.class, true);
        } catch (SQLException ex) {
            _logger.error(ex, "Error dropping tables: " + ex.getMessage());
        }
    }
}
