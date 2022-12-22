package sami.talkaddict.application.factories;

import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.scene.image.ImageView;
import sami.talkaddict.infrastructure.utils.managers.AvatarManager;
import sami.talkaddict.application.models.user.UserFx;

public class UserCellFactory extends MFXListCell<UserFx> {
    private final ImageView _userAvatarImageView;

    public UserCellFactory(MFXListView<UserFx> listView, UserFx data) {
        super(listView, data);
        setPrefHeight(100);
        _userAvatarImageView = new ImageView();
        AvatarManager.assignAvatarToImageView(_userAvatarImageView, data.Avatar.get());
        render(data);
    }

    @Override
    protected void render(UserFx data) {
        super.render(data);
//        if (_userAvatarImageView != null) {
//            getChildren().add(0, _userAvatarImageView);
//        }
    }
}

//import javafx.scene.Node;
//import javafx.scene.control.Label;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import sami.talkaddict.application.models.user.UserFx;
//import sami.talkaddict.infrastructure.utils.managers.AvatarManager;
//
//public class UserCellFactory {
//    public static Node getNode(UserFx data) {
//        var userAvatarImageView = new ImageView();
//        AvatarManager.assignAvatarToImageView(userAvatarImageView, data.Avatar.get());
//        var usernameLabel = new Label(data.Username.get());
//
//        var hbox = new HBox();
//        hbox.getChildren().addAll(userAvatarImageView, usernameLabel);
//        return hbox;
//    }
//}