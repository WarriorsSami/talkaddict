package sami.talkaddict.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField userEmailField;
    @FXML
    private PasswordField userPasswordField;
    @FXML
    private Button loginUserButton;

    private Parent fxml;

    @FXML
    public void openHomeView(ActionEvent actionEvent) throws IOException {
        SceneFxManager.showAlertDialog("Login Error", "Login failed", Alert.AlertType.ERROR);
    }}
