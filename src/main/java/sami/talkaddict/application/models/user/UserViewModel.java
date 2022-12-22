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
import sami.talkaddict.infrastructure.daos.UserDao;
import sami.talkaddict.di.Config;
import sami.talkaddict.infrastructure.utils.converters.UserConverter;

public class UserViewModel {
    private final ObjectProperty<UserFx> _userFxObject = new SimpleObjectProperty<>(new UserFx());

    private final Logger _logger;
    private final GenericDao<User> _userDao;

    public UserViewModel() {
        _logger = ProviderService.provideLogger(UserViewModel.class);
        _userDao = ProviderService.provideDao(User.class);
    }

    public void initFromUser(User user) {
        var userFx = UserConverter.convertUserToUserFx(user);
        _userFxObject.get().initFromUserFx(userFx);
    }

    public void saveOrUpdateUser() throws ApplicationException {
        var user = UserConverter.convertUserFxToUser(_userFxObject.get());
        _userDao.createOrUpdate(user);
        initUserByEmail(user.getEmail());
    }

    public void initUserByEmail(String email) throws ApplicationException {
        var user = ((UserDao) _userDao).findByEmail(email);
        _userFxObject.set(UserConverter.convertUserToUserFx(user));
    }

    public IntegerProperty idProperty() {
        return _userFxObject.get().Id;
    }

    public StringProperty usernameProperty() {
        return _userFxObject.get().Username;
    }

    public StringProperty emailProperty() {
        return _userFxObject.get().Email;
    }

    public StringProperty passwordProperty() {
        return _userFxObject.get().Password;
    }

    public StringProperty descriptionProperty() {
        return _userFxObject.get().Description;
    }

    public ObjectProperty<byte[]> avatarProperty() {
        return _userFxObject.get().Avatar;
    }

    public void isUsernameValid(Check.Context ctx) {
        if (usernameProperty().get() == null || usernameProperty().get().isEmpty()) {
            ctx.error("Username is required!");
        }

        if (usernameProperty().get().length() < Config.ValidationTweaks.MIN_USERNAME_LENGTH) {
            ctx.error("Username must be at least " + Config.ValidationTweaks.MIN_USERNAME_LENGTH + " characters long!");
        }

        if (usernameProperty().get().length() > Config.ValidationTweaks.MAX_USERNAME_LENGTH) {
            ctx.error("Username must be at most " + Config.ValidationTweaks.MAX_USERNAME_LENGTH + " characters long!");
        }

        if (!usernameProperty().get().matches(Config.ValidationTweaks.USERNAME_REGEX)) {
            ctx.error("Username must contain only letters, numbers and underscores!");
        }
    }

    public void isUsernameUnique(Check.Context ctx) {
        try {
            var user = ((UserDao) _userDao).findByName(usernameProperty().get());
            if (user != null && user.getId() != idProperty().get()) {
                ctx.error("Username is already taken!");
            }
        } catch (ApplicationException ex) {
            ctx.error("Something went wrong!");
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        }
    }

    public void isEmailValid(Check.Context ctx) {
        if (emailProperty().get() == null || emailProperty().get().isEmpty()) {
            ctx.error("Email is required!");
        }

        if (!emailProperty().get().matches(Config.ValidationTweaks.EMAIL_REGEX)) {
            ctx.error("Email must be valid!");
        }
    }

    public void isEmailUnique(Check.Context ctx) {
        try {
            var user = ((UserDao) _userDao).findByEmail(emailProperty().get());
            if (user != null) {
                ctx.error("Email is already taken!");
            }
        } catch (ApplicationException ex) {
            ctx.error("Something went wrong!");
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        }
    }

    public void isPasswordValid(Check.Context ctx) {
        if (passwordProperty().get() == null || passwordProperty().get().isEmpty()) {
            ctx.error("Password is required!");
        }

        if (passwordProperty().get().length() < Config.ValidationTweaks.MIN_PASSWORD_LENGTH) {
            ctx.error("Password must be at least " + Config.ValidationTweaks.MIN_PASSWORD_LENGTH + " characters long!");
        }

        if (!passwordProperty().get().matches(Config.ValidationTweaks.PASSWORD_REGEX)) {
            ctx.error("Password must contain at least one uppercase letter, one lowercase letter and one number!");
        }
    }

    public void isDescriptionValid(Check.Context ctx) {
        if (descriptionProperty().get().length() > Config.ValidationTweaks.MAX_DESCRIPTION_LENGTH) {
            ctx.error("Description must be at most " + Config.ValidationTweaks.MAX_DESCRIPTION_LENGTH + " characters long!");
        }
    }
}
