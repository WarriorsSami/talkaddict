package sami.talkaddict.application.requests.queries.chat;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import javafx.collections.ObservableList;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.models.user.UserListViewModel;
import sami.talkaddict.application.requests.helpers.RemoveLoggedInUserFromCollection;
import sami.talkaddict.domain.exceptions.ApplicationException;

public class GetAllUsers {
    public record Query(UserListViewModel dto) implements an.awesome.pipelinr.Command<Result<ObservableList<UserFx>, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Query, Result<ObservableList<UserFx>, Exception>> {
        private final Logger _logger;
        private final Pipeline _mediator;

        public Handler(Logger logger, Pipeline mediator) {
            _logger = logger;
            _mediator = mediator;
        }

        @Override
        public Result<ObservableList<UserFx>, Exception> handle(Query query) {
            _logger.info("GetAllUsers Use Case invoked");
            var dto = query.dto;

            if (dto == null) {
                return Result.err(new ApplicationException("Invalid request!"));
            }

            try {
                dto.initAllUsers();
            } catch (Exception ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            return RemoveLoggedInUserFromCollection.execute(_mediator, _logger, dto.usersProperty().get());
        }
    }
}
