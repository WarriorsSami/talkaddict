package sami.talkaddict.application.models.chat;

import javafx.beans.property.*;
import sami.talkaddict.domain.entities.user.User;

import java.sql.Timestamp;

public class DirectMessageFx {
    public IntegerProperty Id = new SimpleIntegerProperty();
    public StringProperty MessageText = new SimpleStringProperty();
    public ObjectProperty<byte[]> MessageImage = new SimpleObjectProperty<>();
    public BooleanProperty IsRead = new SimpleBooleanProperty();
    public ObjectProperty<Timestamp> CreatedAt = new SimpleObjectProperty<>();
    public ObjectProperty<User> Sender = new SimpleObjectProperty<>();
    public ObjectProperty<User> Receiver = new SimpleObjectProperty<>();

    public void initFromDirectMessageFx(DirectMessageFx directMessageFx) {
        Id.set(directMessageFx.Id.get());
        MessageText.set(directMessageFx.MessageText.get());
        MessageImage.set(directMessageFx.MessageImage.get());
        IsRead.set(directMessageFx.IsRead.get());
        CreatedAt.set(directMessageFx.CreatedAt.get());
        Sender.set(directMessageFx.Sender.get());
        Receiver.set(directMessageFx.Receiver.get());
    }

    public boolean hasImage() {
        return MessageImage.get() != null;
    }

    public boolean hasText() {
        return MessageText.get() != null;
    }
}
