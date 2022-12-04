package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import sami.talkaddict.application.cqrs.queries.auth.GetLoggedInUser;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileEditorController implements Initializable {
    @FXML
    private Label _welcomeLabel;

    private Logger _logger;
    private Pipeline _mediator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _logger = ProviderService.provideLogger(ProfileEditorController.class);
        _mediator = ProviderService.provideMediator();
        try {
            var response = _mediator.send(new GetLoggedInUser.Query());
            if (response.isOk()) {
                var loggedInUser = response.ok().orElseThrow();
                _logger.info("Current logged in user name: " + loggedInUser.getUsername());
                _welcomeLabel.setText("Edit your profile, " + loggedInUser.getUsername() + "!");
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
}
