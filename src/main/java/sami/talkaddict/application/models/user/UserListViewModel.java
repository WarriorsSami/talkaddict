package sami.talkaddict.application.models.user;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.user.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.daos.UserDao;
import sami.talkaddict.infrastructure.utils.converters.UserConverter;

import java.util.List;

public class UserListViewModel {
    private final ObjectProperty<ObservableList<UserFx>> _userFxList = new SimpleObjectProperty<>(
            FXCollections.observableArrayList()
    );

    private final GenericDao<User> _userDao;

    public UserListViewModel() {
        _userDao = ProviderService.provideDao(User.class);
    }

    public synchronized void initAllUsers() throws ApplicationException {
        var users = (List<User>) _userDao.findAll();
        Platform.runLater(() -> {
            _userFxList.get().clear();
            users.forEach(user -> {
                var userFx = UserConverter.convertUserToUserFx(user);
                _userFxList.get().add(userFx);
            });
        });
    }

    public synchronized void initAllUsersExceptLoggedInUser(int loggedInUserId) throws ApplicationException {
        var users = (List<User>) ((UserDao) _userDao).findAllExceptGivenUserIds(List.of(new Integer[]{loggedInUserId}));
        Platform.runLater(() -> {
            _userFxList.get().clear();
            users.forEach(user -> {
                var userFx = UserConverter.convertUserToUserFx(user);
                _userFxList.get().add(userFx);
            });
        });
    }

    public synchronized void initUsersByName(String name) throws ApplicationException {
        var users = (List<User>) ((UserDao) _userDao).findByNameLike(name);
        Platform.runLater(() -> {
            _userFxList.get().clear();
            users.forEach(user -> {
                _userFxList.get().add(UserConverter.convertUserToUserFx(user));
            });
        });
    }

    public synchronized void initUsersByNameExceptLoggedInUser(String name, int loggedInUserId) throws ApplicationException {
        var users = (List<User>) ((UserDao) _userDao).findByNameLikeExceptGivenUserIds(name, List.of(new Integer[]{loggedInUserId}));
        Platform.runLater(() -> {
            _userFxList.get().clear();
            users.forEach(user -> {
                _userFxList.get().add(UserConverter.convertUserToUserFx(user));
            });
        });
    }

    public ObjectProperty<ObservableList<UserFx>> usersProperty() {
        return _userFxList;
    }
}
