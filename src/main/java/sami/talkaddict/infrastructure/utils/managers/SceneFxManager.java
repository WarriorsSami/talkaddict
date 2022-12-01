package sami.talkaddict.infrastructure.utils.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sami.talkaddict.TalkaddictApplication;

import java.io.IOException;

public class SceneFxManager {
    public static void redirectTo(String path, Node anchor) throws IOException {
        var loader = new FXMLLoader(TalkaddictApplication.class.getResource(path));
        var stage = (Stage) anchor.getScene().getWindow();
        var scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void showAlertDialog(String title, String message, Alert.AlertType type) {
        var alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setResizable(true);
        alert.show();
    }
}
