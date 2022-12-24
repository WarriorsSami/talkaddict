package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.application.requests.commands.auth.LogoutUser;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.infrastructure.utils.managers.AvatarManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private BorderPane _homePane;
    @FXML
    private ImageView _avatarImageView;
    @FXML
    private Label _usernameLabel;
    @FXML
    private VBox _chatMenuItem;
    @FXML
    private VBox _notificationMenuItem;
    @FXML
    private VBox _profileMenuItem;
    @FXML
    private VBox _logoutMenuItem;

    private Logger _logger;
    private Pipeline _mediator;
    private UserViewModel _userViewModel;

    private Timeline _updateLoggedInUserViewModelTimeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _logger = ProviderService.provideLogger(HomeController.class);
        _mediator = ProviderService.provideMediator();
        _userViewModel = new UserViewModel();

        bindFieldsToViewModel();
        updateLoggedInUserViewModel();

        var updateLoggedInUserViewModelThread = new Thread(this::updateLoggedInUserViewModelPeriodically);
        updateLoggedInUserViewModelThread.setDaemon(true);
        updateLoggedInUserViewModelThread.start();

        try {
            _homePane.setCenter(SceneFxManager.loadPane(Config.Views.CHAT_PANE));
        } catch (IOException ex) {
            SceneFxManager.showAlertDialog(
                    "Error",
                    "Error loading chat view",
                    Alert.AlertType.ERROR
            );
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        } finally {
            _logger.info("Home View Initialized");
        }
    }

    private void bindFieldsToViewModel() {
        _usernameLabel.textProperty().bind(_userViewModel.usernameProperty());
    }

    private void updateLoggedInUserViewModelPeriodically() {
         _updateLoggedInUserViewModelTimeline = new Timeline(
                 new KeyFrame(
                         Duration.seconds(Config.AppSettings.REFRESH_RATE),
                         event -> updateLoggedInUserViewModel()
                 )
         );
        _updateLoggedInUserViewModelTimeline.setCycleCount(Timeline.INDEFINITE);
        _updateLoggedInUserViewModelTimeline.play();
    }

    private void updateLoggedInUserViewModel() {
        var getLoggedInUserTask = new Task<Result<User, Exception>>() {
            @Override
            protected Result<User, Exception> call() {
                return _mediator.send(new GetLoggedInUser.Query());
            }
        };

        getLoggedInUserTask.setOnSucceeded(event -> {
            try {
                var response = getLoggedInUserTask.getValue();
                if (response.isOk()) {
                    _userViewModel.initFromUser(response.ok().orElseThrow());
                    AvatarManager.assignAvatarToImageView(
                            _avatarImageView,
                            _userViewModel.avatarProperty().get(),
                            _avatarImageView.getFitWidth(),
                            _avatarImageView.getFitHeight()
                    );
                } else {
                    _logger.error("Failed to get logged in user!");
                    throw response.err().orElseThrow();
                }
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Error",
                        "Error loading chat view",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex, ex.getMessage(), ex.getStackTrace());
            }
        });

        var getLoggedInUserThread = new Thread(getLoggedInUserTask);
        getLoggedInUserThread.setDaemon(true);
        getLoggedInUserThread.start();
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
    private void inflateNotificationPane(MouseEvent mouseEvent) {

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
            _updateLoggedInUserViewModelTimeline.stop();
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
