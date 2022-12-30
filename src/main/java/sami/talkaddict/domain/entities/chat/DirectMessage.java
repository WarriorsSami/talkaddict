package sami.talkaddict.domain.entities.chat;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import sami.talkaddict.domain.entities.user.User;
import sami.talkaddict.domain.interfaces.BaseEntity;

import java.sql.Timestamp;

@DatabaseTable(tableName = "direct_message")
public class DirectMessage implements BaseEntity {
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Integer id;

    @DatabaseField(columnName = "text", dataType = DataType.LONG_STRING)
    private String messageText;

    @DatabaseField(columnName = "image", dataType = DataType.BYTE_ARRAY)
    private byte[] messageImage;

    @DatabaseField(columnName = "is_read", canBeNull = false, dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean isRead;

    @DatabaseField(columnName = "timestamp", canBeNull = false, dataType = DataType.TIME_STAMP)
    private Timestamp createdAt;

    @DatabaseField(columnName = "sender_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private User sender;

    @DatabaseField(columnName = "receiver_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private User receiver;

    public DirectMessage() {}

    public DirectMessage(Integer id, String messageText, byte[] messageImage, boolean isRead, Timestamp createdAt, User sender, User receiver) {
        this.id = id;
        this.messageText = messageText;
        this.messageImage = messageImage;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.sender = sender;
        this.receiver = receiver;
    }

    public DirectMessage(String messageText, byte[] messageImage, boolean isRead, Timestamp createdAt, User sender, User receiver) {
        this.messageText = messageText;
        this.messageImage = messageImage;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public byte[] getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(byte[] messageImage) {
        this.messageImage = messageImage;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean hasImageMessage() {
        return this.messageImage != null;
    }

    public boolean hasTextMessage() {
        return this.messageText != null;
    }
}
