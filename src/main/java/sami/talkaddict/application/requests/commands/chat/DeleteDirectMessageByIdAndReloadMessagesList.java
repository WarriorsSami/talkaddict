package sami.talkaddict.application.requests.commands.chat;

import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import sami.talkaddict.application.models.chat.DirectMessageListViewModel;

public class DeleteDirectMessageByIdAndReloadMessagesList {
    public record Command(
            DirectMessageListViewModel dto,
            int id,
            int loggedInUserId,
            int otherUserId
    ) implements an.awesome.pipelinr.Command<Result<Voidy, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Command, Result<Voidy, Exception>> {
        private final Logger _logger;

        public Handler(Logger logger) {
            _logger = logger;
        }

        @Override
        public Result<Voidy, Exception> handle(Command command) {
            _logger.info("DeleteDirectMessageByIdAndOtherUser Use Case invoked");
            var dto = command.dto;
            var id = command.id;
            var loggedInUserId = command.loggedInUserId;
            var otherUserId = command.otherUserId;

            try {
                dto.deleteDirectMessageById(id, loggedInUserId, otherUserId);
            } catch (Exception ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            return Result.ok(new Voidy());
        }
    }
}
