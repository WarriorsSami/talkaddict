package sami.talkaddict.application.factories;

import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.scene.layout.HBox;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.infrastructure.utils.managers.ImageManager;

public class UserCellFactory extends MFXListCell<UserFx> {
    public UserCellFactory(MFXListView<UserFx> listView, UserFx data) {
        super(listView, data);
        setPrefHeight(60);
        getStyleClass().add("user-cell");
        render(data);
    }

    @Override
    protected void render(UserFx data) {
        super.render(data);

        var hbox = new HBox(10, ImageManager.getAvatarForUserFx(data));
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        getChildren().get(getChildren().size() - 1).getStyleClass().add("user-label");
        getChildren().add(0, hbox);
    }
}