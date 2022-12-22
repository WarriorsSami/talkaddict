package sami.talkaddict.application.requests.queries.chat;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import dev.kylesilver.result.UnwrapException;
import javafx.collections.ObservableList;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.models.user.UserListViewModel;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;
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

            var users = dto.getUserFxObservableList();
            var response = _mediator.send(new GetLoggedInUser.Query());
            if (response.isOk()) {
                Integer loggedInUserId;
                try {
                    loggedInUserId = response.unwrap().getId();
                } catch (UnwrapException e) {
                    _logger.error(e.toString());
                    return Result.err(e);
                }
                users.removeIf(user -> user.Id.get() == loggedInUserId);
            }

            return Result.ok(users);
        }
    }
}
