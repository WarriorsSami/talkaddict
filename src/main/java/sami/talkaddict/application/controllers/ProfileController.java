package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dev.kylesilver.result.Result;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import net.synedra.validatorfx.Validator;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.application.requests.commands.profile.UpdateUserProfile;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.user.User;
import sami.talkaddict.domain.entities.user.UserStatus;
import sami.talkaddict.infrastructure.utils.managers.ImageManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Circle _statusClip;
    @FXML
    private MFXComboBox<UserStatus> _statusComboBox;
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

        _saveProfileProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        _saveProfileProgressBar.setVisible(false);

        MFXTooltip.of(_statusComboBox, "Update your status activity here").install();

        var leadingIcon = new FontAwesomeIconView();
        leadingIcon.setGlyphName("SIGNAL");
        _statusComboBox.setLeadingIcon(leadingIcon);

        _statusComboBox.setItems(UserStatus.getAllStatuses());
        _statusComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            _logger.info("Status changed to: " + newValue);
            if (oldValue != newValue) {
                ImageManager.assignStatusToClip(_statusClip, newValue);
            }
        });

        var getLoggedInUserTask = new Task<Result<User, Exception>>() {
            @Override
            protected Result<User, Exception> call() throws Exception {
                return _mediator.send(new GetLoggedInUser.Query());
            }
        };

        getLoggedInUserTask.setOnSucceeded(event -> {
            try {
                var response = getLoggedInUserTask.getValue();
                if (response.isOk()) {
                    _userViewModel.initFromUser(response.ok().orElseThrow());
                    _logger.info("Current logged in user name: " + _userViewModel.usernameProperty().get());

                    ImageManager.assignAvatarToImageView(
                            _avatarImageView,
                            _userViewModel.avatarProperty().get(),
                            _avatarImageView.getFitWidth(),
                            _avatarImageView.getFitHeight()
                    );
                    ImageManager.assignStatusToClip(
                            _statusClip,
                            _userViewModel.statusProperty().get()
                    );
                    _statusComboBox.setValue(_userViewModel.statusProperty().get());
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
                bindValidatorToFields();
            }
        });

        var getLoggedInUserThread = new Thread(getLoggedInUserTask);
        getLoggedInUserThread.setDaemon(true);
        getLoggedInUserThread.start();
    }

    private void bindBidirectionalViewModelToFields() {
        _userViewModel.usernameProperty().bindBidirectional(_usernameField.textProperty());
        _userViewModel.descriptionProperty().bindBidirectional(_descriptionField.textProperty());
        _userViewModel.statusProperty().bindBidirectional(_statusComboBox.valueProperty());
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
        if (_validator.validate()) {
           var updateProfileTask = new Task<Result<Voidy, Exception>>() {
                @Override
                protected Result<Voidy, Exception> call() throws Exception {
                     return _mediator.send(new UpdateUserProfile.Command(_userViewModel));
                }
           };

           updateProfileTask.setOnRunning(workerStateEvent -> {
               _saveProfileProgressBar.setVisible(true);
           });

           updateProfileTask.setOnSucceeded(workerStateEvent -> {
               _saveProfileProgressBar.setVisible(false);
               try {
                   var response = updateProfileTask.getValue();

                   if (response.isErr()) {
                       _logger.error("User profile update failed");
                       throw response.err().orElseThrow();
                   }

                   SceneFxManager.showToastNotification(
                           "Success",
                           "Profile updated successfully"
                   );
               } catch (Exception ex) {
                   SceneFxManager.showAlertDialog(
                           "Update Profile Error",
                           "Something went wrong!",
                           Alert.AlertType.ERROR
                   );
                   _logger.error(ex, ex.getMessage(), ex.getStackTrace());
               }
           });

           var updateProfileThread = new Thread(updateProfileTask);
           updateProfileThread.setDaemon(true);
           updateProfileThread.start();
        } else {
            _logger.warn("Validation failed");
            var errors = SceneFxManager.removeDuplicateErrors(_validator.getValidationResult());
            SceneFxManager.showAlertDialog(
                    "Invalid data",
                    Bindings.concat("Cannot update profile:\n", errors).getValue(),
                    Alert.AlertType.WARNING
            );
        }
    }

    @FXML
    private void onUploadAvatar() {
        var file = ImageManager.loadFileUsingFileChooser(_uploadAvatarButton);
        if (file != null) {
            _userViewModel.avatarProperty().set(ImageManager.convertFileToByteArray(file));
            var fileAsByteArray = ImageManager.convertFileToByteArray(file);
            _avatarImageView.setImage(ImageManager.convertByteArrayToImage(fileAsByteArray));
        }
    }

    @FXML
    private void onResetAvatar() {
        var randomAvatar = ImageManager.getRandomAvatar();
        _userViewModel.avatarProperty().set(randomAvatar);
        _avatarImageView.setImage(ImageManager.convertByteArrayToImage(randomAvatar));
    }
}
