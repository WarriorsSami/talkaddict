package sami.talkaddict.application.models.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sami.talkaddict.di.InjectorModule;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.utils.converters.UserConverter;

import java.sql.SQLException;
import java.util.List;

public class UserListViewModel {
    public ObservableList<UserFx> userFxObservableList = FXCollections.observableArrayList();

    private final GenericDao<User> userDao;

    public UserListViewModel() throws ApplicationException {
        userDao = InjectorModule.getDao(User.class);
        getAllUsers();
    }

    public void getAllUsers() throws ApplicationException {
        var users = (List<User>) userDao.findAll();
        userFxObservableList.clear();

        users.forEach(user -> {
            userFxObservableList.add(UserConverter.toUserFx(user));
        });
    }

    public void getUsersByName(String name) throws ApplicationException, SQLException {
        var filterByName = userDao.queryBuilder();
        filterByName.where().like("name", "%" + name + "%");

        var users = userDao.findByFilter(filterByName);
        userFxObservableList.clear();

        users.forEach(user -> {
            userFxObservableList.add(UserConverter.toUserFx(user));
        });
    }
}
