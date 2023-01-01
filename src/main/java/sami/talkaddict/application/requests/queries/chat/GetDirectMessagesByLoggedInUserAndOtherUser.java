package sami.talkaddict.application.requests.queries.chat;

import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import javafx.collections.ObservableList;
import sami.talkaddict.application.models.chat.DirectMessageFx;
import sami.talkaddict.application.models.chat.DirectMessageListViewModel;

public class GetDirectMessagesByLoggedInUserAndOtherUser {
    public record Query(
            DirectMessageListViewModel dto,
            int loggedInUserId,
            int otherUserId
    ) implements an.awesome.pipelinr.Command<Result<ObservableList<DirectMessageFx>, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Query, Result<ObservableList<DirectMessageFx>, Exception>> {
        private final Logger _logger;

        public Handler(Logger logger) {
            _logger = logger;
        }

        @Override
        public Result<ObservableList<DirectMessageFx>, Exception> handle(Query query) {
            _logger.info("GetDirectMessagesByLoggedInUserAndOther Use Case invoked");
            var loggedInUserId = query.loggedInUserId;
            var otherUserId = query.otherUserId;
            var dto = query.dto;

            try {
                dto.initDirectMessagesByReceiverIdAndSenderIdAndViceVersa(loggedInUserId, otherUserId);
            } catch (Exception ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            return Result.ok(dto.directMessagesProperty().get());
        }
    }
}
