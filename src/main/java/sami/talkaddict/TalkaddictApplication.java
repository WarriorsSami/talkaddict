package sami.talkaddict;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.AuthenticationManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class TalkaddictApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ApplicationException, SQLException {
        ProviderService.init();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Config.Views.MAIN_VIEW)));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle(Config.AppSettings.APP_TITLE);
        stage.show();
    }

    @Override
    public void stop() {
        AuthenticationManager.logout();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
