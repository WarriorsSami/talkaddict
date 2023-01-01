package sami.talkaddict.domain.entities.user;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import sami.talkaddict.domain.entities.chat.DirectMessage;
import sami.talkaddict.domain.interfaces.BaseEntity;

@DatabaseTable(tableName = "user")
public class User implements BaseEntity {
        @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
        private Integer id;

        @DatabaseField(columnName = "name", canBeNull = false, unique = true)
        private String username;

        @DatabaseField(columnName = "pass", canBeNull = false)
        private String password;

        @DatabaseField(canBeNull = false, unique = true)
        private String email;

        @DatabaseField(dataType = DataType.LONG_STRING)
        private String description;

        @DatabaseField(columnName = "image", dataType = DataType.BYTE_ARRAY)
        private byte[] avatar;

        @DatabaseField(canBeNull = false, dataType = DataType.ENUM_INTEGER)
        private UserStatus status;

        @ForeignCollectionField(columnName = "sender_id", orderColumnName = "timestamp")
        private ForeignCollection<DirectMessage> sentMessages;

        @ForeignCollectionField(columnName = "receiver_id", orderColumnName = "timestamp")
        private ForeignCollection<DirectMessage> receivedMessages;

        public User() {}

        public User(String username, String password, String email, String description, byte[] avatar, UserStatus status) {
                this.username = username;
                this.password = password;
                this.email = email;
                this.description = description;
                this.avatar = avatar;
                this.status = status;
        }

        public User(Integer id, String username, String password, String email, String description, byte[] avatar, UserStatus status) {
                this.id = id;
                this.username = username;
                this.password = password;
                this.email = email;
                this.description = description;
                this.avatar = avatar;
                this.status = status;
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public byte[] getAvatar() {
                return avatar;
        }

        public void setAvatar(byte[] avatar) {
                this.avatar = avatar;
        }

        public UserStatus getStatus() {
                return status;
        }

        public void setStatus(UserStatus status) {
                this.status = status;
        }

        public ForeignCollection<DirectMessage> getSentMessages() {
                return sentMessages;
        }

        public void setSentMessages(ForeignCollection<DirectMessage> sentMessages) {
                this.sentMessages = sentMessages;
        }

        public ForeignCollection<DirectMessage> getReceivedMessages() {
                return receivedMessages;
        }

        public void setReceivedMessages(ForeignCollection<DirectMessage> receivedMessages) {
                this.receivedMessages = receivedMessages;
        }
}
