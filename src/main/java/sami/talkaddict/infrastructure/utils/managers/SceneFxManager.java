package sami.talkaddict.infrastructure.utils.managers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.synedra.validatorfx.ValidationMessage;
import net.synedra.validatorfx.ValidationResult;
import sami.talkaddict.TalkaddictApplication;
import sami.talkaddict.infrastructure.utils.Config;

import java.io.IOException;

public class SceneFxManager {
    public static void redirectTo(String path, Node anchor) throws IOException {
        var loader = new FXMLLoader(TalkaddictApplication.class.getResource(path));
        var stage = (Stage) anchor.getScene().getWindow();
        var scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void showAlertDialog(String title, String message, Alert.AlertType type) {
        var alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setResizable(true);
        alert.show();
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