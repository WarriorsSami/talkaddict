package sami.talkaddict;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sami.talkaddict.application.cqrs.commands.auth.LogoutUser;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.infrastructure.utils.Config;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class TalkaddictApplication extends Application {
    private static Logger _logger;
    private static Pipeline _mediator;

    @Override
    public void start(Stage stage) throws IOException, ApplicationException, SQLException {
        ProviderService.init();
        _logger = ProviderService.provideLogger(TalkaddictApplication.class);
        _mediator = ProviderService.provideMediator();

        _logger.info("Initializing application...");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Config.Views.MAIN_VIEW)));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle(Config.AppSettings.APP_TITLE);
        stage.setResizable(Config.AppSettings.RESIZABLE);
        stage.show();
    }

    @Override
    public void stop() {
        _mediator.send(new LogoutUser.Command());
        _logger.info("Application closed.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
