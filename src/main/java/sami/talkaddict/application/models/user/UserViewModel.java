package sami.talkaddict.application.models.user;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import sami.talkaddict.di.InjectorModule;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.utils.converters.UserConverter;

import java.sql.SQLException;

public class UserViewModel {
    private ObjectProperty<UserFx> userFxObject = new SimpleObjectProperty<>(new UserFx());

    private final GenericDao<User> userDao;

    public UserViewModel() {
        userDao = InjectorModule.getDao(User.class);
    }

    public void saveOrUpdateUser() throws ApplicationException {
        var user = UserConverter.toUser(userFxObject.get());
        userDao.createOrUpdate(user);
    }

    public void getUserByEmail(String email) throws ApplicationException, SQLException {
        var filterByEmail = userDao.queryBuilder();
        filterByEmail.where().eq("email", email);

        var user = userDao.findByFilter(filterByEmail).iterator().next();
        userFxObject.set(UserConverter.toUserFx(user));
    }

    public IntegerProperty idProperty() {
        return userFxObject.get().Id;
    }

    public StringProperty usernameProperty() {
        return userFxObject.get().Username;
    }

    public StringProperty emailProperty() {
        return userFxObject.get().Email;
    }

    public StringProperty passwordProperty() {
        return userFxObject.get().Password;
    }

    public StringProperty descriptionProperty() {
        return userFxObject.get().Description;
    }

    public ObjectProperty<byte[]> avatarProperty() {
        return userFxObject.get().Avatar;
    }
}
