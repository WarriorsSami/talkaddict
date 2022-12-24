package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dev.kylesilver.result.Result;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.reactfx.EventStreams;
import sami.talkaddict.application.factories.UserCellFactory;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.models.user.UserListViewModel;
import sami.talkaddict.application.requests.queries.chat.GetAllUsers;
import sami.talkaddict.application.requests.queries.chat.GetUsersByName;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
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
    private Label _chatLabel;
    @FXML
    private MFXListView<UserFx> _usersListView;

    private Logger _logger;
    private Pipeline _mediator;
    private UserListViewModel _userListViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _logger = ProviderService.provideLogger(ChatController.class);
        _mediator = ProviderService.provideMediator();
        _userListViewModel = new UserListViewModel();
        bindFieldsToViewModel();

        _usersListView.setConverter(FunctionalStringConverter.to(userFx -> userFx.Username.get()));
        _usersListView.setCellFactory(userFx -> new UserCellFactory(_usersListView, userFx));

        _usersListView.setOnMouseClicked(event -> {
            if (event.getEventType().getName().equals("MOUSE_CLICKED")) {
                var selectedUser = _usersListView.getSelectionModel().getSelectedValues().get(0);
                if (selectedUser != null) {
                    _logger.info("Selected user: " + selectedUser.Username.get());
                    _chatLabel.setText("Chat with " + selectedUser.Username.get());
                } else {
                    _logger.info("Selected user is null");
                }
            }
        });

        _usersListView.features().enableBounceEffect();
        _usersListView.features().enableSmoothScrolling(Config.FxmlSettings.USER_LIST_VIEW_SCROLLING_SPEED);

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

    private void bindFieldsToViewModel() {
        _usersListView.itemsProperty().bind(_userListViewModel.usersProperty());
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
}
