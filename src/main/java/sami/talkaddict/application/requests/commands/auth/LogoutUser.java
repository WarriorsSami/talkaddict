package sami.talkaddict.application.requests.commands.auth;

import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import sami.talkaddict.di.Config;
import sami.talkaddict.infrastructure.utils.managers.PreferencesManager;

public class LogoutUser {
    public record Command() implements an.awesome.pipelinr.Command<Voidy> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Command, Voidy> {
        private final Logger _logger;

        public Handler(Logger logger) {
            _logger = logger;
        }

        @Override
        public Voidy handle(Command command) {
            _logger.info("LogoutUser Use Case invoked");

            PreferencesManager.removeKey(Config.Preferences.LOGGED_IN_USER_ID_KEY);
            return new Voidy();
        }
    }
}
