package sami.talkaddict.application.cqrs.commands.auth;

import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.dao.UserDao;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.PasswordManager;
import sami.talkaddict.infrastructure.utils.managers.PreferencesManager;

public class LoginUser {
    public record Command(UserViewModel dto) implements an.awesome.pipelinr.Command<Result<Voidy, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Command, Result<Voidy, Exception>> {
        private final Logger _logger;
        private final GenericDao<User> _userDao;

        public Handler(Logger logger, GenericDao<User> userDao) {
            _logger = logger;
            _userDao = userDao;
        }

        @Override
        public Result<Voidy, Exception> handle(Command command) {
            _logger.info("LoginUser Use Case invoked");
            var dto = command.dto;
            if (dto == null) {
                return Result.err(new ApplicationException("Invalid login credentials!"));
            }

            User user;
            try {
                user = ((UserDao) _userDao).findByEmail(dto.emailProperty().get());
                if (user == null) {
                    return Result.err(new ApplicationException("Invalid login credentials!"));
                }
            } catch (ApplicationException e) {
               return Result.err(e);
            }

            if (!PasswordManager.verify(dto.passwordProperty().get(), user.getPassword())) {
                return Result.err(new ApplicationException("Invalid login credentials!"));
            }

            PreferencesManager.setKey(Config.Preferences.LOGGED_IN_USER_ID_KEY, user.getId().toString());
            return Result.ok(new Voidy());
        }
    }
}
