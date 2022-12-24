package sami.talkaddict.infrastructure.utils.managers;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.di.Config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class AvatarManager {
    public static Iterable<File> getFilesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        return List.of(Objects.requireNonNull(directory.listFiles()));
    }

    public static File getRandomFileFromDirectory(String directoryPath) {
        Iterable<File> files = getFilesFromDirectory(directoryPath);
        int randomIndex = (int) (Math.random() * ((List<File>) files).size());
        return ((List<File>) files).get(randomIndex);
    }

    public static byte[] getRandomAvatar() {
        var file = getRandomFileFromDirectory(
                DotenvManager.get(Config.ValidationTweaks.DEFAULT_AVATAR_DIRECTORY_PATH)
        );
        return convertFileToByteArray(file);
    }

    public static Image convertByteArrayToImage(byte[] bytes) {
        return new Image(new ByteArrayInputStream(bytes));
    }

    public static byte[] convertFileToByteArray(File file) {
        try {
            return file.toURI().toURL().openStream().readAllBytes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Node getAvatarClip(double width, double height) {
        var rectangularClip = new Rectangle(
                width,
                height
        );
        rectangularClip.setArcWidth(Config.FxmlSettings.AVATAR_CLIP_ARC_WIDTH);
        rectangularClip.setArcHeight(Config.FxmlSettings.AVATAR_CLIP_ARC_HEIGHT);
        return rectangularClip;
    }

    public static void assignAvatarToImageView(ImageView imageView, byte[] avatar, double fitWidth, double fitHeight) {
        imageView.setImage(convertByteArrayToImage(avatar));
        imageView.setClip(getAvatarClip(fitWidth, fitHeight));
    }

    public static Node getAvatarForUser(UserFx user) {
        var avatarImageView = new ImageView();
        avatarImageView.setFitHeight(40);
        avatarImageView.setFitWidth(40);
        AvatarManager.assignAvatarToImageView(
                avatarImageView,
                user.Avatar.get(),
                avatarImageView.getFitWidth(),
                avatarImageView.getFitHeight()
        );
        var statusClip = new Circle(avatarImageView.getFitWidth(), avatarImageView.getFitHeight(), 5);
        switch (user.Status.get()) {
            case ONLINE -> statusClip.getStyleClass().add("online-status");
            case OFFLINE -> statusClip.getStyleClass().add("offline-status");
            case BUSY -> statusClip.getStyleClass().add("busy-status");
            case AWAY -> statusClip.getStyleClass().add("away-status");
        }
        statusClip.getStyleClass().add("online-status");
        var statusHBox = new HBox(statusClip);
        statusHBox.setAlignment(Pos.CENTER_RIGHT);
        var statusBorderPane = new BorderPane();
        statusBorderPane.setBottom(statusHBox);

        return new StackPane(avatarImageView, statusBorderPane);
    }
}
