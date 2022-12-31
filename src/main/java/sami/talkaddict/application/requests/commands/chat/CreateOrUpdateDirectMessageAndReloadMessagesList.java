package sami.talkaddict.application.requests.commands.chat;

import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import sami.talkaddict.application.models.chat.DirectMessageListViewModel;
import sami.talkaddict.application.models.chat.DirectMessageViewModel;

public class CreateOrUpdateDirectMessageAndReloadMessagesList {
    public record Command(
            DirectMessageViewModel dmDto,
            DirectMessageListViewModel dmsDto,
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
            _logger.info("CreateOrUpdateDirectMessage Use Case invoked");
            var dmDto = command.dmDto;
            var dmsDto = command.dmsDto;
            var loggedInUserId = command.loggedInUserId;
            var otherUserId = command.otherUserId;

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
