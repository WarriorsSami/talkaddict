package sami.talkaddict.application.models.chat;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.entities.chat.DirectMessage;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.domain.interfaces.GenericDao;
import sami.talkaddict.infrastructure.daos.DirectMessageDao;
import sami.talkaddict.infrastructure.utils.converters.DirectMessageConverter;

import java.util.List;

public class DirectMessageListViewModel {
    private final ObjectProperty<ObservableList<DirectMessageFx>> _directMessageFxList = new SimpleObjectProperty<>(
            FXCollections.observableArrayList()
    );

    private final GenericDao<DirectMessage> _directMessageDao;

    public DirectMessageListViewModel() {
        _directMessageDao = ProviderService.provideDao(DirectMessage.class);
    }

    public synchronized void initDirectMessagesByReceiverIdAndSenderIdAndViceVersa(int receiverId, int senderId) throws ApplicationException {
        var directMessages = (List<DirectMessage>) ((DirectMessageDao) _directMessageDao)
                .findByReceiverIdAndSenderIdAndViceVersa(receiverId, senderId);
        Platform.runLater( () -> {
            _directMessageFxList.get().clear();
            directMessages.forEach(directMessage -> {
                _directMessageFxList.get().add(DirectMessageConverter.convertDirectMessageToDirectMessageFx(directMessage));
            });
        });
    }

    public synchronized void deleteDirectMessageById(int id, int receiverId, int senderId) throws ApplicationException {
        _directMessageDao.deleteById(id);
        initDirectMessagesByReceiverIdAndSenderIdAndViceVersa(receiverId, senderId);
    }

    public ObjectProperty<ObservableList<DirectMessageFx>> directMessagesProperty() {
        return _directMessageFxList;
    }
}
