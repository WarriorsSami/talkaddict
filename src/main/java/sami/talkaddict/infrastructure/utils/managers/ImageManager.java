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
import javafx.stage.FileChooser;
import sami.talkaddict.application.models.user.UserFx;
import sami.talkaddict.di.Config;
import sami.talkaddict.domain.entities.user.User;
import sami.talkaddict.domain.entities.user.UserStatus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class ImageManager {
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

    public static void assignStatusToClip(Circle statusClip, UserStatus status) {
        // remove all style classes from the status clip
        statusClip.getStyleClass().removeAll(
                Config.FxmlSettings.AVATAR_STATUS_ONLINE_STYLE_CLASS,
                Config.FxmlSettings.AVATAR_STATUS_OFFLINE_STYLE_CLASS,
                Config.FxmlSettings.AVATAR_STATUS_BUSY_STYLE_CLASS,
                Config.FxmlSettings.AVATAR_STATUS_AWAY_STYLE_CLASS
        );
        switch (status) {
            case ONLINE -> statusClip.getStyleClass().add(Config.FxmlSettings.AVATAR_STATUS_ONLINE_STYLE_CLASS);
            case OFFLINE -> statusClip.getStyleClass().add(Config.FxmlSettings.AVATAR_STATUS_OFFLINE_STYLE_CLASS);
            case BUSY -> statusClip.getStyleClass().add(Config.FxmlSettings.AVATAR_STATUS_BUSY_STYLE_CLASS);
            case AWAY -> statusClip.getStyleClass().add(Config.FxmlSettings.AVATAR_STATUS_AWAY_STYLE_CLASS);
        }
    }

    public static Node getAvatarForUserFx(UserFx user) throws NullPointerException {
        var avatarImageView = new ImageView();
        avatarImageView.setFitHeight(40);
        avatarImageView.setFitWidth(40);
        ImageManager.assignAvatarToImageView(
                avatarImageView,
                user.Avatar.get(),
                avatarImageView.getFitWidth(),
                avatarImageView.getFitHeight()
        );
        var statusClip = new Circle(avatarImageView.getFitWidth(), avatarImageView.getFitHeight(), 5);
        ImageManager.assignStatusToClip(statusClip, user.Status.get());
        var statusHBox = new HBox(statusClip);
        statusHBox.setAlignment(Pos.CENTER_RIGHT);
        var statusBorderPane = new BorderPane();
        statusBorderPane.setBottom(statusHBox);

        return new StackPane(avatarImageView, statusBorderPane);
    }

    public static Node getAvatarForUser(User user, boolean addStatus) {
        var avatarImageView = new ImageView();
        avatarImageView.setFitHeight(40);
        avatarImageView.setFitWidth(40);
        ImageManager.assignAvatarToImageView(
                avatarImageView,
                user.getAvatar(),
                avatarImageView.getFitWidth(),
                avatarImageView.getFitHeight()
        );

        if (addStatus) {
            var statusClip = new Circle(avatarImageView.getFitWidth(), avatarImageView.getFitHeight(), 5);
            ImageManager.assignStatusToClip(statusClip, user.getStatus());
            var statusHBox = new HBox(statusClip);
            statusHBox.setAlignment(Pos.CENTER_RIGHT);
            var statusBorderPane = new BorderPane();
            statusBorderPane.setBottom(statusHBox);

            return new StackPane(avatarImageView, statusBorderPane);
        } else {
            return avatarImageView;
        }
    }

    public static File loadFileUsingFileChooser(Node anchor) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        return fileChooser.showOpenDialog(anchor.getScene().getWindow());
    }

    public static Node getImageView(byte[] imageBytes, double fitWidth, double fitHeight) {
        var imageView = new ImageView();
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setImage(convertByteArrayToImage(imageBytes));
        return imageView;
    }
}
