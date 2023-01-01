package sami.talkaddict.application.models.user;

import javafx.beans.property.*;
import sami.talkaddict.domain.entities.chat.DirectMessage;
import sami.talkaddict.domain.entities.user.UserStatus;

public class UserFx {
    public IntegerProperty Id = new SimpleIntegerProperty();
    public StringProperty Username = new SimpleStringProperty();
    public StringProperty Password = new SimpleStringProperty();
    public StringProperty Email = new SimpleStringProperty();
    public StringProperty Description = new SimpleStringProperty();
    public ObjectProperty<byte[]> Avatar = new SimpleObjectProperty<>();
    public ObjectProperty<UserStatus> Status = new SimpleObjectProperty<>();
    public ListProperty<DirectMessage> ReceivedMessages = new SimpleListProperty<>();
    public ListProperty<DirectMessage> SentMessages = new SimpleListProperty<>();

    public void initFromUserFx(UserFx userFx) {
        Id.set(userFx.Id.get());
        Username.set(userFx.Username.get());
        Password.set(userFx.Password.get());
        Email.set(userFx.Email.get());
        Description.set(userFx.Description.get());
        Avatar.set(userFx.Avatar.get());
        Status.set(userFx.Status.get());
        ReceivedMessages.set(userFx.ReceivedMessages.get());
        SentMessages.set(userFx.SentMessages.get());
    }
}
