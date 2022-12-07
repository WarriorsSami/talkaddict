package sami.talkaddict.application.controllers;

import com.j256.ormlite.logger.Logger;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    private VBox _authPane;
    private Parent _fxml;

    private Logger _logger;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        _logger = ProviderService.provideLogger(SplashController.class);
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), _authPane);
        t.setToX(Config.FxmlSettings.MAIN_SLIDER_POSITION);
        t.play();
        t.setOnFinished((e) -> {
            try {
                _authPane.getChildren().removeAll();
                _authPane.getChildren().setAll(SceneFxManager.loadPane(Config.Views.LOGIN_PANE));
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Initialization error",
                        "Failed to initialize main view",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex, ex.getMessage(), ex.getStackTrace());
            }
        });
        _logger.info("Splash View initialized");
    }

    @FXML
    private void inflateLoginPane(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), _authPane);
        t.setToX(Config.FxmlSettings.MAIN_SLIDER_POSITION);
        t.play();
        t.setOnFinished((e) -> {
            try {
                _authPane.getChildren().removeAll();
                _authPane.getChildren().setAll(SceneFxManager.loadPane(Config.Views.LOGIN_PANE));
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Initialization error",
                        "Failed to initialize login view",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex, ex.getMessage(), ex.getStackTrace());
            }
        });
    }

    @FXML
    private void inflateRegisterPane(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), _authPane);
        t.setToX(0);
        t.play();
        t.setOnFinished((e) -> {
            try {
                _authPane.getChildren().removeAll();
                _authPane.getChildren().setAll(SceneFxManager.loadPane(Config.Views.REGISTER_PANE));
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Initialization error",
                        "Failed to initialize register view",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex, ex.getMessage(), ex.getStackTrace());
            }
        });
    }
}
