package sami.talkaddict.application.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import sami.talkaddict.TalkaddictApplication;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private VBox _vbox;
    private Parent _fxml;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), _vbox);
        t.setToX(_vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) -> {
            try {
                _fxml = FXMLLoader.load(
                        Objects.requireNonNull(TalkaddictApplication.class
                        .getResource(Config.Views.LOGIN_VIEW)
                        ));
                _vbox.getChildren().removeAll();
                _vbox.getChildren().setAll(_fxml);
            } catch (IOException ex) {
                SceneFxManager.showAlertDialog(
                        "Initialization error",
                        "Failed to initialize main view",
                        Alert.AlertType.ERROR
                );
            }
        });
    }

    @FXML
    private void openLoginView(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), _vbox);
        t.setToX(_vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) -> {
            try {
                _fxml = FXMLLoader.load(
                        Objects.requireNonNull(TalkaddictApplication.class
                        .getResource(Config.Views.LOGIN_VIEW)
                        ));
                _vbox.getChildren().removeAll();
                _vbox.getChildren().setAll(_fxml);
            } catch (IOException ex) {
                SceneFxManager.showAlertDialog(
                        "Initialization error",
                        "Failed to initialize login view",
                        Alert.AlertType.ERROR
                );
            }
        });
    }

    @FXML
    private void openRegisterView(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), _vbox);
        t.setToX(0);
        t.play();
        t.setOnFinished((e) -> {
            try {
                _fxml = FXMLLoader.load(
                        Objects.requireNonNull(TalkaddictApplication.class
                        .getResource(Config.Views.REGISTER_VIEW)
                        ));
                _vbox.getChildren().removeAll();
                _vbox.getChildren().setAll(_fxml);
            } catch (IOException ex) {
                SceneFxManager.showAlertDialog(
                        "Initialization error",
                        "Failed to initialize register view",
                        Alert.AlertType.ERROR
                );
            }
        });
    }
}
