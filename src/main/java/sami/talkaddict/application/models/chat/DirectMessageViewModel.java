package sami.talkaddict.application.models.chat;

import javafx.beans.property.*;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.chat.DirectMessage;
import sami.talkaddict.domain.entities.user.User;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.utils.converters.DirectMessageConverter;

import java.sql.Timestamp;
import java.time.Instant;

public class DirectMessageViewModel {
    private final ObjectProperty<DirectMessageFx> _directMessageFxObject = new SimpleObjectProperty<>(new DirectMessageFx());

    private final GenericDao<DirectMessage> _directMessageDao;
    private final GenericDao<User> _userDao;

    public DirectMessageViewModel() {
        _directMessageDao = ProviderService.provideDao(DirectMessage.class);
        _userDao = ProviderService.provideDao(User.class);
    }

    public void initFromDirectMessage(DirectMessage directMessage) {
        _directMessageFxObject.set(DirectMessageConverter.convertDirectMessageToDirectMessageFx(directMessage));
    }

    public synchronized void saveOrUpdateDirectMessage(int loggedInUserId, int otherUserId) throws ApplicationException {
        createdAtProperty().set(Timestamp.from(Instant.now()));
        senderProperty().set((User) _userDao.findById(loggedInUserId));
        receiverProperty().set((User) _userDao.findById(otherUserId));
        _directMessageDao.createOrUpdate(DirectMessageConverter.convertDirectMessageFxToDirectMessage(_directMessageFxObject.get()));
    }

    public IntegerProperty idProperty() {
        return _directMessageFxObject.get().Id;
    }

    public StringProperty messageTextProperty() {
        return _directMessageFxObject.get().MessageText;
    }

    public ObjectProperty<byte[]> messageImageProperty() {
        return _directMessageFxObject.get().MessageImage;
    }

    public BooleanProperty isReadProperty() {
        return _directMessageFxObject.get().IsRead;
    }

    public ObjectProperty<Timestamp> createdAtProperty() {
        return _directMessageFxObject.get().CreatedAt;
    }

    public ObjectProperty<User> senderProperty() {
        return _directMessageFxObject.get().Sender;
    }

    public ObjectProperty<User> receiverProperty() {
        return _directMessageFxObject.get().Receiver;
    }
}
