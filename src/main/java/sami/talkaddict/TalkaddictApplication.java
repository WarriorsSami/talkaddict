package sami.talkaddict;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sami.talkaddict.infrastructure.DbManager;

import java.io.IOException;

public class TalkaddictApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DbManager.initDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(TalkaddictApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Talkaddict - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
