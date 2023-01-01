package sami.talkaddict.application.requests.queries.chat;

import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import javafx.collections.ObservableList;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.models.user.UserListViewModel;
import sami.talkaddict.domain.exceptions.ApplicationException;

public class GetUsersByNameExceptLoggedInUser {
    public record Query(
            UserListViewModel dto,
            String name,
            int loggedInUserId
    ) implements an.awesome.pipelinr.Command<Result<ObservableList<UserFx>, Exception>> {
    }

    public static class Handler implements an.awesome.pipelinr.Command.Handler<Query, Result<ObservableList<UserFx>, Exception>> {
        private final Logger _logger;

        public Handler(Logger logger) {
            _logger = logger;
        }

        @Override
        public Result<ObservableList<UserFx>, Exception> handle(Query query) {
            _logger.info("GetUsersByName Use Case invoked");
            var dto = query.dto;
            var name = query.name;
            var loggedInUserId = query.loggedInUserId;

            if (dto == null || name == null) {
                return Result.err(new ApplicationException("Invalid request!"));
            }

            try {
                dto.initUsersByNameExceptLoggedInUser(name, loggedInUserId);
            } catch (Exception ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            return Result.ok(dto.usersProperty().get());
        }
    }
}
