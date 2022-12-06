package sami.talkaddict.application.requests.commands.profile;

import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.domain.exceptions.ApplicationException;

public class UpdateUserProfile {
    public record Command(UserViewModel dto) implements an.awesome.pipelinr.Command<Result<Voidy, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Command, Result<Voidy, Exception>> {
        private final Logger _logger;

        public Handler(Logger logger) {
            _logger = logger;
        }

        @Override
        public Result<Voidy, Exception> handle(Command command) {
            _logger.info("UpdateUserProfile Use Case invoked");

            var dto = command.dto;
            if (dto == null) {
                return Result.err(new ApplicationException("Invalid user data!"));
            }

            try {
                dto.saveOrUpdateUser();
            } catch (Exception ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            return Result.ok(new Voidy());
        }
    }
}
