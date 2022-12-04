package sami.talkaddict.application.requests.queries.auth;

import dev.kylesilver.result.Result;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.di.Config;
import sami.talkaddict.infrastructure.utils.managers.PreferencesManager;

public class GetLoggedInUser {
    public record Query() implements an.awesome.pipelinr.Command<Result<User, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Query, Result<User, Exception>> {
        private final GenericDao<User> _userDao;

        public Handler(GenericDao<User> userDao) {
            _userDao = userDao;
        }

        @Override
        public Result<User, Exception> handle(Query query) {
            var id = PreferencesManager.getKey(Config.Preferences.LOGGED_IN_USER_ID_KEY);
            if (id == null) {
                return Result.err(new ApplicationException("No user is logged in!"));
            }

            try {
                var user = _userDao.findById(Integer.parseInt(id));
                if (user == null) {
                    return Result.err(new ApplicationException("No user is logged in!"));
                }
                return Result.ok(user);
            } catch (ApplicationException e) {
                return Result.err(e);
            }
        }
    }
}
