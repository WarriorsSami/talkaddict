package sami.talkaddict.application.cqrs.commands.auth;

import an.awesome.pipelinr.Voidy;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.PreferencesManager;

public class LogoutUser {
    public record Command() implements an.awesome.pipelinr.Command<Voidy> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Command, Voidy> {
        @Override
        public Voidy handle(Command command) {
            PreferencesManager.removeKey(Config.Preferences.LOGGED_IN_USER_ID_KEY);
            return new Voidy();
        }
    }
}
