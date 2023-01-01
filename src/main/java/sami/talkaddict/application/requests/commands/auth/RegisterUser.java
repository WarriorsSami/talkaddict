package sami.talkaddict.application.requests.commands.auth;

import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.di.Config;
import sami.talkaddict.domain.entities.user.UserStatus;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.infrastructure.utils.managers.ImageManager;
import sami.talkaddict.infrastructure.utils.managers.PasswordManager;
import sami.talkaddict.infrastructure.utils.managers.PreferencesManager;

public class RegisterUser {
    public record Command(UserViewModel dto) implements an.awesome.pipelinr.Command<Result<Voidy, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Command, Result<Voidy, Exception>> {
        private final Logger _logger;

        public Handler(Logger logger) {
            _logger = logger;
        }

        @Override
        public Result<Voidy, Exception> handle(Command command) {
            _logger.info("RegisterUser Use Case invoked");

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
            registeredUser.descriptionProperty().set(Config.ValidationTweaks.DEFAULT_USER_DESCRIPTION);
            registeredUser.statusProperty().set(UserStatus.ONLINE);

            try {
                registeredUser.avatarProperty().set(ImageManager.getRandomAvatar());
                registeredUser.saveOrUpdateUser();
                registeredUser.initUserByEmail(dto.emailProperty().get());
            } catch (Exception ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            PreferencesManager.setKey(
                    Config.Preferences.LOGGED_IN_USER_ID_KEY,
                    String.valueOf(registeredUser.idProperty().get())
            );
            return Result.ok(new Voidy());
        }
    }
}
