package sami.talkaddict.infrastructure.utils.managers;

import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.utils.Config;

import java.sql.SQLException;
import java.util.List;

public class AuthenticationManager {
    private static final GenericDao<User> _userDao = ProviderService.provideDao(User.class);

    public static void register(UserViewModel dto) throws ApplicationException, SQLException {
        if (dto == null) {
            throw new ApplicationException("Invalid register data provided!");
        }

        var rawPassword = dto.passwordProperty().get();
        var encodedPassword = PasswordManager.encode(rawPassword);
        // TODO: remove this workaround
        var newUserViewModel = new UserViewModel();
        newUserViewModel.emailProperty().set(dto.emailProperty().get());
        newUserViewModel.usernameProperty().set(dto.usernameProperty().get());
        newUserViewModel.passwordProperty().set(encodedPassword);
        newUserViewModel.saveOrUpdateUser();

        var findUserByEmail = _userDao.queryBuilder();
        findUserByEmail.where().eq(Config.Database.EMAIL_COLUMN_NAME, dto.emailProperty().get());
        var registeredUser = _userDao.findByFilter(findUserByEmail).iterator().next();
        PreferencesManager.setKey(Config.Preferences.LOGGED_IN_USER_ID_KEY, registeredUser.getId().toString());
    }

    public static void login(UserViewModel dto) throws ApplicationException, SQLException {
        if (dto == null) {
            throw new ApplicationException("Invalid login credentials!");
        }

        var filterByEmail = _userDao.queryBuilder();
        filterByEmail.where().eq(Config.Database.EMAIL_COLUMN_NAME, dto.emailProperty().get());
        List<User> users = (List<User>) _userDao.findByFilter(filterByEmail);

        if (users.size() != 1) {
            throw new ApplicationException("Invalid login credentials!");
        }

        var user = users.iterator().next();
        if (!PasswordManager.verify(dto.passwordProperty().get(), user.getPassword())) {
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
