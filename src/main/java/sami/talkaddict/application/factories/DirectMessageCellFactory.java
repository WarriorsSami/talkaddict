package sami.talkaddict.application.factories;

import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sami.talkaddict.application.models.chat.DirectMessageFx;
import sami.talkaddict.di.Config;
import sami.talkaddict.infrastructure.utils.managers.ImageManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

public class DirectMessageCellFactory extends MFXListCell<DirectMessageFx> {
    public DirectMessageCellFactory(MFXListView<DirectMessageFx> listView, DirectMessageFx data) {
        super(listView, data);
        setPrefHeight(200);
        setPadding(new Insets(10, 10, 10, 10));
        getStyleClass().add("message-cell");
        render(data);
    }

//    @Override
//    protected void render(DirectMessageFx data) {
//        var avatarImageView = ImageManager.getAvatarForUser(data.Sender.get(), false);
//        var avatarVBox = new VBox(avatarImageView);
//        avatarVBox.setAlignment(Pos.CENTER);
//
//        var senderNameLabel = new Label(data.Sender.get().getUsername());
//        senderNameLabel.setFont(Font.font("Ubuntu Mono", FontWeight.BOLD, 12));
//        senderNameLabel.setTextFill(Color.GREEN);
//        var createdAtLabel = new Label(data.CreatedAt.get().toLocalDateTime().toString());
//        var senderHBox = new HBox(5, senderNameLabel, createdAtLabel);
//        senderHBox.setAlignment(Pos.CENTER_LEFT);
//
//        var messageBorderPane = new BorderPane();
//        messageBorderPane.setLeft(avatarImageView);
//        messageBorderPane.setTop(senderHBox);
//
//        if (data.hasImage() && !data.hasText()) {
//            var imageView = ImageManager.getImageView(data.MessageImage.get(),
//                    Config.FxmlSettings.MESSAGE_IMAGE_WIDTH,
//                    Config.FxmlSettings.MESSAGE_IMAGE_HEIGHT);
//
//            messageBorderPane.setCenter(imageView);
//        } else if (data.hasText() && !data.hasImage()) {
//            SceneFxManager.addMessageTextLabelToPane(data, messageBorderPane);
//        } else if (data.hasText() && data.hasImage()) {
//            var imageView = ImageManager.getImageView(data.MessageImage.get(),
//                    Config.FxmlSettings.MESSAGE_IMAGE_WIDTH,
//                    Config.FxmlSettings.MESSAGE_IMAGE_HEIGHT);
//            var imageHBox = new HBox(imageView);
//            imageHBox.setAlignment(Pos.CENTER);
//
//            SceneFxManager.addMessageTextLabelToPane(data, messageBorderPane);
//            messageBorderPane.setBottom(imageHBox);
//        }
//
//        getChildren().remove(0, getChildren().size());
//        getChildren().add(messageBorderPane);
//    }

    @Override
    protected void render(DirectMessageFx data) {
        var avatarImageView = ImageManager.getAvatarForUser(data.Sender.get(), false);
        var avatarVBox = new VBox(avatarImageView);
        avatarVBox.setAlignment(Pos.CENTER);

        var senderNameLabel = new Label(data.Sender.get().getUsername());
        senderNameLabel.setFont(Font.font("Ubuntu Mono", FontWeight.BOLD, 12));
        senderNameLabel.setTextFill(Color.GREEN);
        var createdAtLabel = new Label(data.CreatedAt.get().toLocalDateTime().toString());
        var senderHBox = new HBox(5, senderNameLabel, createdAtLabel);
        senderHBox.setAlignment(Pos.CENTER_LEFT);

        BorderPane messageBorderPane;
        if (getChildren().isEmpty()) {
            messageBorderPane = new BorderPane();
            messageBorderPane.setLeft(avatarImageView);
            messageBorderPane.setTop(senderHBox);
            getChildren().add(messageBorderPane);
        } else {
            messageBorderPane = (BorderPane) getChildren().get(getChildren().size() - 1);
            messageBorderPane.setLeft(avatarImageView);
            messageBorderPane.setTop(senderHBox);
            messageBorderPane.setCenter(null);
            messageBorderPane.setBottom(null);
        }

        if (data.hasImage() && !data.hasText()) {
            var imageView = ImageManager.getImageView(data.MessageImage.get(),
                    Config.FxmlSettings.MESSAGE_IMAGE_WIDTH,
                    Config.FxmlSettings.MESSAGE_IMAGE_HEIGHT);

            messageBorderPane.setCenter(imageView);
        } else if (data.hasText() && !data.hasImage()) {
            SceneFxManager.addMessageTextLabelToPane(data, messageBorderPane);
        } else if (data.hasText() && data.hasImage()) {
            var imageView = ImageManager.getImageView(data.MessageImage.get(),
                    Config.FxmlSettings.MESSAGE_IMAGE_WIDTH,
                    Config.FxmlSettings.MESSAGE_IMAGE_HEIGHT);
            var imageHBox = new HBox(imageView);
            imageHBox.setAlignment(Pos.CENTER);
            SceneFxManager.addMessageTextLabelToPane(data, messageBorderPane);
            messageBorderPane.setBottom(imageHBox);
        }
    }
}
