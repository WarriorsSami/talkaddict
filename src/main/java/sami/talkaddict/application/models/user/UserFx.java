package sami.talkaddict.application.models.user;

import javafx.beans.property.*;

public class UserFx {
    public IntegerProperty Id = new SimpleIntegerProperty();
    public StringProperty Username = new SimpleStringProperty();
    public StringProperty Password = new SimpleStringProperty();
    public StringProperty Email = new SimpleStringProperty();
    public StringProperty Description = new SimpleStringProperty();
    public ObjectProperty<byte[]> Avatar = new SimpleObjectProperty<>();
}
