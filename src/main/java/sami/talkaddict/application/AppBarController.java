package sami.talkaddict.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sami.talkaddict.TalkaddictApplication;

import java.io.IOException;
import java.util.Objects;

public class AppBarController {
    @FXML
    Button loginButton, registerButton;

    public void onLoginClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(TalkaddictApplication.class.getResource("login-view.fxml")));
        Stage window = (Stage) loginButton.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void onRegisterClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(TalkaddictApplication.class.getResource("register-view.fxml")));
        Stage window = (Stage) registerButton.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
