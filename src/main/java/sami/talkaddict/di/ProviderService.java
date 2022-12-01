package sami.talkaddict.di;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import sami.talkaddict.application.controllers.HomeController;
import sami.talkaddict.application.controllers.LoginController;
import sami.talkaddict.application.controllers.MainController;
import sami.talkaddict.application.controllers.RegisterController;
import sami.talkaddict.application.models.user.UserViewModel;
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

public class ProviderService {
    private static Logger _logger;
    private static DatabaseManager _databaseManager;
    private static HashMap<Type, GenericDao> _daos = new HashMap<>();
    private static HashMap<Type, Logger> _loggers = new HashMap<>();

    public static void init() throws ApplicationException, SQLException {
        _loggers.put(DatabaseManager.class, LoggerFactory.getLogger(DatabaseManager.class));
        _loggers.put(UserDao.class, LoggerFactory.getLogger(UserDao.class));

        _loggers.put(UserViewModel.class, LoggerFactory.getLogger(UserViewModel.class));

        _loggers.put(LoginController.class, LoggerFactory.getLogger(LoginController.class));
        _loggers.put(RegisterController.class, LoggerFactory.getLogger(RegisterController.class));
        _loggers.put(MainController.class, LoggerFactory.getLogger(MainController.class));
        _loggers.put(HomeController.class, LoggerFactory.getLogger(HomeController.class));

        if (_logger == null) {
            _logger = LoggerFactory.getLogger(ProviderService.class);
        }
        _logger.info("Initializing infrastructure module...");

        String jdbcUrl = DotenvManager.get(Config.Database.JDBC_URL);
        _logger.info("JDBC URL: " + jdbcUrl);

        if (_databaseManager == null) {
            _databaseManager = new DatabaseManager(provideLogger(DatabaseManager.class), jdbcUrl);
        }
        _daos.put(User.class, new UserDao(_logger, _databaseManager));
    }

    public static <T extends BaseEntity> GenericDao<T> provideDao(Class<T> type) {
        return (GenericDao<T>) _daos.get(type);
    }

    public static <T> Logger provideLogger(Class<T> type) {
        return _loggers.get(type);
    }
}
