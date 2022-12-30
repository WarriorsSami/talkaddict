package sami.talkaddict.application.requests.commands.chat;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import dev.kylesilver.result.UnwrapException;
import sami.talkaddict.application.models.chat.DirectMessageListViewModel;
import sami.talkaddict.application.requests.helpers.GetLoggedInUserId;

public class DeleteDirectMessageByIdAndReloadMessagesList {
    public record Command(
            DirectMessageListViewModel dto,
            int id,
            int otherUserId
    ) implements an.awesome.pipelinr.Command<Result<Voidy, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Command, Result<Voidy, Exception>> {
        private final Logger _logger;
        private final Pipeline _mediator;

        public Handler(Logger logger, Pipeline mediator) {
            _logger = logger;
            _mediator = mediator;
        }

        @Override
        public Result<Voidy, Exception> handle(Command command) {
            _logger.info("DeleteDirectMessageByIdAndOtherUser Use Case invoked");
            var dto = command.dto;
            var id = command.id;
            var otherUserId = command.otherUserId;

            Integer loggedInUserId;
            try {
                loggedInUserId = GetLoggedInUserId.execute(_mediator, _logger).unwrap();
            } catch (UnwrapException e) {
                throw new RuntimeException(e);
            }

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
