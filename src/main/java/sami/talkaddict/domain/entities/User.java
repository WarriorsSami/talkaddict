package sami.talkaddict.domain.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;
import java.util.Objects;

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

        @Override
        public String toString() {
                return "User{" +
                        "id=" + id +
                        ", username='" + username + '\'' +
                        ", password='" + password + '\'' +
                        ", email='" + email + '\'' +
                        ", description='" + description + '\'' +
                        ", avatar=" + Arrays.toString(avatar) +
                        ", status=" + status +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof User user)) return false;
                return id.equals(user.id) && username.equals(user.username)
                        && password.equals(user.password) && email.equals(user.email)
                        && description.equals(user.description) && Arrays.equals(avatar, user.avatar) && status == user.status;
        }

        @Override
        public int hashCode() {
                int result = Objects.hash(id, username, password, email, description, status);
                result = 31 * result + Arrays.hashCode(avatar);
                return result;
        }
}
