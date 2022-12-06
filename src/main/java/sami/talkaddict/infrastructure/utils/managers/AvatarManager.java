package sami.talkaddict.infrastructure.utils.managers;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
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
        var file = getRandomFileFromDirectory(Config.ValidationTweaks.AVATARS_DIRECTORY_PATH);
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
}
