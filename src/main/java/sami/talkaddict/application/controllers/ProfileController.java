package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.managers.AvatarManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private ImageView _avatarImageView;
    @FXML
    private TextField _usernameField;
    @FXML
    private TextArea _descriptionField;

    private Logger _logger;
    private Pipeline _mediator;
    private UserViewModel _userViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _logger = ProviderService.provideLogger(ProfileController.class);
        _mediator = ProviderService.provideMediator();
        try {
            var response = _mediator.send(new GetLoggedInUser.Query());
            if (response.isOk()) {
                _userViewModel = new UserViewModel(response.ok().orElseThrow());
                _logger.info("Current logged in user name: " + _userViewModel.usernameProperty().get());

                _usernameField.setText(_userViewModel.usernameProperty().get());
                _descriptionField.setText(_userViewModel.descriptionProperty().get());
                _avatarImageView.setClip(AvatarManager.getAvatarClip(
                        _avatarImageView.getFitWidth(),
                        _avatarImageView.getFitHeight())
                );
                _avatarImageView.setImage(AvatarManager.convertByteArrayToImage(
                        _userViewModel.avatarProperty().get())
                );
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
