package sami.talkaddict.application.requests.commands.chat;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import dev.kylesilver.result.UnwrapException;
import sami.talkaddict.application.models.chat.DirectMessageListViewModel;
import sami.talkaddict.application.models.chat.DirectMessageViewModel;
import sami.talkaddict.application.requests.helpers.GetLoggedInUserId;

public class CreateOrUpdateDirectMessageAndReloadMessagesList {
    public record Command(
            DirectMessageViewModel dmDto,
            DirectMessageListViewModel dmsDto,
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
            _logger.info("CreateOrUpdateDirectMessage Use Case invoked");
            var dmDto = command.dmDto;
            var dmsDto = command.dmsDto;
            var otherUserId = command.otherUserId;

            Integer loggedInUserId = 0;
            try {
                loggedInUserId = GetLoggedInUserId.execute(_mediator, _logger).unwrap();
            } catch (UnwrapException ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            try {
                dmDto.saveOrUpdateDirectMessage(loggedInUserId, otherUserId);
                dmsDto.initDirectMessagesByReceiverIdAndSenderIdAndViceVersa(loggedInUserId, otherUserId);
            } catch (Exception ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            return Result.ok(new Voidy());
        }
    }
}
