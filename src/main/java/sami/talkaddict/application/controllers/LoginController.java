package sami.talkaddict.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sami.talkaddict.TalkaddictApplication;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.SceneRedirectManagerFx;

import java.io.IOException;
import java.util.Objects;

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
        SceneRedirectManagerFx.redirectTo(Config.HOME_VIEW, loginUserButton);
    }}
