package sami.talkaddict.infrastructure.utils.managers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.synedra.validatorfx.ValidationMessage;
import net.synedra.validatorfx.ValidationResult;
import org.controlsfx.control.Notifications;
import sami.talkaddict.TalkaddictApplication;
import sami.talkaddict.application.models.chat.DirectMessageFx;
import sami.talkaddict.di.Config;

import java.io.IOException;

public class SceneFxManager {
    public static void redirectTo(String path, Node anchor) throws IOException {
        var loader = new FXMLLoader(TalkaddictApplication.class.getResource(path));
        var stage = (Stage) anchor.getScene().getWindow();
        var scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static Pane loadPane(String path) throws IOException {
        var loader = new FXMLLoader(TalkaddictApplication.class.getResource(path));
        return loader.load();
    }

    public static void addMessageTextLabelToPane(DirectMessageFx data, BorderPane messageBorderPane) {
        var messageLabel = new Label(data.MessageText.get());
        messageLabel.setFont(Font.font("Ubuntu Mono", FontWeight.BOLD, 18));
        messageLabel.setTextFill(Color.BLACK);
        var textHBox = new HBox(messageLabel);
        textHBox.setAlignment(Pos.CENTER);

        messageBorderPane.setCenter(textHBox);
    }

    public static void showAlertDialog(String title, String message, Alert.AlertType type) {
        var alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setResizable(true);
        alert.show();
    }

    public static void showToastNotification(String title, String message, Duration duration) {
        Notifications.create()
                .darkStyle()
                .title(title)
                .text(message)
                .hideAfter(duration)
                .showInformation();
    }

    public static void initMFXNotificationSystem(Stage stage) {
        Platform.runLater(() -> {
            MFXNotificationSystem.instance().initOwner(stage);
            MFXNotificationCenterSystem.instance().initOwner(stage);
        });
    }

    public static void showToastNotification(String title, String message) {
        MFXNotificationSystem.instance().setPosition(NotificationPos.BOTTOM_RIGHT).publish(
              new CustomNotification()
                      .title(title)
                      .message(message)
        );
    }

    private static class CustomNotification extends MFXSimpleNotification {
        private final StringProperty headerText = new SimpleStringProperty("Notification Header");
        private final StringProperty contentText = new SimpleStringProperty();

        public CustomNotification() {
            MFXIconWrapper icon = new MFXIconWrapper(FontResources.CHECK_CIRCLE.getDescription(), 23,  Color.GREEN, 32);
            Label headerLabel = new Label();
            headerLabel.textProperty().bind(headerText);

            HBox header = new HBox(15, icon, headerLabel);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(InsetsFactory.of(5, 0, 5, 0));
            header.setMaxWidth(Double.MAX_VALUE);

            Label contentLabel = new Label();
            contentLabel.getStyleClass().add("content");
            contentLabel.textProperty().bind(contentText);
            contentLabel.setWrapText(true);
            contentLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            contentLabel.setAlignment(Pos.TOP_LEFT);

            BorderPane container = new BorderPane();
            container.getStyleClass().add("notification");
            container.setTop(header);
            container.setCenter(contentLabel);
            container.getStylesheets()
                    .add(String.valueOf(TalkaddictApplication.class.getResource("styles/talkaddict-style.css")));
            container.setMinHeight(100);
            container.setMaxWidth(400);

            setContent(container);
        }

        public String getHeaderText() {
            return headerText.get();
        }

        public StringProperty headerTextProperty() {
            return headerText;
        }

        public void setHeaderText(String headerText) {
            this.headerText.set(headerText);
        }

        public CustomNotification title(String title) {
            setHeaderText(title);
            return this;
        }

        public CustomNotification message(String message) {
            setContentText(message);
            return this;
        }

        public String getContentText() {
            return contentText.get();
        }

        public StringProperty contentTextProperty() {
            return contentText;
        }

        public void setContentText(String contentText) {
            this.contentText.set(contentText);
        }
    }

    public static void togglePasswordField(PasswordField passwordField,
                                           TextField passwordTextField,
                                           FontAwesomeIconView passwordToggler) {
        if (passwordToggler.getGlyphName().equals(Config.FxmlSettings.EYE_SLASH_GLYPH)) {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
            passwordToggler.setGlyphName(Config.FxmlSettings.EYE_GLYPH);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
            passwordToggler.setGlyphName(Config.FxmlSettings.EYE_SLASH_GLYPH);
        }
    }

    public static String removeDuplicateErrors(ValidationResult validationResult) {
        var str = new StringBuilder();
        for (ValidationMessage msg : validationResult.getMessages()) {
            if (!str.toString().contains(msg.getText())) {
                str.append("- ").append(msg.getText()).append("\n");
            }
        }

        return str.toString();
    }
}
