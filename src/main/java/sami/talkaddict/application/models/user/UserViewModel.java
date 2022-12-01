package sami.talkaddict.application.models.user;

import com.j256.ormlite.logger.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import net.synedra.validatorfx.Check;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.converters.UserConverter;

import java.sql.SQLException;
import java.util.List;

public class UserViewModel {
    private final ObjectProperty<UserFx> userFxObject = new SimpleObjectProperty<>(new UserFx());

    private final Logger _logger;
    private final GenericDao<User> userDao;

    public UserViewModel() {
        _logger = ProviderService.provideLogger(UserViewModel.class);
        userDao = ProviderService.provideDao(User.class);
    }

    public void saveOrUpdateUser() throws ApplicationException {
        var user = UserConverter.toUser(userFxObject.get());
        userDao.createOrUpdate(user);
    }

    public void getUserByEmail(String email) throws ApplicationException, SQLException {
        var filterByEmail = userDao.queryBuilder();
        filterByEmail.where().eq(Config.Database.EMAIL_COLUMN_NAME, email);

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

    public void isUsernameValid(Check.Context ctx) {
        if (usernameProperty().get() == null || usernameProperty().get().isEmpty()) {
            ctx.error("Username is required!");
        }

        if (usernameProperty().get().length() < Config.AuthTweaks.MIN_USERNAME_LENGTH) {
            ctx.error("Username must be at least " + Config.AuthTweaks.MIN_USERNAME_LENGTH + " characters long!");
        }

        if (usernameProperty().get().length() > Config.AuthTweaks.MAX_USERNAME_LENGTH) {
            ctx.error("Username must be at most " + Config.AuthTweaks.MAX_USERNAME_LENGTH + " characters long!");
        }

        if (!usernameProperty().get().matches(Config.AuthTweaks.USERNAME_REGEX)) {
            ctx.error("Username must contain only letters, numbers and underscores!");
        }
    }

    public void isUsernameUnique(Check.Context ctx) {
        try {
            var filterByUsername = userDao.queryBuilder();
            filterByUsername.where().eq(Config.Database.USERNAME_COLUMN_NAME, usernameProperty().get());

            List<User> users = (List<User>) userDao.findByFilter(filterByUsername);
            if (!users.isEmpty()) {
                ctx.error("Username is already taken!");
            }
        } catch (ApplicationException | SQLException ex) {
            ctx.error("Something went wrong!");
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        }
    }

    public void isEmailValid(Check.Context ctx) {
        if (emailProperty().get() == null || emailProperty().get().isEmpty()) {
            ctx.error("Email is required!");
        }

        if (!emailProperty().get().matches(Config.AuthTweaks.EMAIL_REGEX)) {
            ctx.error("Email must be valid!");
        }
    }

    public void isEmailUnique(Check.Context ctx) {
        try {
            var filterByEmail = userDao.queryBuilder();
            filterByEmail.where().eq(Config.Database.EMAIL_COLUMN_NAME, emailProperty().get());
            List<User> users = (List<User>) userDao.findByFilter(filterByEmail);
            if (!users.isEmpty()) {
                ctx.error("Email is already taken!");
            }
        } catch (SQLException | ApplicationException ex) {
            ctx.error("Something went wrong!");
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        }
    }

    public void isPasswordValid(Check.Context ctx) {
        if (passwordProperty().get() == null || passwordProperty().get().isEmpty()) {
            ctx.error("Password is required!");
        }

        if (passwordProperty().get().length() < Config.AuthTweaks.MIN_PASSWORD_LENGTH) {
            ctx.error("Password must be at least " + Config.AuthTweaks.MIN_PASSWORD_LENGTH + " characters long!");
        }

        if (!passwordProperty().get().matches(Config.AuthTweaks.PASSWORD_REGEX)) {
            ctx.error("Password must contain at least one uppercase letter, one lowercase letter and one number!");
        }
    }
}
