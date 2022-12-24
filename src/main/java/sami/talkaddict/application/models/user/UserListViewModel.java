package sami.talkaddict.application.models.user;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.daos.UserDao;
import sami.talkaddict.infrastructure.utils.converters.UserConverter;

import java.util.List;

public class UserListViewModel {
    private final ObjectProperty<ObservableList<UserFx>> _userFxObservableList = new SimpleObjectProperty<>(
            FXCollections.observableArrayList()
    );

    private final GenericDao<User> _userDao;

    public UserListViewModel() {
        _userDao = ProviderService.provideDao(User.class);
    }

    public void initAllUsers() throws ApplicationException {
        var users = (List<User>) _userDao.findAll();
        Platform.runLater(() -> {
            _userFxObservableList.get().clear();
            users.forEach(user -> {
                var userFx = UserConverter.convertUserToUserFx(user);
                _userFxObservableList.get().add(userFx);
            });
        });
    }

    public void initUsersByName(String name) throws ApplicationException {
        var users = (List<User>) ((UserDao) _userDao).findByNameLike(name);
        Platform.runLater(() -> {
            _userFxObservableList.get().clear();
            users.forEach(user -> {
                _userFxObservableList.get().add(UserConverter.convertUserToUserFx(user));
            });
        });
    }

    public ObjectProperty<ObservableList<UserFx>> usersProperty() {
        return _userFxObservableList;
    }
}
