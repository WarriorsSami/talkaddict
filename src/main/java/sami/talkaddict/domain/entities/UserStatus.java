package sami.talkaddict.domain.entities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public enum UserStatus {
    ONLINE,
    OFFLINE,
    BUSY,
    AWAY;

    public static ObservableList<UserStatus> getAllStatuses() {
        return FXCollections.observableArrayList(Arrays.asList(UserStatus.values()));
    }
}
