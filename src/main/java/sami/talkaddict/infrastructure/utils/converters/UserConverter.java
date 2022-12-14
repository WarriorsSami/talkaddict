package sami.talkaddict.infrastructure.utils.converters;

import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.domain.entities.user.User;

public class UserConverter {
    public static User convertUserFxToUser(UserFx userFx) {
        return new User(
                userFx.Id.get(),
                userFx.Username.get(),
                userFx.Password.get(),
                userFx.Email.get(),
                userFx.Description.get(),
                userFx.Avatar.get(),
                userFx.Status.get()
        );
    }

    public static UserFx convertUserToUserFx(User user) {
        UserFx userFx = new UserFx();
        userFx.Id.set(user.getId());
        userFx.Username.set(user.getUsername());
        userFx.Password.set(user.getPassword());
        userFx.Email.set(user.getEmail());
        userFx.Description.set(user.getDescription());
        userFx.Avatar.set(user.getAvatar());
        userFx.Status.set(user.getStatus());
        return userFx;
    }
}
