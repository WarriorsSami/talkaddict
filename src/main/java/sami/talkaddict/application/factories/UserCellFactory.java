package sami.talkaddict.application.factories;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.infrastructure.utils.managers.ImageManager;

public class UserCellFactory extends ListCell<UserFx> {
    @Override
    protected void updateItem(UserFx item, boolean empty) {
        super.updateItem(item, empty);
        getStyleClass().add("user-cell");
        setPadding(new Insets(10, 5, 10, 5));

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setFont(Font.font(14));
            setText(item.Username.get());

            var hbox = new HBox(10, ImageManager.getAvatarForUserFx(item));
            hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(hbox);
        }
    }
}