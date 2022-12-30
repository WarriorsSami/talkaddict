package sami.talkaddict.application.requests.queries.chat;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import dev.kylesilver.result.UnwrapException;
import javafx.collections.ObservableList;
import sami.talkaddict.application.models.chat.DirectMessageFx;
import sami.talkaddict.application.models.chat.DirectMessageListViewModel;
import sami.talkaddict.application.requests.helpers.GetLoggedInUserId;

public class GetDirectMessagesByLoggedInUserAndOtherUser {
    public record Query(
            DirectMessageListViewModel dto,
            int otherUserId
    ) implements an.awesome.pipelinr.Command<Result<ObservableList<DirectMessageFx>, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Query, Result<ObservableList<DirectMessageFx>, Exception>> {
        private final Logger _logger;
        private final Pipeline _mediator;

        public Handler(Logger logger, Pipeline mediator) {
            _logger = logger;
            _mediator = mediator;
        }

        @Override
        public Result<ObservableList<DirectMessageFx>, Exception> handle(Query query) {
            _logger.info("GetDirectMessagesByLoggedInUserAndOther Use Case invoked");
            var otherUserId = query.otherUserId;
            var dto = query.dto;

           Integer loggedInUserId;
            try {
                loggedInUserId = GetLoggedInUserId.execute(_mediator, _logger).unwrap();
            } catch (UnwrapException e) {
                throw new RuntimeException(e);
            }

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
