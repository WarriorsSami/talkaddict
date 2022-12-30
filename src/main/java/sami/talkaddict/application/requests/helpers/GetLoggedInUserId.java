package sami.talkaddict.application.requests.helpers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import dev.kylesilver.result.UnwrapException;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;

public class GetLoggedInUserId {
    public static Result<Integer, Exception> execute(Pipeline mediator, Logger logger) {
        Integer loggedInUserId = 0;
        var response = mediator.send(new GetLoggedInUser.Query());
        if (response.isOk()) {
            try {
                loggedInUserId = response.unwrap().getId();
            } catch (UnwrapException e) {
                logger.error(e.toString());
                return Result.err(e);
            }
        }
        return Result.ok(loggedInUserId);
    }
}
