package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import dev.kylesilver.result.Result;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import sami.talkaddict.application.factories.UserCellFactory;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.application.models.user.UserListViewModel;
import sami.talkaddict.application.requests.queries.chat.GetAllUsers;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
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

        var getAllUsersTask = new Task<Result<ObservableList<UserFx>, Exception>>() {
            @Override
            protected Result<ObservableList<UserFx>, Exception> call() {
                return _mediator.send(new GetAllUsers.Query(_userListViewModel));
            }
        };

        getAllUsersTask.setOnSucceeded(event -> {
            try {
                var response = getAllUsersTask.getValue();
                if (response.isOk()) {
                    var users = response.ok().orElseThrow();

//                  users.stream().map(UserCellFactory::getNode).forEach(item -> _usersListView.getItems().add(item));

                    _usersListView.setItems(users);
                    _usersListView.setConverter(FunctionalStringConverter.to(userFx -> userFx.Username.get()));
                    _usersListView.setCellFactory(userFx -> new UserCellFactory(_usersListView, userFx));

                    _usersListView.features().enableBounceEffect();
                    _usersListView.features().enableSmoothScrolling(Config.FxmlSettings.USER_LIST_VIEW_SCROLLING_SPEED);

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
}
