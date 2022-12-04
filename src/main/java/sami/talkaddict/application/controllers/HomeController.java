package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import sami.talkaddict.application.cqrs.commands.auth.LogoutUser;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private BorderPane _homePane;
    @FXML
    private FontAwesomeIconView _homeMenuItem;
    @FXML
    private FontAwesomeIconView _profileMenuItem;
    @FXML
    private FontAwesomeIconView _logoutMenuItem;

    private Logger _logger;
    private Pipeline _mediator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _logger = ProviderService.provideLogger(HomeController.class);
        _mediator = ProviderService.provideMediator();
        try {
            _homePane.setCenter(SceneFxManager.loadPane(Config.Views.CHAT_VIEW));
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
    private void openChatView(MouseEvent mouseEvent) {
        try {
            _logger.info("Opening chat view");
            _homePane.setCenter(SceneFxManager.loadPane(Config.Views.CHAT_VIEW));
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
    private void openProfileView(MouseEvent mouseEvent) {
        try {
            _logger.info("Opening profile view");
            _homePane.setCenter(SceneFxManager.loadPane(Config.Views.PROFILE_VIEW));
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
    private void logoutAndOpenMainView(MouseEvent mouseEvent) {
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
