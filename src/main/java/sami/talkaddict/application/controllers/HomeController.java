package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import sami.talkaddict.application.cqrs.commands.auth.LogoutUser;
import sami.talkaddict.application.cqrs.queries.auth.GetLoggedInUser;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button _logoutUserButton;

    private Logger _logger;
    private Pipeline _mediator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _logger = ProviderService.provideLogger(HomeController.class);
        _mediator = ProviderService.provideMediator();
        try {
            var response = _mediator.send(new GetLoggedInUser.Query());
            if (response.isOk()) {
                var loggedInUser = response.ok().orElseThrow();
                _logger.info("Current logged in user name: " + loggedInUser.getUsername());
            } else {
                _logger.error("Failed to get logged in user");
                throw response.err().orElseThrow();
            }
        } catch (Exception ex) {
            SceneFxManager.showAlertDialog(
                    "Error getting logged in user",
                    "Something went wrong!",
                    Alert.AlertType.ERROR
            );
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        }
    }

    @FXML
    private void openMainViewAndLogout(ActionEvent actionEvent) {
        try {
            _mediator.send(new LogoutUser.Command());
            _logger.info("User logged out");
            SceneFxManager.redirectTo(Config.Views.MAIN_VIEW, _logoutUserButton);
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
