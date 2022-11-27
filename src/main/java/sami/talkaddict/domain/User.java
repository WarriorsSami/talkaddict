package sami.talkaddict.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public record User(
        @DatabaseField(generatedId = true)
        Integer id,
        String username,
        String password,
        String email,
        @DatabaseField(canBeNull = true)
        String description,
        String avatarFilename) {
}
