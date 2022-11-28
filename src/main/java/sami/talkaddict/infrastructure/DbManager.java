package sami.talkaddict.infrastructure;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import sami.talkaddict.domain.User;

import java.sql.SQLException;

public class DbManager {
    private static final Logger _logger = LoggerFactory.getLogger(DbManager.class);
    private static final String JDBC_URL = "jdbc:sqlite:/media/sami/Dev Space4/JavaStuff/talkaddict/talkaddict.sqlite";

    private static ConnectionSource _conn;

    public static void initDatabase() {
        createConnectionSource();
//        dropTables();
        createTables();
        closeConnectionSource();
    }

    public static ConnectionSource getConnectionSource() {
        if (_conn == null) {
            createConnectionSource();
        }
        return _conn;
    }

    public static void closeConnectionSource() {
        if (_conn != null) {
            try {
                _conn.close();
            } catch (SQLException ex) {
                _logger.error(ex, "Error closing connection source: " + ex.getMessage());
            }
        }
    }

    private static void createConnectionSource() {
        try {
            _conn = new JdbcConnectionSource(JDBC_URL);
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating connection source: " + ex.getMessage());
        }
    }

    private static void createTables() {
        try {
            TableUtils.createTableIfNotExists(_conn, User.class);
        } catch (SQLException ex) {
            _logger.error(ex, "Error creating tables: " + ex.getMessage());
        }
    }

    private static void dropTables() {
        try {
            TableUtils.dropTable(_conn, User.class, true);
        } catch (SQLException ex) {
            _logger.error(ex, "Error dropping tables: " + ex.getMessage());
        }
    }
}
