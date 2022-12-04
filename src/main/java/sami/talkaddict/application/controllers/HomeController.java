package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import sami.talkaddict.application.requests.commands.auth.LogoutUser;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private BorderPane _homePane;
    @FXML
    private VBox _homeMenuItem;
    @FXML
    private VBox _profileMenuItem;
    @FXML
    private VBox _logoutMenuItem;

    private Logger _logger;
    private Pipeline _mediator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _logger = ProviderService.provideLogger(HomeController.class);
        _mediator = ProviderService.provideMediator();
        try {
            _homePane.setCenter(SceneFxManager.loadPane(Config.Views.CHAT_PANE));
        } catch (IOException ex) {
            SceneFxManager.showAlertDialog(
                    "Error",
                    "Error loading chat view",
                    Alert.AlertType.ERROR
            );
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        }
    }

    @FXML
    private void inflateChatPane(MouseEvent mouseEvent) {
        try {
            _logger.info("Opening chat view");
            _homePane.setCenter(SceneFxManager.loadPane(Config.Views.CHAT_PANE));
        } catch (IOException e) {
            SceneFxManager.showAlertDialog(
                    "Error opening chat view",
                    "Something went wrong!",
                    Alert.AlertType.ERROR
            );
            _logger.error(e, e.getMessage(), e.getStackTrace());
        }
    }

    @FXML
    private void inflateProfilePane(MouseEvent mouseEvent) {
        try {
            _logger.info("Opening profile view");
            _homePane.setCenter(SceneFxManager.loadPane(Config.Views.PROFILE_PANE));
        } catch (IOException e) {
            SceneFxManager.showAlertDialog(
                    "Error opening profile view",
                    "Something went wrong!",
                    Alert.AlertType.ERROR
            );
            _logger.error(e, e.getMessage(), e.getStackTrace());
        }
    }

    @FXML
    private void onLogoutUser(MouseEvent mouseEvent) {
        try {
            _mediator.send(new LogoutUser.Command());
            _logger.info("User logged out");
            SceneFxManager.redirectTo(Config.Views.MAIN_VIEW, _logoutMenuItem);
        } catch (Exception ex) {
            SceneFxManager.showAlertDialog(
                    "Error logging out",
                    "Something went wrong!",
                    Alert.AlertType.ERROR
            );
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        }
    }
}
