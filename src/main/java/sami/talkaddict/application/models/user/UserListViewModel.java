package sami.talkaddict.application.models.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.di.Config;
import sami.talkaddict.infrastructure.utils.converters.UserConverter;

import java.sql.SQLException;
import java.util.List;

public class UserListViewModel {
    private ObservableList<UserFx> _userFxObservableList = FXCollections.observableArrayList();

    private final GenericDao<User> _userDao;

    public UserListViewModel() {
        _userDao = ProviderService.provideDao(User.class);
    }

    public void initAllUsers() throws ApplicationException {
        var users = (List<User>) _userDao.findAll();
        _userFxObservableList.clear();

        users.forEach(user -> {
            _userFxObservableList.add(UserConverter.convertUserToUserFx(user));
        });
    }

    public void initUsersByName(String name) throws ApplicationException, SQLException {
        var filterByName = _userDao.queryBuilder();
        filterByName.where().like(Config.Database.USERNAME_COLUMN_NAME, "%" + name + "%");

        var users = _userDao.findByFilter(filterByName);
        _userFxObservableList.clear();

        users.forEach(user -> {
            _userFxObservableList.add(UserConverter.convertUserToUserFx(user));
        });
    }

    public ObservableList<UserFx> getUserFxObservableList() {
        return _userFxObservableList;
    }

    public void setUserFxObservableList(ObservableList<UserFx> userFxObservableList) {
        _userFxObservableList = userFxObservableList;
    }
}
