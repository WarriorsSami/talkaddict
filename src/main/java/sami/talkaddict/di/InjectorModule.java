package sami.talkaddict.di;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.entities.BaseEntity;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.DbManager;
import sami.talkaddict.infrastructure.dao.UserDao;
import sami.talkaddict.infrastructure.utils.managers.DotenvManager;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;

public class InjectorModule {
    private static Logger _logger;
    private static DbManager _dbManager;
    private static HashMap<Type, GenericDao> _daos = new HashMap<>();

    public static void init() throws ApplicationException, SQLException {
        if (_logger == null) {
            _logger = LoggerFactory.getLogger(InjectorModule.class);
        }
        _logger.info("Initializing infrastructure module...");

        String jdbcUrl = DotenvManager.get(Config.JDBC_URL);
        _logger.info("JDBC URL: " + jdbcUrl);

        if (_dbManager == null) {
            _dbManager = new DbManager(_logger, jdbcUrl);
        }
        _daos.put(User.class, new UserDao(_logger, _dbManager));
    }

    public static <T extends BaseEntity> GenericDao<T> getDao(Class<T> type) {
        return (GenericDao<T>) _daos.get(type);
    }
}
