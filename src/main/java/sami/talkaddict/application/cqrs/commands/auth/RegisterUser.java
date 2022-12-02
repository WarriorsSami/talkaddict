package sami.talkaddict.application.cqrs.commands.auth;

import an.awesome.pipelinr.Voidy;
import dev.kylesilver.result.Result;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.PasswordManager;
import sami.talkaddict.infrastructure.utils.managers.PreferencesManager;

public class RegisterUser {
    public record Command(UserViewModel dto) implements an.awesome.pipelinr.Command<Result<Voidy, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Command, Result<Voidy, Exception>> {
        @Override
        public Result<Voidy, Exception> handle(Command command) {
            var dto = command.dto;
            if (dto == null) {
                return Result.err(new ApplicationException("Invalid register data provided!"));
            }

            var rawPassword = dto.passwordProperty().get();
            var encodedPassword = PasswordManager.encode(rawPassword);

            var registeredUser = new UserViewModel();
            registeredUser.emailProperty().set(dto.emailProperty().get());
            registeredUser.usernameProperty().set(dto.usernameProperty().get());
            registeredUser.passwordProperty().set(encodedPassword);

            try {
                registeredUser.saveOrUpdateUser();
                registeredUser.initUserByEmail(dto.emailProperty().get());
            } catch (Exception e) {
                return Result.err(e);
            }

            PreferencesManager.setKey(
                    Config.Preferences.LOGGED_IN_USER_ID_KEY,
                    String.valueOf(registeredUser.idProperty().get())
            );
            return Result.ok(new Voidy());
        }
    }
}
