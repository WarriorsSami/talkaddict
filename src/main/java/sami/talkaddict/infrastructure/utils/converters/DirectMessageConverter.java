package sami.talkaddict.infrastructure.utils.converters;

import sami.talkaddict.application.models.chat.DirectMessageFx;
import sami.talkaddict.domain.entities.chat.DirectMessage;

public class DirectMessageConverter {
    public static DirectMessage convertDirectMessageFxToDirectMessage(DirectMessageFx directMessageFx) {
        return new DirectMessage(
                directMessageFx.Id.get(),
                directMessageFx.MessageText.get(),
                directMessageFx.MessageImage.get(),
                directMessageFx.IsRead.get(),
                directMessageFx.CreatedAt.get(),
                directMessageFx.Sender.get(),
                directMessageFx.Receiver.get()
        );
    }

    public static DirectMessageFx convertDirectMessageToDirectMessageFx(DirectMessage directMessage) {
        DirectMessageFx directMessageFx = new DirectMessageFx();
        directMessageFx.Id.set(directMessage.getId());
        directMessageFx.MessageText.set(directMessage.getMessageText());
        directMessageFx.MessageImage.set(directMessage.getMessageImage());
        directMessageFx.IsRead.set(directMessage.isRead());
        directMessageFx.CreatedAt.set(directMessage.getCreatedAt());
        directMessageFx.Sender.set(directMessage.getSender());
        directMessageFx.Receiver.set(directMessage.getReceiver());
        return directMessageFx;
    }
}
