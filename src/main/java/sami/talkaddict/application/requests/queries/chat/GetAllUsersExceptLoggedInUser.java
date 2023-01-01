package sami.talkaddict.application.requests.queries.chat;

import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import javafx.collections.ObservableList;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.models.user.UserListViewModel;
import sami.talkaddict.domain.exceptions.ApplicationException;

public class GetAllUsersExceptLoggedInUser {
    public record Query(
            UserListViewModel dto,
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
            _logger.info("GetAllUsers Use Case invoked");
            var dto = query.dto;
            var loggedInUserId = query.loggedInUserId;

            if (dto == null) {
                return Result.err(new ApplicationException("Invalid request!"));
            }

            try {
                dto.initAllUsersExceptLoggedInUser(loggedInUserId);
            } catch (Exception ex) {
                _logger.error(ex.toString());
                return Result.err(ex);
            }

            return Result.ok(dto.usersProperty().get());
        }
    }
}
