package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dev.kylesilver.result.Result;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.reactfx.EventStreams;
import sami.talkaddict.application.factories.DirectMessageCellFactory;
import sami.talkaddict.application.factories.UserCellFactory;
import sami.talkaddict.application.models.chat.DirectMessageFx;
import sami.talkaddict.application.models.chat.DirectMessageListViewModel;
import sami.talkaddict.application.models.chat.DirectMessageViewModel;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.models.user.UserListViewModel;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.application.requests.commands.chat.CreateOrUpdateDirectMessageAndReloadMessagesList;
import sami.talkaddict.application.requests.commands.chat.DeleteDirectMessageByIdAndReloadMessagesList;
import sami.talkaddict.application.requests.queries.auth.GetLoggedInUser;
import sami.talkaddict.application.requests.queries.chat.GetAllUsers;
import sami.talkaddict.application.requests.queries.chat.GetDirectMessagesByLoggedInUserAndOtherUser;
import sami.talkaddict.application.requests.queries.chat.GetUsersByName;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.user.User;
import sami.talkaddict.infrastructure.utils.converters.UserConverter;
import sami.talkaddict.infrastructure.utils.managers.ImageManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private ListView<DirectMessageFx> _directMessagesListView;
    @FXML
    private Circle _discardImageIconClip;
    @FXML
    private Label _usernameLabel;
    @FXML
    private Circle _statusClip;
    @FXML
    private ImageView _avatarImageView;
    @FXML
    private StackPane _sendMessageIcon;
    @FXML
    private TextArea _messageText;
    @FXML
    private StackPane _discardImageIcon;
    @FXML
    private HBox _messageBox;
    @FXML
    private StackPane _uploadImageIcon;
    @FXML
    private HBox _searchBox;
    @FXML
    private MFXTextField _searchField;
    @FXML
    private MFXProgressSpinner _searchSpinner;
    @FXML
    private FontAwesomeIconView _searchIcon;
    @FXML
    private Pane _loadingOverlay;
    @FXML
    private ListView<UserFx> _usersListView;

    private Logger _logger;
    private Pipeline _mediator;
    private UserListViewModel _userListViewModel;
    private DirectMessageListViewModel _directMessageListViewModel;
    private UserViewModel _loggedInUserViewModel;
    private UserViewModel _selectedUserViewModel;
    private DirectMessageViewModel _dmToBeWrittenViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _logger = ProviderService.provideLogger(ChatController.class);
        _mediator = ProviderService.provideMediator();

        _userListViewModel = new UserListViewModel();
        _loggedInUserViewModel = new UserViewModel();
        _selectedUserViewModel = new UserViewModel();

        _directMessageListViewModel = new DirectMessageListViewModel();
        _dmToBeWrittenViewModel = new DirectMessageViewModel();

        bindFieldsToViewModel();
        bindViewModelToFields();

        // users list view
        _usersListView.setCellFactory(userFx -> new UserCellFactory());
        _usersListView.setFixedCellSize(Config.FxmlSettings.USERS_LIST_CELL_SIZE);

        _usersListView.setOnMouseClicked(event -> {
            if (event.getEventType().getName().equals("MOUSE_CLICKED")) {
                var selectedUser = _usersListView.getSelectionModel().getSelectedItems().get(0);
                if (selectedUser != null) {
                    _logger.info("Selected user: " + selectedUser.Username.get());
                    _selectedUserViewModel.initFromUser(UserConverter.convertUserFxToUser(selectedUser));
                    initChatHeader(selectedUser);

                    // get direct messages
                    getDirectMessagesForChat();
                } else {
                    _logger.info("Selected user is null");
                }
            }
        });

        // direct messages list view
        _directMessagesListView.setCellFactory(dmFx -> new DirectMessageCellFactory());
        _directMessagesListView.setFixedCellSize(Config.FxmlSettings.CHAT_LIST_CELL_SIZE);

        _directMessagesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                var selectedDm = _directMessagesListView.getSelectionModel().getSelectedItems().get(0);
                if (selectedDm != null) {
                    _logger.info("Selected dm: " + selectedDm.Id.get());
                    deleteDirectMessage(selectedDm);
                } else {
                    _logger.info("Selected dm is null");
                }
            }
        });
        MFXTooltip.of(_directMessagesListView, "Double click to delete message").install();

        // Search field
        MFXTooltip.of(_searchBox, "Filter users by name").install();
        // add a debouncing of x milliseconds to the search field
        EventStreams.valuesOf(_searchField.textProperty())
                .successionEnds(Duration.ofMillis(Config.FxmlSettings.SEARCH_DELAY))
                .subscribe(this::filterUsersByName);

        _searchIcon.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                filterUsersByName(_searchField.getText());
            }
        });

        // other fields
        MFXTooltip.of(_uploadImageIcon, "Upload image").install();
        MFXTooltip.of(_discardImageIcon, "Discard image").install();

        // TODO 1. add loading overlays to chat panel, welcome overlay to chat header when no group is selected and no messages overlay when chat is empty
        // TODO 2. implement the read message functionality

        getLoggedInUser();
        getAllUsers();
    }

    private void bindFieldsToViewModel() {
        _usersListView.itemsProperty().bind(_userListViewModel.usersProperty());
        _directMessagesListView.itemsProperty().bind(_directMessageListViewModel.directMessagesProperty());
    }

    private void bindViewModelToFields() {
        _dmToBeWrittenViewModel.messageTextProperty().bind(_messageText.textProperty());
    }

    private void initChatHeader(UserFx userFx) {
        _usernameLabel.setText(userFx.Username.get());
        ImageManager.assignAvatarToImageView(
                _avatarImageView,
                userFx.Avatar.get(),
                _avatarImageView.getFitWidth(),
                _avatarImageView.getFitHeight()
        );
        ImageManager.assignStatusToClip(
                _statusClip,
                userFx.Status.get()
        );
    }

    private void getLoggedInUser() {
        var getLoggedInUserTask = new Task<Result<User, Exception>>() {
            @Override
            protected Result<User, Exception> call() {
                return _mediator.send(new GetLoggedInUser.Query());
            }
        };

        getLoggedInUserTask.setOnSucceeded(event -> {
            try {
                var result = getLoggedInUserTask.getValue();
                if (result.isOk()) {
                    var user = result.ok().orElseThrow();
                    _loggedInUserViewModel.initFromUser(user);
                    _logger.info("Logged in user: " + _loggedInUserViewModel.usernameProperty().get());
                } else {
                    _logger.error("Failed to get logged in user");
                    throw result.err().orElseThrow();
                }
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Error fetching logged in user",
                        "Something went wrong!",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex, ex.getMessage(), ex.getStackTrace());
            }
        });

        var getLoggedInUserThread = new Thread(getLoggedInUserTask);
        getLoggedInUserThread.setDaemon(true);
        getLoggedInUserThread.start();
    }

    private void getAllUsers() {
        var getAllUsersTask = new Task<Result<ObservableList<UserFx>, Exception>>() {
            @Override
            protected Result<ObservableList<UserFx>, Exception> call() {
                return _mediator.send(new GetAllUsers.Query(_userListViewModel));
            }
        };

        getAllUsersTask.setOnRunning(event -> {
            _loadingOverlay.setVisible(true);
        });

        getAllUsersTask.setOnSucceeded(event -> {
            _loadingOverlay.setVisible(false);
            try {
                var response = getAllUsersTask.getValue();
                if (response.isOk()) {
                    var users = response.ok().orElseThrow();
                    _logger.info("Users fetched successfully");
                } else {
                    _logger.error("Failed to fetch users");
                    throw response.err().orElseThrow();
                }
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Error fetching users",
                        "Something went wrong!",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex, ex.getMessage(), ex.getStackTrace());
            } finally {
                _logger.info("Chat View initialized");
            }
        });

        var getAllUsersThread = new Thread(getAllUsersTask);
        getAllUsersThread.setDaemon(true);
        getAllUsersThread.start();
    }

    private void filterUsersByName(String value) {
        var filterUsersTask = new Task<Result<ObservableList<UserFx>, Exception>>() {
            @Override
            protected Result<ObservableList<UserFx>, Exception> call() {
                return _mediator.send(new GetUsersByName.Query(_userListViewModel, value));
            }
        };

        filterUsersTask.setOnRunning(event -> {
            _searchSpinner.setVisible(true);
        });

        filterUsersTask.setOnSucceeded(event -> {
            _searchSpinner.setVisible(false);
            try {
                var response = filterUsersTask.getValue();
                if (response.isOk()) {
                    var users = response.ok().orElseThrow();
                    _logger.info("Users filtered successfully");
                } else {
                    _logger.error("Failed to filter users");
                    throw response.err().orElseThrow();
                }
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Error filtering users",
                        "Something went wrong!",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex.toString(), ex.getStackTrace());
            }
        });

        var filterUsersThread = new Thread(filterUsersTask);
        filterUsersThread.setDaemon(true);
        filterUsersThread.start();
    }

    private void deleteDirectMessage(DirectMessageFx messageFx) {
        var deleteDirectMessageTask = new Task<Result<Voidy, Exception>>() {
            @Override
            protected Result<Voidy, Exception> call() {
                return _mediator.send(new DeleteDirectMessageByIdAndReloadMessagesList.Command(
                        _directMessageListViewModel,
                        messageFx.Id.get(),
                        _loggedInUserViewModel.idProperty().get(),
                        _selectedUserViewModel.idProperty().get()
                ));
            }
        };

        deleteDirectMessageTask.setOnSucceeded(event -> {
            _loadingOverlay.setVisible(false);
            try {
                var response = deleteDirectMessageTask.getValue();
                if (response.isOk()) {
                    var message = response.ok().orElseThrow();
                    _logger.info("Direct message deleted successfully");
                } else {
                    _logger.error("Failed to delete direct message");
                    throw response.err().orElseThrow();
                }
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Error deleting direct message",
                        "Something went wrong!",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex.toString(), ex.getStackTrace());
            }
        });

        var deleteDirectMessageThread = new Thread(deleteDirectMessageTask);
        deleteDirectMessageThread.setDaemon(true);
        deleteDirectMessageThread.start();
    }

    private void getDirectMessagesForChat() {
        var getDirectMessagesTask = new Task<Result<ObservableList<DirectMessageFx>, Exception>>() {
            @Override
            protected Result<ObservableList<DirectMessageFx>, Exception> call() {
                return _mediator.send(new GetDirectMessagesByLoggedInUserAndOtherUser.Query(
                    _directMessageListViewModel,
                    _loggedInUserViewModel.idProperty().get(),
                    _selectedUserViewModel.idProperty().get()
                ));
            }
        };

        getDirectMessagesTask.setOnSucceeded(event -> {
            try {
                var response = getDirectMessagesTask.getValue();
                if (response.isOk()) {
                    var directMessages = response.ok().orElseThrow();
                    _directMessagesListView.scrollTo(directMessages.size() - 1);
                    _logger.info("Direct messages fetched successfully");
                } else {
                    _logger.error("Failed to fetch direct messages");
                    throw response.err().orElseThrow();
                }
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Error fetching direct messages",
                        "Something went wrong!",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex, ex.getMessage(), ex.getStackTrace());
            }
        });

        var getDirectMessagesThread = new Thread(getDirectMessagesTask);
        getDirectMessagesThread.setDaemon(true);
        getDirectMessagesThread.start();
    }

    @FXML
    private void onUploadMessageImage(MouseEvent mouseEvent) {
        _discardImageIcon.setDisable(false);
        _discardImageIconClip.setFill(Color.RED);

        var imageFile = ImageManager.loadFileUsingFileChooser(_uploadImageIcon);
        if (imageFile != null) {
            var imageBytes = ImageManager.convertFileToByteArray(imageFile);
            var messageImageView = ImageManager.getImageView(
                    imageBytes,
                    Config.FxmlSettings.MESSAGE_IMAGE_PREVIEW_WIDTH,
                    Config.FxmlSettings.MESSAGE_IMAGE_PREVIEW_HEIGHT
            );
            _messageBox.getChildren().add(2, messageImageView);
            _dmToBeWrittenViewModel.messageImageProperty().set(imageBytes);
        }
    }

    @FXML
    private void onDiscardMessageImage(MouseEvent mouseEvent) {
        _discardImageIcon.setDisable(true);
        _discardImageIconClip.setFill(Color.INDIANRED);

        _messageBox.getChildren().remove(2);
        _dmToBeWrittenViewModel.messageImageProperty().set(null);
    }

    @FXML
    private void onSendMessage(MouseEvent mouseEvent) {
        if (_dmToBeWrittenViewModel.messageTextProperty().get() == null && _dmToBeWrittenViewModel.messageImageProperty().get() == null) {
            return;
        }

        var sendMessageTask = new Task<Result<Voidy, Exception>>() {
            @Override
            protected Result<Voidy, Exception> call() {
                return _mediator.send(
                        new CreateOrUpdateDirectMessageAndReloadMessagesList.Command(
                                _dmToBeWrittenViewModel,
                                _directMessageListViewModel,
                                _loggedInUserViewModel.idProperty().get(),
                                _selectedUserViewModel.idProperty().get()
                        )
                );
            }
        };

        sendMessageTask.setOnSucceeded(event -> {
            try {
                var response = sendMessageTask.getValue();
                if (response.isOk()) {
                    var voidy = response.ok().orElseThrow();
                    _logger.info("Message sent successfully");
                } else {
                    _logger.error("Failed to send message");
                    throw response.err().orElseThrow();
                }
            } catch (Exception ex) {
                SceneFxManager.showAlertDialog(
                        "Error sending message",
                        "Something went wrong!",
                        Alert.AlertType.ERROR
                );
                _logger.error(ex.toString(), ex.getStackTrace());
            }
        });

        var sendMessageThread = new Thread(sendMessageTask);
        sendMessageThread.setDaemon(true);
        sendMessageThread.start();
    }
}
