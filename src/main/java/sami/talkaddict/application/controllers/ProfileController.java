package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import net.synedra.validatorfx.Validator;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.application.requests.commands.profile.UpdateUserProfile;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.managers.AvatarManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private ImageView _avatarImageView;
    @FXML
    private Button _uploadAvatarButton;
    @FXML
    private Button _resetAvatarButton;
    @FXML
    private TextField _usernameField;
    @FXML
    private TextArea _descriptionField;
    @FXML
    private ProgressBar _saveProfileProgressBar;
    @FXML
    private Button _saveChangesButton;

    private Logger _logger;
    private Pipeline _mediator;
    private Validator _validator;
    private UserViewModel _userViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _logger = ProviderService.provideLogger(ProfileController.class);
        _mediator = ProviderService.provideMediator();
        _userViewModel = new UserViewModel();
        _validator = new Validator();

        bindBidirectionalViewModelToFields();
        try {
            _saveProfileProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            _saveProfileProgressBar.setVisible(false);

            var response = _mediator.send(new GetLoggedInUser.Query());
            if (response.isOk()) {
                _userViewModel.initFromUser(response.ok().orElseThrow());
                _logger.info("Current logged in user name: " + _userViewModel.usernameProperty().get());

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
        } finally {
            _logger.info("Profile view initialized");
        }
        bindValidatorToFields();
    }

    private void bindBidirectionalViewModelToFields() {
        _userViewModel.usernameProperty().bindBidirectional(_usernameField.textProperty());
        _userViewModel.descriptionProperty().bindBidirectional(_descriptionField.textProperty());
    }

    private void bindValidatorToFields() {
        _validator.createCheck()
                .dependsOn(Config.ValidationTweaks.USERNAME_FIELD_PROFILE_KEY, _usernameField.textProperty())
                .withMethod(_userViewModel::isUsernameValid)
                .decorates(_usernameField)
                .immediate();

        _validator.createCheck()
                .dependsOn(Config.ValidationTweaks.USERNAME_FIELD_PROFILE_KEY, _usernameField.textProperty())
                .withMethod(_userViewModel::isUsernameUnique)
                .decorates(_usernameField);

        _validator.createCheck()
                .dependsOn(Config.ValidationTweaks.DESCRIPTION_FIELD_PROFILE_KEY, _descriptionField.textProperty())
                .withMethod(_userViewModel::isDescriptionValid)
                .decorates(_descriptionField)
                .immediate();
    }

    @FXML
    private void onSaveProfileChanges(ActionEvent event) {
        try {
            if (_validator.validate()) {
                var response = _mediator.send(new UpdateUserProfile.Command(_userViewModel));

                if (response.isErr()) {
                    _logger.error("User profile update failed");
                    throw response.err().orElseThrow();
                }

                _logger.info("User profile updated successfully");
                var responseLoggedInUser = _mediator.send(new GetLoggedInUser.Query());

                if (responseLoggedInUser.isErr()) {
                    _logger.error("Failed to get logged in user");
                    throw responseLoggedInUser.err().orElseThrow();
                }

                _userViewModel.initFromUser(responseLoggedInUser.ok().orElseThrow());
                SceneFxManager.redirectTo(Config.Views.HOME_VIEW, _saveChangesButton);
            } else {
                _logger.warn("Validation failed");
                var errors = SceneFxManager.removeDuplicateErrors(_validator.getValidationResult());
                SceneFxManager.showAlertDialog(
                        "Invalid data",
                        Bindings.concat("Cannot update profile:\n", errors).getValue(),
                        Alert.AlertType.WARNING
                );
            }
        } catch (Exception ex) {
            SceneFxManager.showAlertDialog(
                    "Update Profile Error",
                    "Something went wrong!",
                    Alert.AlertType.ERROR
            );
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        }
    }

    @FXML
    private void onUploadAvatar() {
        var file = SceneFxManager.loadFileUsingFileChooser(_uploadAvatarButton);
        if (file != null) {
            _userViewModel.avatarProperty().set(AvatarManager.convertFileToByteArray(file));
            var fileAsByteArray = AvatarManager.convertFileToByteArray(file);
            _avatarImageView.setImage(AvatarManager.convertByteArrayToImage(fileAsByteArray));
        }
    }

    @FXML
    private void onResetAvatar() {
        var randomAvatar = AvatarManager.getRandomAvatar();
        _userViewModel.avatarProperty().set(randomAvatar);
        _avatarImageView.setImage(AvatarManager.convertByteArrayToImage(randomAvatar));
    }
}
