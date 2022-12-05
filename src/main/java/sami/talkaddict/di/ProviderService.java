package sami.talkaddict.di;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import sami.talkaddict.TalkaddictApplication;
import sami.talkaddict.application.controllers.*;
import sami.talkaddict.application.requests.commands.auth.LoginUser;
import sami.talkaddict.application.requests.commands.auth.LogoutUser;
import sami.talkaddict.application.requests.commands.auth.RegisterUser;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.domain.entities.BaseEntity;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.daos.UserDao;
import sami.talkaddict.infrastructure.utils.managers.DatabaseManager;
import sami.talkaddict.infrastructure.utils.managers.DotenvManager;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Stream;

public class ProviderService {
    private static Logger _logger;
    private static DatabaseManager _databaseManager;
    private static final HashMap<Type, GenericDao> _daos = new HashMap<>();
    private static final HashMap<Type, Logger> _loggers = new HashMap<>();
    private static Pipeline _mediator;

    public static void init() throws ApplicationException, SQLException {
        _loggers.put(TalkaddictApplication.class, LoggerFactory.getLogger(TalkaddictApplication.class));

        _loggers.put(DatabaseManager.class, LoggerFactory.getLogger(DatabaseManager.class));
        _loggers.put(UserDao.class, LoggerFactory.getLogger(UserDao.class));

        _loggers.put(UserViewModel.class, LoggerFactory.getLogger(UserViewModel.class));

        _loggers.put(LoginUser.class, LoggerFactory.getLogger(LoginUser.class));
        _loggers.put(RegisterUser.class, LoggerFactory.getLogger(RegisterUser.class));
        _loggers.put(LogoutUser.class, LoggerFactory.getLogger(LogoutUser.class));
        _loggers.put(GetLoggedInUser.class, LoggerFactory.getLogger(GetLoggedInUser.class));

        _loggers.put(LoginController.class, LoggerFactory.getLogger(LoginController.class));
        _loggers.put(RegisterController.class, LoggerFactory.getLogger(RegisterController.class));
        _loggers.put(SplashController.class, LoggerFactory.getLogger(SplashController.class));
        _loggers.put(HomeController.class, LoggerFactory.getLogger(HomeController.class));
        _loggers.put(ChatController.class, LoggerFactory.getLogger(ChatController.class));
        _loggers.put(ProfileController.class, LoggerFactory.getLogger(ProfileController.class));

        if (_logger == null) {
            _logger = LoggerFactory.getLogger(ProviderService.class);
        }

        _logger.info("Initializing infrastructure module...");
        String jdbcUrl = DotenvManager.get(Config.Database.JDBC_URL);
        _logger.info("JDBC URL: " + jdbcUrl);

        if (_databaseManager == null) {
            _databaseManager = new DatabaseManager(provideLogger(DatabaseManager.class), jdbcUrl);
        }
        _daos.put(User.class, new UserDao(provideLogger(UserDao.class), _databaseManager));
        _logger.info("Infrastructure module initialized.");

        _logger.info("Initializing application module...");
        if (_mediator == null) {
            _mediator = new Pipelinr()
                    .with(() -> Stream.of(
                            new RegisterUser.Handler(provideLogger(RegisterUser.class)),
                            new LoginUser.Handler(
                                    provideLogger(LoginUser.class),
                                    provideDao(User.class)
                            ),
                            new LogoutUser.Handler(),
                            new GetLoggedInUser.Handler(provideDao(User.class))
                    ));
        }
        _logger.info("Application module initialized.");
    }

    public static <T extends BaseEntity> GenericDao<T> provideDao(Class<T> type) {
        return _daos.get(type);
    }

    public static <T> Logger provideLogger(Class<T> type) {
        return _loggers.get(type);
    }

    public static Pipeline provideMediator() {
        return _mediator;
    }
}
