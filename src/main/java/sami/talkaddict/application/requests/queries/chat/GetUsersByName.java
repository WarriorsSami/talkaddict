package sami.talkaddict.application.requests.queries.chat;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import javafx.collections.ObservableList;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.models.user.UserListViewModel;
import sami.talkaddict.application.requests.helpers.RemoveLoggedInUserFromCollection;
import sami.talkaddict.domain.exceptions.ApplicationException;

public class GetUsersByName {
    public record Query(UserListViewModel dto, String name) implements an.awesome.pipelinr.Command<Result<ObservableList<UserFx>, Exception>> {
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
            _logger.info("GetUsersByName Use Case invoked");
            var dto = query.dto;
            var name = query.name;

            if (dto == null || name == null) {
                return Result.err(new ApplicationException("Invalid request!"));
            }

            try {
                dto.initUsersByName(name);
            } catch (Exception ex) {
                return Result.err(ex);
            }

            return RemoveLoggedInUserFromCollection.execute(_mediator, _logger, dto.usersProperty().get());
        }
    }
}
