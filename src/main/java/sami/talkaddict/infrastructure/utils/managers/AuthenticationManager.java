package sami.talkaddict.infrastructure.utils.managers;

import sami.talkaddict.application.models.user.LoginDto;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.utils.Config;

import java.sql.SQLException;

public class AuthenticationManager {
    private static final GenericDao<User> _userDao = ProviderService.provideDao(User.class);

    public static void register(UserViewModel userModel) throws ApplicationException, SQLException {
        if (userModel == null) {
            throw new ApplicationException("Invalid register data provided!");
        }

        var rawPassword = userModel.passwordProperty().get();
        var encodedPassword = PasswordManager.encode(rawPassword);
        // TODO: remove this workaround
        var newUserViewModel = new UserViewModel();
        newUserViewModel.emailProperty().set(userModel.emailProperty().get());
        newUserViewModel.usernameProperty().set(userModel.usernameProperty().get());
        newUserViewModel.passwordProperty().set(encodedPassword);
        newUserViewModel.saveOrUpdateUser();

        var findUserByEmail = _userDao.queryBuilder();
        findUserByEmail.where().eq(Config.Database.EMAIL_COLUMN_NAME, userModel.emailProperty().get());
        var registeredUser = _userDao.findByFilter(findUserByEmail).iterator().next();
        PreferencesManager.setKey(Config.Preferences.LOGGED_IN_USER_ID_KEY, registeredUser.getId().toString());
    }

    public static void login(LoginDto dto) throws ApplicationException, SQLException {
        if (dto == null) {
            throw new ApplicationException("Invalid login credentials!");
        }

        var filterByEmail = _userDao.queryBuilder();
        filterByEmail.where().eq(Config.Database.EMAIL_COLUMN_NAME, dto.Email);
        var user = _userDao.findByFilter(filterByEmail).iterator().next();

        if (user == null) {
            throw new ApplicationException("Invalid login credentials!");
        }

        if (!PasswordManager.verify(dto.Password, user.getPassword())) {
            throw new ApplicationException("Invalid login credentials!");
        }

        PreferencesManager.setKey(Config.Preferences.LOGGED_IN_USER_ID_KEY, user.getId().toString());
    }

    public static User getLoggedInUser() throws ApplicationException, SQLException {
        var id = PreferencesManager.getKey(Config.Preferences.LOGGED_IN_USER_ID_KEY);
        if (id == null) {
            throw new ApplicationException("No user is logged in!");
        }

        var filterById = _userDao.queryBuilder();
        filterById.where().eq("id", id);
        return _userDao.findByFilter(filterById).iterator().next();
    }

    public static void logout() {
        PreferencesManager.removeKey(Config.Preferences.LOGGED_IN_USER_ID_KEY);
    }
}
