package sami.talkaddict.di;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.entities.BaseEntity;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.DatabaseManager;
import sami.talkaddict.infrastructure.dao.UserDao;
import sami.talkaddict.infrastructure.utils.managers.DotenvManager;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;

public class InjectorModule {
    private static Logger _logger;
    private static DatabaseManager _databaseManager;
    private static HashMap<Type, GenericDao> _daos = new HashMap<>();

    public static void init() throws ApplicationException, SQLException {
        if (_logger == null) {
            _logger = LoggerFactory.getLogger(InjectorModule.class);
        }
        _logger.info("Initializing infrastructure module...");

        String jdbcUrl = DotenvManager.get(Config.Database.JDBC_URL);
        _logger.info("JDBC URL: " + jdbcUrl);

        if (_databaseManager == null) {
            _databaseManager = new DatabaseManager(_logger, jdbcUrl);
        }
        _daos.put(User.class, new UserDao(_logger, _databaseManager));
    }

    public static <T extends BaseEntity> GenericDao<T> getDao(Class<T> type) {
        return (GenericDao<T>) _daos.get(type);
    }
}
