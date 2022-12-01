package sami.talkaddict.application.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import sami.talkaddict.infrastructure.utils.managers.AuthenticationManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            System.out.println("Current logged in user name: " + AuthenticationManager.getLoggedInUser().getUsername());
        } catch (Exception ex) {
            SceneFxManager.showAlertDialog(
                    "Logged in user error",
                    "Failed to get logged in user!",
                    Alert.AlertType.ERROR
            );
        }
    }
}
