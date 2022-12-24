package sami.talkaddict.application.requests.helpers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import dev.kylesilver.result.UnwrapException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;

public class RemoveLoggedInUserFromCollection {
    public static Result<ObservableList<UserFx>, Exception> execute(Pipeline mediator, Logger logger, ObservableList<UserFx> users) {
        var response = mediator.send(new GetLoggedInUser.Query());
        if (response.isOk()) {
            Integer loggedInUserId;
            try {
                loggedInUserId = response.unwrap().getId();
            } catch (UnwrapException e) {
                logger.error(e.toString());
                return Result.err(e);
            }
            Platform.runLater(() -> {
                users.removeIf(user -> user.Id.get() == loggedInUserId);
            });
        }
        return Result.ok(users);
    }
}
